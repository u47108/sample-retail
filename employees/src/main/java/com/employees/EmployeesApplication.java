package com.employees;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.web.client.RestTemplate;
import com.employees.filters.CorsFilter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = { MultipartAutoConfiguration.class, JmxAutoConfiguration.class, })
@ComponentScan("com.employees")
@PropertySource("classpath:/application-${spring.profiles.active:local}.properties")
@EnableSwagger2
public class EmployeesApplication {
	/** The Constant DEFAULT_TIMEOUT. */
	private static final int DEFAULT_TIMEOUT = 25000;
	@Value("${ftp.url}")
	private String endpointFtp;
	@Value("${ftp.pass}")
	private String passFtp;
	@Value("${ftp.user}")
	private String userFtp;

	public static void main(String[] args) {
		SpringApplication.run(EmployeesApplication.class, args);
	}

	public Docket employeeApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.employees.controller")).build();
	}

	@Bean
	public RestTemplate rest() {
		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy).build();

			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
			HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
			httpRequestFactory.setHttpClient(httpClient);

			String sTimeout = System.getenv("TIMEOUT");
			// Valor por defecto
			int timeout = DEFAULT_TIMEOUT;
			if (sTimeout != null) {
				timeout = Integer.parseInt(sTimeout);
			}
			httpRequestFactory.setConnectionRequestTimeout(timeout);
			httpRequestFactory.setConnectTimeout(timeout);
			httpRequestFactory.setReadTimeout(timeout);

			return new RestTemplate(httpRequestFactory);
		} catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
			throw new BeanInitializationException("Can't generate Rest Template", e);
		}
	}
	
	@Bean
	public DefaultFtpSessionFactory ftpSessionFactory() {
		DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
		sf.setHost(this.endpointFtp);
		sf.setUsername(this.userFtp);
		sf.setPassword(this.passFtp);
		sf.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
		sf.setConnectTimeout(DEFAULT_TIMEOUT);
		return sf;
	}


	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
		CorsFilter corsFilter = new CorsFilter();
		registrationBean.setFilter(corsFilter);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	@Bean
	public FtpRemoteFileTemplate template(DefaultFtpSessionFactory sf) {
		
		return new FtpRemoteFileTemplate(sf);
	}


}
