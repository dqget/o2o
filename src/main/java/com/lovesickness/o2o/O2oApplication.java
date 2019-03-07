package com.lovesickness.o2o;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author 懿
 */
@SpringBootApplication
public class O2oApplication extends SpringBootServletInitializer {
    private static Logger logger = LoggerFactory.getLogger(O2oApplication.class);

    public static void main(String[] args) {
        logger.debug("springboot-------------------开始启动");
        Long startTime = System.currentTimeMillis();

        SpringApplication app = new SpringApplication(O2oApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
        Long endTime = System.currentTimeMillis();
        logger.debug("springboot-------------------启动完成");
        logger.debug("springboot-------------------启动时长 = " + (endTime - startTime));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(O2oApplication.class);

    }
}
