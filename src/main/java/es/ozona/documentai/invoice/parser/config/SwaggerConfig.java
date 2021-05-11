package es.ozona.documentai.invoice.parser.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
        		.useDefaultResponseMessages(false)
        		.directModelSubstitute(Object.class, java.lang.Void.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("es.ozona"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo(){
        return new ApiInfo(
                "GPC Document AI connector",
                "Conector API para document AI.",
                "1.0.0",
                "Terms of Service",
                new Contact("Xose Eijo", "ozona.es", "xose.eijo@ozona.es"),
                "",
                "",
                Collections.emptyList());
    }

}

