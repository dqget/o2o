package com.lovesickness.o2o.common.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = {"classpath:spring-aop.xml"})
public class AOPConfig {
}
