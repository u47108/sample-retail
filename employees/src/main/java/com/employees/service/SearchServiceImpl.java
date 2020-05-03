package com.employees.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate.ExistsMode;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.employees.dto.ResponseSearch;
import com.employees.dto.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@PropertySource("classpath:/application-${spring.profiles.active:local}.properties")
public class SearchServiceImpl implements SearchService {

	private String remoteDirectory;
	private String endpoint;
	private RestTemplate restTemplate;
	private FtpRemoteFileTemplate templateFtp;
	private final HttpHeaders httpHeaders;
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Autowired
	public SearchServiceImpl(RestTemplate restTemplate, @Value("${be.endpoint}") String endpoint,
			FtpRemoteFileTemplate templateFtp,
			@Value("${ftp.remote.directory}") String remoteDirectory) {
		LOGGER.debug("Endpoint: {}", endpoint);
		this.templateFtp = templateFtp;
		this.restTemplate = restTemplate;
		this.endpoint = endpoint;
		this.remoteDirectory=remoteDirectory;
		httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	}

	@Override
	public ResponseSearch search(SearchRequest request) {
		boolean errorFtp = false;
		LOGGER.info("Init authorize method for call the search");
		ResponseSearch response = null;
		try {
			LOGGER.debug("Endpoint: {}", endpoint);
			response = restTemplate.getForObject(this.endpoint, ResponseSearch.class);
			LOGGER.info("<- search Response: {}", response);

		} catch (HttpStatusCodeException | ResourceAccessException exception) {
			LOGGER.error("Consuming REST error: {}", exception);
			response = new ResponseSearch();
			response.setStatus("Error when consume employees");
		}
		File file = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.debug("intentando crear archivo: {}", request.getBody());
			String jsonArray = mapper.writeValueAsString(response.getData());
			file = new File(System.getProperty("java.io.tmpdir"), request.getBody());
			FileWriter writer = new FileWriter(file);
			writer.write(jsonArray);
			writer.close();
			templateFtp.setExistsMode(ExistsMode.NLST);
			templateFtp.setRemoteDirectoryExpression(new LiteralExpression(this.remoteDirectory));
			templateFtp.send(new GenericMessage<>(file));
		} catch (MessagingException | IOException e) {
			LOGGER.error("Error subiendo a FTP error: {}", e.getCause().getMessage());
			errorFtp = true;
		}
		if (errorFtp) {
			response = new ResponseSearch();
			response.setStatus("Error subiendo archivo");
		}
		return response;
	}

}
