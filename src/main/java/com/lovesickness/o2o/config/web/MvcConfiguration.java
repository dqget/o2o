package com.lovesickness.o2o.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.lovesickness.o2o.interceptor.shopadmin.ShopLoginInterceptor;
import com.lovesickness.o2o.interceptor.shopadmin.ShopPermissionInterceptor;
import com.lovesickness.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * WebMvcConfigurerAdapter过时  WebMvcConfigurationSupport  视图解析器
 * <p>
 * 一个类实现ApplicationContextAware后，这个类就可以获得ApplicationContext中的所有bean
 *
 * @author 懿
 */

/**
 * 等价于<mvc:annotation-driven/>
 */
@EnableWebMvc
@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware {
    /**
     * spring容器
     */
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${kaptcha.border}")
    private String border;
    @Value("${kaptcha.textproducer.font.color}")
    private String fontColor;
    @Value("${kaptcha.image.width}")
    private String width;
    @Value("${kaptcha.textproducer.char.string}")
    private String string;
    @Value("${kaptcha.image.height}")
    private String height;
    @Value("${kaptcha.textproducer.font.size}")
    private String size;
    @Value("${kaptcha.noise.color}")
    private String noiseColor;
    @Value("${kaptcha.textproducer.char.length}")
    private String length;
    @Value("${kaptcha.textproducer.font.names}")
    private String fontNames;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 配置静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + PathUtil.getImageBasePath() + "/upload/");
    }

    /**
     * 定义默认的请求处理器
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置Spring容器
        viewResolver.setApplicationContext(applicationContext);
        //设置缓存
        viewResolver.setCache(false);
        //前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createCommonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        // 1024*1024*20 = 20M
        multipartResolver.setMaxUploadSize(20971520L);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }

    @Bean
    public ServletRegistrationBean<KaptchaServlet> servletRegistrationBean() {
        ServletRegistrationBean<KaptchaServlet> kaptchaServlet = new ServletRegistrationBean<>(new KaptchaServlet(), "/Kaptcha");
        kaptchaServlet.addInitParameter("kaptcha.border", border);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.color", fontColor);
        kaptchaServlet.addInitParameter("kaptcha.image.width", width);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.string", string);
        kaptchaServlet.addInitParameter("kaptcha.image.height", height);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.size", size);
        kaptchaServlet.addInitParameter("kaptcha.noise.color", noiseColor);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.length", length);
        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.names", fontNames);
        return kaptchaServlet;
    }
//    @Bean
//    public ServletRegistrationBean servletRegistrationBean(){
//        ServletRegistrationBean kaptchaServlet = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
//        kaptchaServlet.addInitParameter("kaptcha.border",border);
//        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.color",fontColor);
//        kaptchaServlet.addInitParameter("kaptcha.image.width",width);
//        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.string",string);
//        kaptchaServlet.addInitParameter("kaptcha.image.height",height);
//        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.size",size);
//        kaptchaServlet.addInitParameter("kaptcha.noise.color",noiseColor);
//        kaptchaServlet.addInitParameter("kaptcha.textproducer.char.length",length);
//        kaptchaServlet.addInitParameter("kaptcha.textproducer.font.names",fontNames);
//        return  kaptchaServlet;
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截路径
        String interceptPath = "/shopadmin/**";
        //生成拦截器对象
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截路径
        loginIR.addPathPatterns(interceptPath);
        //店铺授权管理添加界面
        loginIR.excludePathPatterns("/shopadmin/addshopauthmap");
        //生成权限验证的拦截器
        InterceptorRegistration permissionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        permissionIR.addPathPatterns(interceptPath);
        //配置不拦截路径
        //店铺列表界面
        permissionIR.excludePathPatterns("/shopadmin/shoplist", "/shopadmin/getshoplist");
        //新增店铺
        permissionIR.excludePathPatterns("/shopadmin/getshopinitinfo", "/shopadmin/registershop", "/shopadmin/shopoperation");
        //店铺管理界面
        permissionIR.excludePathPatterns("/shopadmin/shopmanage", "/shopadmin/getshopmanagementinfo");
        //店铺授权管理添加界面
        permissionIR.excludePathPatterns("/shopadmin/addshopauthmap");

    }
}

