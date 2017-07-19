package com.p2pdinner.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.p2pdinner.common.codes.ErrorCode;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import com.p2pdinner.common.filepicker.FilePickerOperations;
import com.p2pdinner.common.filepicker.FilePickerUploadResponse;
import com.p2pdinner.common.messagebuilders.ExceptionMessageBuilder;
import com.p2pdinner.common.s3.StorageOperations;
import com.p2pdinner.common.utils.P2PDinnerUtils;
import com.p2pdinner.exceptions.P2PDinnerDataException;
import com.p2pdinner.service.MenuDataService;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.transformers.MenuItemTransformer;
import com.p2pdinner.domain.vo.MenuItemVO;
import com.p2pdinner.domain.vo.P2PResponseBuilder;
import com.p2pdinner.domain.vo.P2PResponseBuilderImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Path("/menu")
public class MenuService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

	@Autowired
	private MenuDataService menuDataService;

	@Autowired
	private ExceptionMessageBuilder excepBuilder;

	@Autowired
	private StorageOperations storageOperations;

	@Autowired
	private FilePickerOperations filePickerOperations;
	

	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMenuItem(String mi) throws P2PDinnerException {
		Response response;
		MenuItemVO menuItem = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			menuItem = mapper.readValue(mi, MenuItemVO.class);

			if (menuItem == null) {
				throw excepBuilder.createException(ErrorCode.BAD_REQUEST, null, Locale.US);
			}
			if (StringUtils.isEmpty(mi)) {
				throw excepBuilder.createException(ErrorCode.BAD_REQUEST, null, Locale.US);
			}
			if (StringUtils.isEmpty(menuItem.getTitle())) {
				throw excepBuilder.createException(ErrorCode.MISSING_TITLE, null, Locale.US);
			}
			if (StringUtils.isEmpty(menuItem.getDinnerCategories())) {
				throw excepBuilder.createException(ErrorCode.INVALID_CATEGORY, null, Locale.US);
			}
			MenuItem menu = menuDataService.saveOrUpdateMenuItem(menuItem);
			P2PResponseBuilder<MenuItem, MenuItemVO> responseBuilder = new P2PResponseBuilderImpl<MenuItem, MenuItemVO>();
			MenuItemVO menuVO = new MenuItemVO();
			menuVO = responseBuilder.buildResponse(menu, menuVO, new MenuItemTransformer());
			response = Response.ok(menuVO).build();
		} catch (P2PDinnerException excep) {
			LOGGER.error(excep.getMessage(), excep);
			throw excep;
		} catch (P2PDinnerDataException excep) {
			switch (excep.getReason()) {
			case INVALID_MENU_ITEM:
				throw excepBuilder.createException(ErrorCode.INVALID_MENU_ITEM, new Object[] { menuItem.getId() }, Locale.US);
			default:
				throw excepBuilder.createException(ErrorCode.UNKNOWN, null, Locale.US);
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.ADD_MI_FAILED, new Object[] { e.getMessage() }, Locale.US);
		}
		return response;
	}

	@Path("/{menuItemId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response menuItemById(@PathParam("menuItemId") Integer menuItemId) {
		Response response;
		try {
			MenuItem menu = menuDataService.findMenuItemById(menuItemId);
			P2PResponseBuilder<MenuItem, MenuItemVO> responseBuilder = new P2PResponseBuilderImpl<MenuItem, MenuItemVO>();
			MenuItemVO menuVO = new MenuItemVO();
			menuVO = responseBuilder.buildResponse(menu, menuVO, new MenuItemTransformer());
			response = Response.ok(menuVO).build();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to add menu item to profile. Check request message").build();
		}
		return response;
	}

	@Path("/view/{profileId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response menuByProfile(@PathParam("profileId") Integer profileId) {
		Response response;
		List<MenuItem> menu = menuDataService.findMenuByProfileId(profileId);
		P2PResponseBuilder<MenuItem, MenuItemVO> responseBuilder = new P2PResponseBuilderImpl<MenuItem, MenuItemVO>();
		List<MenuItemVO> menuItems = new ArrayList<MenuItemVO>();
		try {
			for (MenuItem m : menu) {
				MenuItemVO menuVO = new MenuItemVO();
				menuVO = responseBuilder.buildResponse(m, menuVO, new MenuItemTransformer());
				menuItems.add(menuVO);
			}
			response = Response.ok(menuItems).build();
			return response;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch menu items for profile. Check request message").build();
		}
		return response;

	}

	/**
	 * 
	 * @param body
	 *            JSON request format - [1, 2, 3, 4]
	 * @return
	 */
	@Path("/remove")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteMenuItems(String body) {
		Response response = Response.ok().build();
		try {
			Set<Integer> ids = new HashSet<Integer>();
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(body);
			if (rootNode instanceof ArrayNode) {
				ArrayNode menuItems = (ArrayNode) rootNode;
				Iterator<JsonNode> iterator = menuItems.iterator();
				while (iterator.hasNext()) {
					ids.add(iterator.next().asInt());
				}
			}
			menuDataService.deleteMenuItems(ids);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity("Failed to delete item").build();
		}
		return response;
	}

	@Path("/upload")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
	public Response uploadImage(@Multipart(value = "imagefile", required = true) Attachment attachment) throws P2PDinnerException{
		String filename = attachment.getContentDisposition().getParameter("filename");
		String uri = StringUtils.EMPTY;
		try {
			if (!StringUtils.isEmpty(filename)) {
				LOGGER.info(filename);
				if (P2PDinnerUtils.isValidImageExtn(filename)) {
					String fileExtn = P2PDinnerUtils.fileExtn(filename);
					java.nio.file.Path path = Files.createTempFile("img" + System.currentTimeMillis(), "." + fileExtn);
					Files.copy(attachment.getDataHandler().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					LOGGER.info("Temp File {}", path);
					// Loading into s3
					FilePickerUploadResponse response = filePickerOperations.upload(path.toString());
					uri = response.getUrl();
				} else {
					throw excepBuilder.createException(ErrorCode.INVALID_FILE_EXTN, new Object[] {P2PDinnerUtils.fileExtn(filename)}, Locale.US);
				}
			}
		} catch (P2PDinnerException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{ e.getMessage() }, Locale.US);
		}
		Map<String,Object> responseMap = new HashMap<>();
		responseMap.put("url", uri);
		responseMap.put("status", "OK");
		return Response.ok(responseMap).build();
	}

	@Path("/uploadImage")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
	public Response uploadImage(@FormParam("filename")String filename, @FormParam("image")String image) throws P2PDinnerException{
		String uri = StringUtils.EMPTY;
		try {
			if (!StringUtils.isEmpty(filename)) {
				LOGGER.info(filename);
				if (P2PDinnerUtils.isValidImageExtn(filename)) {
					String fileExtn = P2PDinnerUtils.fileExtn(filename);
					java.nio.file.Path path = Files.createTempFile("img" + System.currentTimeMillis(), "." + fileExtn);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decodeBase64(image));
					Files.copy(byteArrayInputStream, path, StandardCopyOption.REPLACE_EXISTING);
					LOGGER.info("Temp File {}", path);
					// Loading into s3
					FilePickerUploadResponse response = filePickerOperations.upload(path.toString());
					uri = response.getUrl();
				} else {
					throw excepBuilder.createException(ErrorCode.INVALID_FILE_EXTN, new Object[] {P2PDinnerUtils.fileExtn(filename)}, Locale.US);
				}
			}
		} catch (P2PDinnerException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{ e.getMessage() }, Locale.US);
		}
		Map<String,Object> responseMap = new HashMap<>();
		responseMap.put("url", uri);
		responseMap.put("status", "OK");
		return Response.ok(responseMap).build();
	}


}
