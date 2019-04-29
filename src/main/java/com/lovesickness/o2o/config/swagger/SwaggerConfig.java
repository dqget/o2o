package com.lovesickness.o2o.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * Function: swagger配置类
 *
 * @author zhangyu
 * Date: 2018/10/12 下午2:23
 * @since JDK 1.8
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private Boolean enabledSwagger;

    @Bean
    public Docket createRestApi() {
        AuthorizationScope[] authScopes = new AuthorizationScope[1];
        authScopes[0] = new AuthorizationScopeBuilder()
                .scope("global")
                .description("full access")
                .build();
        SecurityReference securityReference = SecurityReference.builder()
                .reference("Authorization")
                .scopes(authScopes)
                .build();

        List<SecurityContext> securityContexts = Collections.singletonList(SecurityContext
                .builder()
                .securityReferences(Collections.singletonList(securityReference))
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //是否开启Swagger
                .enable(enabledSwagger)
                .select()
                //扫描所有有注解的api，用这种方式更灵活
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //指定扫描添加了@ApiOperation注解的请求
                .apis(RequestHandlerSelectors.basePackage("com.lovesickness.o2o.web"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(new ApiKey("Authorization", "Authorization", "header")))
                .securityContexts(securityContexts);
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("商铺助手")
                .description("主营鲜花销售")
                .version("0.0.1")
                .build();
    }

}