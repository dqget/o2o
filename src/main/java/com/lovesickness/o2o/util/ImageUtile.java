package com.lovesickness.o2o.util;

import com.lovesickness.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtile {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    private static String basePath = PathUtil.getImageBasePath();
    private static Random r = new Random();

    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("D:/SSH视频/054 SSM到Spring Boot-从零开发校园商铺平台 加/images/item/imgTest.jpg"))
                .size(200, 200)
                .watermark(Positions.BOTTOM_RIGHT,
                        ImageIO.read(new File(basePath + "/waterMark.png")), 0.25f).outputQuality(0.8f)
                .toFile("D:/SSH视频/054 SSM到Spring Boot-从零开发校园商铺平台 加/images/item/imgTest2.jpg");
        ;
    }

    public static String generateThumbnails(ImageHolder image, String targetAddr) {
        //获取一个随机名字
        String realFileName = getRandomFileName();
        //拓展名
        String extension = getFileExtension(image.getImageName());
        //创建路径
        makeDirPath(targetAddr);
        //
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImageBasePath() + relativeAddr);
        try {
            Thumbnails.of(image.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "/waterMark.png")), 0.25f).outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 处理详情图
     *
     * @param image
     * @param targetAddr
     * @return
     */
    public static String generateNomalImg(ImageHolder image, String targetAddr) {
        //获取一个随机名字
        String realFileName = getRandomFileName();
        //拓展名
        String extension = getFileExtension(image.getImageName());
        //创建路径
        makeDirPath(targetAddr);
        //
        String relativeAddr = targetAddr + realFileName + extension;
        File dest = new File(PathUtil.getImageBasePath() + relativeAddr);
        try {
            Thumbnails.of(image.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "/waterMark.png")), 0.25f).outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImageBasePath() + targetAddr;
        File dirpPath = new File(realFileParentPath);
        if (!dirpPath.exists()) {
            dirpPath.mkdirs();
        }

    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        //获取随机五位数
        int ranNum = r.nextInt(89999) + 10000;
        String nowTimeStr = SIMPLE_DATE_FORMAT.format(new Date());
        return nowTimeStr + ranNum;

    }

    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImageBasePath() + storePath);
        if (fileOrPath.exists()) {
            if (fileOrPath.isDirectory()) {
                File files[] = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }
}
