package com.employees;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.employees.filters.CorsFilter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = { MultipartAutoConfiguration.class, JmxAutoConfiguration.class, })
@ComponentScan("com.employees")
@PropertySource("classpath:/application-${spring.profiles.active:local}.properties")
@EnableSwagger2
@EnableFeignClients(basePackages = "com.employees.client")
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

	// RestTemplate is deprecated - use Feign Client (EmployeeSearchClient) instead
	// Feign Client provides better type safety, declarative API, and easier configuration
	
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
