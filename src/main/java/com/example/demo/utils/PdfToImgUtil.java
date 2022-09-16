package com.example.demo.utils;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <!--PDF文件转PNG图片，全部页数-->
 * <dependency>
 * <groupId>org.apache.pdfbox</groupId>
 * <artifactId>pdfbox</artifactId>
 * <version>2.0.24</version>
 * </dependency>
 */
public class PdfToImgUtil {

    /***
     * PDF文件转PNG图片，全部页数
     *
     * @param PdfFilePath pdf完整路径
     * @param imgFilePath img完整路径 第一张图片 index:0
     * @param dpi dpi越大转换后越清晰，相对转换速度越慢
     * @return
     */
    public static void pdf2Image(String PdfFilePath, Map<Integer, String> imgFilePath, int dpi) {
        File file = new File(PdfFilePath);
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            /* dpi越大转换后越清晰，相对转换速度越慢 */
            List<BufferedImage> picList = new ArrayList<>();
            for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                image.flush();
                picList.add(image);
            }
            if (imgFilePath != null && imgFilePath.size() == 1) {
                yPic(picList, imgFilePath.get(0));
            } else {
                xyPic(picList, imgFilePath);
            }

            System.out.println("PDF文档转PNG图片成功！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pdDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成图片
     *
     * @param picList 文件流数组
     * @param outPath 输出路径
     */
    public static void xyPic(List<BufferedImage> picList, Map<Integer, String> outPath) {// 处理图片
        if (picList == null || picList.size() <= 0) {
            System.out.println("图片数组为空!");
            return;
        }
        if (outPath == null || outPath.size() <= 0) {
            System.out.println("保存图片路径为空!");
            return;
        }
        try {
            int height = 0; // 高度
            int width = 0; // 宽度
            int picNum = picList.size();// 图片的数量

            int[] heightArray = new int[picNum];
            int[] widthArray = new int[picNum];

            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
            int[] _imgRGB; // 保存一张图片中的RGB数据

            BufferedImage buffer = null; // 保存图片流
            for (int i = 0; i < picNum; i++) {
                buffer = picList.get(i);

                heightArray[i] = height = buffer.getHeight();// 图片高度
                widthArray[i] = width = buffer.getWidth();// 图片宽度

                _imgRGB = new int[width * height];// 从图片中读取RGB
                _imgRGB = buffer.getRGB(0, 0, width, height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            // 生成新图片
            for (int i = 0; i < picNum; i++) {
                height = heightArray[i];
                width = widthArray[i];
                BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                imageResult.setRGB(0, 0, width, height, imgRGB.get(i), 0, width); // 写入流中
                File outFile = new File(outPath.get(i));
                ImageIO.write(imageResult, "jpg", outFile);// 写图片
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("写入JPG图片成功！");
        }
    }

    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     *
     * @param picList 文件流数组
     * @param outPath 输出路径
     */
    public static void yPic(List<BufferedImage> picList, String outPath) {// 纵向处理图片
        if (picList == null || picList.size() <= 0) {
            System.out.println("图片数组为空!");
            return;
        }
        if (outPath == null || outPath.length() <= 0) {
            System.out.println("保存图片路径为空!");
            return;
        }
        try {
            int height = 0, // 总高度
                    width = 0, // 总宽度
                    _height = 0, // 临时的高度 , 或保存偏移高度
                    __height = 0, // 临时的高度，主要保存每个高度
                    picNum = picList.size();// 图片的数量
            int[] heightArray = new int[picNum]; // 保存每个文件的高度
            BufferedImage buffer = null; // 保存图片流
            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
            int[] _imgRGB; // 保存一张图片中的RGB数据
            for (int i = 0; i < picNum; i++) {
                buffer = picList.get(i);
                heightArray[i] = _height = buffer.getHeight();// 图片高度
                if (i == 0) {
                    width = buffer.getWidth();// 图片宽度
                }
                height += _height; // 获取总高度
                _imgRGB = new int[width * _height];// 从图片中读取RGB
                _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            _height = 0; // 设置偏移高度为0
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                if (i != 0) {
                    _height += __height; // 计算偏移高度
                }
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
            }
            File outFile = new File(outPath);
            ImageIO.write(imageResult, "jpg", outFile);// 写图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("写入JPG图片成功！");
        }
    }


    public static void main(String[] args) throws IOException {
//        pdf2Image("C:\\jdt_attach\\dev\\HXDEV000000000021582\\HXDEV000000000021582_211003_0_07101_.pdf", "C:\\jdt_attach\\dev\\HXDEV000000000021582\\1.gif", 200);
        Map<Integer, String> imgFilePathMap = new HashMap<>();
        imgFilePathMap.put(0, "pdf\\1.gif");
        imgFilePathMap.put(1, "pdf\\2.gif");
        PdfToImgUtil.pdf2Image("pdf\\1.pdf", imgFilePathMap, 200);
//        pdf2Image("pdf文件地址\\XX.pdf", "转换后图片地址\\XX.png", 200);
    }
}
