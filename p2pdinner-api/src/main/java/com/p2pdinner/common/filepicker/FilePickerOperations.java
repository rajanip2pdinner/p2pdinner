package com.p2pdinner.common.filepicker;

import com.p2pdinner.common.codes.ErrorCode;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import com.p2pdinner.common.messagebuilders.ExceptionMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Locale;

/**
 * Created by rajaniy on 7/13/15.
 */
@Component
public class FilePickerOperations implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilePickerOperations.class);

    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExceptionMessageBuilder excepBuilder;

    private String apiKey;
    private String apiSecretKey;

    private static final String FILEPICKER_API_KEY = "FILEPICKER_API_KEY";
    private static final String FILEPICKER_API_SECRET = "FILEPICKER_API_SECRET";
    private static final String FILEPICKER_URL =  "https://www.filepicker.io/api/store/S3";

    public FilePickerUploadResponse upload(String fileName) throws P2PDinnerException {
        LOGGER.info("Uploading file - {}", fileName);
        final MultiValueMap<String,Object> data = new LinkedMultiValueMap<String, Object>();
        try {
            Resource resource = new FileSystemResource(fileName);
            data.add("fileUpload", resource);
            data.add("filename", resource.getFile().getName());
            data.add("container", "uploads");
            URI uri = UriComponentsBuilder.fromHttpUrl(FILEPICKER_URL).queryParam("key", this.apiKey).build().toUri();
            FilePickerUploadResponse response = restTemplate.postForObject(uri, data, FilePickerUploadResponse.class);
            return response;
        } catch (Exception excep) {
            LOGGER.error(excep.getMessage(), excep);
            throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] {excep.getMessage()}, Locale.US);
        }

    }

    public void afterPropertiesSet() throws Exception {
        apiKey = !org.springframework.util.StringUtils.isEmpty(System.getenv(FILEPICKER_API_KEY)) ? System.getenv(FILEPICKER_API_KEY) : environment.getProperty("filepicker.api.key");
        apiSecretKey = !org.springframework.util.StringUtils.isEmpty(System.getenv(FILEPICKER_API_SECRET)) ? System.getenv(FILEPICKER_API_SECRET) : environment.getProperty("filepicker.api.secret");
    }
}
