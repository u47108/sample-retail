package com.cleansing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cleansing.dto.MessagePub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@SpringBootApplication
@RestController
public class CleansingApplication {
	@Autowired
	private PubsubOutboundGateway messagingGateway;

	private static Storage storage = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(CleansingApplication.class);

	// [START init]
	static {
		storage = StorageOptions.getDefaultInstance().getService();
	}

	public static void main(String[] args) {
		SpringApplication.run(CleansingApplication.class, args);
	}

	@Bean
	public MessageChannel pubsubInputChannel() {
		return new DirectChannel();
	}

	@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
	public interface PubsubOutboundGateway {
		
		void sendToPubsub(MessagePub text);
	}
	/**
	 * This bean enables serialization/deserialization of Java objects to JSON allowing you
	 * utilize JSON message payloads in Cloud Pub/Sub.
	 * @param objectMapper the object mapper to use
	 * @return a Jackson message converter
	 */
	@Bean
	public JacksonPubSubMessageConverter jacksonPubSubMessageConverter(ObjectMapper objectMapper) {
		return new JacksonPubSubMessageConverter(objectMapper);
	}
	
	@Bean
	@ServiceActivator(inputChannel = "pubsubOutputChannel")
	public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {
		
		PubSubMessageHandler adapter = new PubSubMessageHandler(pubsubTemplate, "pubstub-cleansing");
		adapter.setPublishCallback(new ListenableFutureCallback<String>() {
			@Override
			public void onFailure(Throwable ex) {
				LOGGER.info("There was an error sending the message.");
			}

			@Override
			public void onSuccess(String result) {
				LOGGER.info("Message was sent successfully.");
			}
		});

		return adapter;
	}

	@PostMapping("/cleansing/api/postMessage")
	public String postMessage(@RequestParam("file") Part filePart) {
		String respuestaUpload = "No subido";
		try {
			respuestaUpload = uploadFile(filePart, "employee-files-in");
			MessagePub element= new MessagePub();
			element.setBody(filePart.getSubmittedFileName());
			LOGGER.info("Enviando mensage {}",element.toString());
			this.messagingGateway.sendToPubsub(element);
		} catch (Exception e) {
			LOGGER.error("Error en subir archivo al cloudstorage");
		}
		return respuestaUpload;
	}
	

	/**
	 * Uploads a file to Google Cloud Storage to the bucket specified in the
	 * BUCKET_NAME environment variable, appending a timestamp to end of the
	 * uploaded filename.
	 */
	@SuppressWarnings("deprecation")
	public String uploadFile(Part filePart, final String bucketName) throws IOException {
		final String fileName = filePart.getSubmittedFileName();
		// the inputstream is closed by default, so we don't need to close it here
		BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, fileName)
				// Modify access list to allow all users with link to read file
				.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER)))).build(),
				filePart.getInputStream());
		// return the public download link
		return blobInfo.getMediaLink();
	}
}
