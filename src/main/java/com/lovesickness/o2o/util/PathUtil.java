package com.lovesickness.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
    public static String separator = System.getProperty("file.separator");
    public static String winPath;
    public static String linuxPath;
    public static String shopPath;
    @Value("${win.base.path}")
    public void setWinPath(String winPath) {
        PathUtil.winPath = winPath;
    }
    @Value("${linux.base.path}")
    public void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }
    @Value("${shop.base.path}")
    public void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    public static String getImageBasePath() {
        String os = System.getProperty("os.name");
        String basePath;
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    public static String getShopImagePath(long shopId) {
        String imgPath =  shopPath + shopId + separator;
        imgPath = imgPath.replace("/", separator);
        return imgPath;
    }

    public static String getPersonInfoImagePath() {
        String personInfoImagePath = "/upload/images/item/personinfo/";
        personInfoImagePath = personInfoImagePath.replace("/", separator);
        return personInfoImagePath;
    }
}
