package com.example.demo.utils;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.font.FontProvider;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Velocity
 *
 * 首先，我们在代码中初始化了VelocityEngine这个模板引擎，对其设置参数进行初始化，
 * 指定使用ClasspathResourceLoader来加载vm文件。然后我们就可以往VelocityContext这个Velocity
 * 容器中存放对象了，在vm文件中我们可以取出这些变量，从而进行模板输出。
 */
public class VelocityTest {
    // 初始化模板引擎
    private static final VelocityEngine velocityEngine = new VelocityEngine();

    static  {
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        // 指定编码格式，避免生成模板就造成乱码，影响到转pdf后的文件
        velocityEngine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        velocityEngine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        velocityEngine.init();
    }


    private static final String VM_PATH = "template/velocity/template.vm";


    public static void main(String[] args) throws IOException {

        // 获取模板文件
        Template template = VelocityTest.velocityEngine.getTemplate(VM_PATH);

        // 设置变量，velocityContext是一个类似map的结构
        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("headerName", "这是一个模板页面");

        Map<String, String> map = new HashMap<>();
        map.put("firstTitle","标题一");
        map.put("secondTitle","标题二");
        velocityContext.put("tabObj", map);

        List<String> list = new ArrayList<String>();
        list.add("人际沟通很重要");
        list.add("脚踏实地很重要");
        list.add("学海无涯");
        velocityContext.put("list", list);

        // 输出渲染后的结果
        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);
        System.out.println(stringWriter.toString());
        htmlToPdf(stringWriter.toString(), "C:\\work\\hexiehealth\\demo\\pdf\\VelocityTest.pdf");
    }

    //private static final String resourcesDir = System.getProperty("user.dir") + "/src/main/resources";
    //private static final String resourcesDir = ResourceUtils.getURL("classpath:").getPath();

    /**
     * html字符串转换PDF文件
     * @param htmlStr html字符串
     * @param outPath 保存PDF文件路径
     * @throws IOException
     */
    public static void htmlToPdf(String htmlStr, String outPath) throws IOException {
        //String resourcesDir = ResourceUtils.getURL("classpath:").getPath();
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addDirectory("C:/Windows/Fonts");
        //fontProvider.addDirectory(resourcesDir + "/fonts");
        properties.setFontProvider(fontProvider);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_8));
            outputStream = new FileOutputStream(outPath);
            HtmlConverter.convertToPdf(inputStream, outputStream, properties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }

    }
}
