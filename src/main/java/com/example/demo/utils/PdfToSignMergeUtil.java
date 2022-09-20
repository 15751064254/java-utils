package com.example.demo.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <!--项目要使用iText，必须引入jar包。才能使用，maven依赖如下：-->
 * <dependency>
 * <groupId>com.itextpdf</groupId>
 * <artifactId>itextpdf</artifactId>
 * <version>5.5.13</version>
 * </dependency>
 * <p>
 * <!--输出中文，还要引入下面itext-asian.jar包：-->
 * <dependency>
 * <groupId>com.itextpdf</groupId>
 * <artifactId>itext-asian</artifactId>
 * <version>5.2.0</version>
 * </dependency>
 */
public class PdfToSignMergeUtil {

    /**
     * 山东模板
     */
    public static final String LIFE_INSURANCE_TEMPLATE_PATH_SHANDONG = "pdf\\00_1025_和谐健康人身保险投保提示书_山东.pdf";

    /**
     * 其它模板
     */
    public static final String LIFE_INSURANCE_TEMPLATE_PATH_OTHER = "pdf\\01_1016_和谐健康人身保险投保提示书.pdf";

    /**
     * 投保声明与授权书
     */
    public static final String STATEMENT_TEMPLATE_PATH = "pdf\\02_投保声明与授权书.pdf";

    /**
     * 签名
     */
    public static final String SIGN_IMG_PATH = "pdf\\\\HXJDT000000000026805_S101_1_64556.gif";

    /**
     * 将参数保存并且导出新的pdf
     *
     * @param objectMap  需要保存的参数
     * @param newPdfPath 生成的新文件pdf的路径
     * @return
     */
    private static void pdfOut(Map<String, Object> objectMap, String templatePath, String newPdfPath) {

        long startTimeMillis = System.currentTimeMillis();

        FileOutputStream fileOutputStream = null;
        PdfReader reader = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        PdfStamper stamper = null;
        Document document = null;
        PdfCopy copy = null;
        //生成的新文件pdf的路径
        // String newPDFPath = "C:\\Users\\Administrator\\Desktop\\1.pdf";
        try {

            //输出流
            fileOutputStream = new FileOutputStream(newPdfPath);

            //读取pdf模板
            reader = new PdfReader(templatePath);

            //字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中。
            byteArrayOutputStream = new ByteArrayOutputStream();

            // 使用pdf stamper 修改现有文档
            stamper = new PdfStamper(reader, byteArrayOutputStream);

            // 提取pdf中的表单
            AcroFields acroFields = stamper.getAcroFields();

            //使用微软雅黑字体显示中文
            BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            acroFields.addSubstitutionFont(baseFont);

            Map<String, String> mapField = (Map<String, String>) objectMap.get("objectMap");
            if (mapField != null) {
                for (String key : mapField.keySet()) {
                    String value = mapField.get(key);
                    acroFields.setFieldProperty(key, "textfont", baseFont, null); //设置中文
                    acroFields.setField(key, value);
                }
            }


            //图片类的内容处理
            Map<String, String> imageMap = (Map<String, String>) objectMap.get("imageMap");
            if (imageMap != null) {
                for (String key : imageMap.keySet()) {
                    String value = imageMap.get(key);
                    String imgPath = value;
                    int pageNo = acroFields.getFieldPositions(key).get(0).page;
                    Rectangle signRect = acroFields.getFieldPositions(key).get(0).position;
                    float x = signRect.getLeft();
                    float y = signRect.getBottom();
                    //根据路径读取图片
                    Image image = Image.getInstance(imgPath);
                    //获取图片页面
                    PdfContentByte under = stamper.getOverContent(pageNo);
                    //图片大小自适应
                    image.scaleToFit(signRect.getWidth(), signRect.getHeight());
                    //添加图片
                    image.setAbsolutePosition(x, y);
                    under.addImage(image);

                }
            }


            //true代表生成的PDF文件不可编辑，false可编辑
            stamper.setFormFlattening(true);
            stamper.close();

            document = new Document();
            copy = new PdfCopy(document, fileOutputStream);
            document.open();
            //循环是处理成品只显示一页的问题
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                PdfImportedPage importPage = copy.getImportedPage(new PdfReader(byteArrayOutputStream.toByteArray()), i);
                copy.addPage(importPage);
            }

            byteArrayOutputStream.close();
            document.close();
            copy.close();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (copy != null) {
                    copy.close();
                }
                if (document != null) {
                    document.close();
                }
                if (stamper != null) {
                    stamper.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("PDF生成耗时:[" + (System.currentTimeMillis() - startTimeMillis) + "]毫秒");
        }

    }


    public static void main(String[] args) {
        Map<String, String> textMap = new HashMap();
        textMap.put("riskNo", "11140399002201668262");
        //textMap.put("tbSignName", "姜克兵");
        textMap.put("tbSignDate", "2018年6月12日");

        textMap.put("year", "2018");
        textMap.put("month", "06");
        textMap.put("day", "12");

        textMap.put("agentSignName", "姜克兵");
        textMap.put("agentCode", "JD00168597");
        textMap.put("agentPhone", "13387808900");

        Map<String, String> mapImage = new HashMap();
        mapImage.put("tbSignName", SIGN_IMG_PATH);

        Map<String, Object> objectMap = new HashMap();
        objectMap.put("objectMap", textMap);
        objectMap.put("imageMap", mapImage);

//        PdfToSignMergeUtil.pdfOut(objectMap, LIFE_INSURANCE_TEMPLATE_PATH_SHANDONG, "C:\\work\\hexiehealth\\demo\\pdf\\人身保险投保提示书_山东.pdf");
//        Map<Integer, String> imageFilePathOne = new HashMap<>();
//        imageFilePathOne.put(0, "C:\\work\\hexiehealth\\demo\\pdf\\人身保险投保提示书_山东.gif");
//        PdfToImgUtil.pdf2Image("C:\\work\\hexiehealth\\demo\\pdf\\人身保险投保提示书_山东.pdf", imageFilePathOne, 100);
//
        PdfToSignMergeUtil.pdfOut(objectMap, STATEMENT_TEMPLATE_PATH, "C:\\work\\hexiehealth\\demo\\pdf\\投保声明与授权书.pdf");
        Map<Integer, String> imageFilePathOne = new HashMap<>();
        imageFilePathOne.put(0, "C:\\work\\hexiehealth\\demo\\pdf\\投保声明与授权书.gif");
        PdfToImgUtil.pdf2Image("C:\\work\\hexiehealth\\demo\\pdf\\投保声明与授权书.pdf", imageFilePathOne, 100);
        Map<Integer, String> imageFilePath = new HashMap<>();
        imageFilePath.put(0, "C:\\work\\hexiehealth\\demo\\pdf\\投保声明与授权书第一页.gif");
        imageFilePath.put(1, "C:\\work\\hexiehealth\\demo\\pdf\\投保声明与授权书第二页.gif");
        PdfToImgUtil.pdf2Image("C:\\work\\hexiehealth\\demo\\pdf\\投保声明与授权书.pdf", imageFilePath, 100);


    }

}