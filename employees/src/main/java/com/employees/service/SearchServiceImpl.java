package com.employees.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate.ExistsMode;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import com.employees.client.EmployeeSearchClient;
import com.employees.dto.ResponseSearch;
import com.employees.dto.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@PropertySource("classpath:/application-${spring.profiles.active:local}.properties")
public class SearchServiceImpl implements SearchService {

	private String remoteDirectory;
	private final EmployeeSearchClient employeeSearchClient;
	private FtpRemoteFileTemplate templateFtp;
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Autowired
	public SearchServiceImpl(EmployeeSearchClient employeeSearchClient,
			FtpRemoteFileTemplate templateFtp,
			@Value("${ftp.remote.directory}") String remoteDirectory) {
		this.templateFtp = templateFtp;
		this.employeeSearchClient = employeeSearchClient;
		this.remoteDirectory=remoteDirectory;
	}

	@Override
	public ResponseSearch search(SearchRequest request) {
		boolean errorFtp = false;
		LOGGER.info("Init authorize method for call the search");
		ResponseSearch response = null;
		try {
			LOGGER.debug("Calling Employee Search API via Feign Client");
			response = employeeSearchClient.searchEmployees();
			LOGGER.info("<- search Response: {}", response);

		} catch (Exception exception) {
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
