package com.sap.cloud.cmp.ord.service.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.PathSelectors;

@Configuration
public class SpringFoxConfig {

	@Value("${static.api.version}")
	private String version;

	@Value("${static.request_mapping_path}")
	private String path;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex(String.format("/%s.*",path)))
				.build()
				.apiInfo(metaData());
	}
	private ApiInfo metaData(){
		ApiInfo apiInfo = new ApiInfo("ORD service", "ORD service description", version, "", new Contact("", "", ""), "", "",  new ArrayList());
		return apiInfo;
	}
}