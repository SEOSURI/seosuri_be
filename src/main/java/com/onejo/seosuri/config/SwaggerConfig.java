package com.onejo.seosuri.config;

// http://localhost:9500/swagger-ui/index.html

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "Seosuri API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Seosuri API 명세서";

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version(API_VERSION)
                .title(API_NAME)
                .description(API_DESCRIPTION);

//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER).name("Authorization");

//        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                //.components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                //.security(Arrays.asList(securityRequirement))
                .info(info);
    }

}
