package com.p2pdinner.common.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.p2pdinner.common.codes.ErrorCode;
import com.p2pdinner.common.exceptions.P2PDinnerException;
import com.p2pdinner.common.messagebuilders.ExceptionMessageBuilder;
import com.p2pdinner.common.utils.P2PDinnerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

@Component
public class StorageOperations implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageOperations.class);
	private static final String AWS_ACCESS_KEY = "AWS_ACCESS_KEY";
	private static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";
	private static final String AWS_BUCKET_NAME = "AWS_BUCKET_NAME";

	private String awsAccessKey;
	private String awsSecretKey;
	private String awsBucket;

	@Autowired
	private Environment env;
	
	@Autowired
	private ExceptionMessageBuilder excepBuilder;

	public URL uploadObject(String filename) throws P2PDinnerException {
		LOGGER.info("Uploading file to s3 - {}", filename);
		try {
			URL imageUrl = null;
			TransferManager tm = new TransferManager(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
			String imageFileName = "image-" + System.currentTimeMillis() + "." + P2PDinnerUtils.fileExtn(filename);
			Upload upload = tm.upload(awsBucket, "uploads/" + imageFileName , new File(filename));
			try {
				upload.waitForCompletion();
				tm.getAmazonS3Client().setObjectAcl(awsBucket, "uploads/" + imageFileName ,CannedAccessControlList.PublicRead);
				GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(awsBucket, "uploads/" + imageFileName);
				imageUrl = tm.getAmazonS3Client().generatePresignedUrl(request);
				LOGGER.info("Uploading - {} - complete", filename);
			} catch (AmazonClientException amazonClientException) {
				LOGGER.error(amazonClientException.getMessage(), amazonClientException);
				throw excepBuilder.createException(ErrorCode.UPLOAD_ERROR, new Object[] {filename}, Locale.US);
			}
			return imageUrl;
		} catch (Exception excep) {
			LOGGER.error(excep.getMessage(), excep);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] {excep.getMessage()}, Locale.US);
		}
	}
	
	public Path readObject(String key) throws P2PDinnerException {
		AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsSecretKey));
		S3Object s3Object = s3Client.getObject(new GetObjectRequest(awsBucket, key));
		try {
			Path path = Files.createTempFile("", "");
			Files.copy(s3Object.getObjectContent(), path, StandardCopyOption.REPLACE_EXISTING);
			return path;
		} catch (Exception excp) {
			LOGGER.error(excp.getMessage(), excp);
			throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[] {excp.getMessage()}, Locale.US);
		}
	}
	

	public void afterPropertiesSet() throws Exception {
		awsAccessKey = !StringUtils.isEmpty(System.getenv(AWS_ACCESS_KEY)) ? System.getenv(AWS_ACCESS_KEY) : env.getProperty("aws.access.key");
		awsSecretKey = !StringUtils.isEmpty(System.getenv(AWS_SECRET_KEY)) ? System.getenv(AWS_SECRET_KEY) : env.getProperty("aws.secret.key");
		awsBucket = !StringUtils.isEmpty(System.getenv(AWS_BUCKET_NAME)) ? System.getenv(AWS_BUCKET_NAME) : env.getProperty("aws.bucket.name");
	}
}
