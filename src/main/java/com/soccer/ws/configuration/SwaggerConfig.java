package com.soccer.ws.configuration;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by u0090265 on 10/2/15.
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"com.soccer.ws.model", "com.soccer.ws.controllers"})
public class SwaggerConfig {
    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/api/v1/.*"))
                .build()
                .pathMapping("/")
                .apiInfo(metadata())
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,
                        Lists.newArrayList(new ResponseMessageBuilder()
                                .code(500)
                                .responseModel(new ModelRef("ErrorDetailDTO"))
                                .build()))
                .globalResponseMessage(RequestMethod.DELETE,
                        Lists.newArrayList(new ResponseMessageBuilder()
                                .code(500)
                                .responseModel(new ModelRef("ErrorDetailDTO"))
                                .build()))
                .globalResponseMessage(RequestMethod.PUT,
                        Lists.newArrayList(new ResponseMessageBuilder()
                                .code(500)
                                .responseModel(new ModelRef("ErrorDetailDTO"))
                                .build()))
                .globalResponseMessage(RequestMethod.POST,
                        Lists.newArrayList(new ResponseMessageBuilder()
                                        .code(500)
                                        .responseModel(new ModelRef("ErrorDetailDTO"))
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(400)
                                        .responseModel(new ModelRef("ValidationErrorDetailDTO"))
                                        .build()));
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Soccer API")
                .version("1.0")
                .contact(new Contact("Administrator", "", "voetbalsvk@gmail.com"))
                .build();
    }

    private List<? extends SecurityScheme> securitySchemes() {
        return Collections.<SecurityScheme>singletonList(new ApiKey("token", "X-Auth-Token", "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder().forPaths(PathSelectors.any()).securityReferences(securityReferences()).build());
    }

    private List<SecurityReference> securityReferences() {
        return Collections.singletonList(SecurityReference.builder().reference("token").scopes(new AuthorizationScope[0]).build());
    }


}
