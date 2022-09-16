package com.example.demo.core;

import com.example.demo.service.AreaZDJS;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.OkHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AreaServiceMain implements ApplicationRunner {

    @Value("${bsapp.url}")
    private String bsAppUrl;

    @Value("${bsapp.cookie}")
    private String cookie;

    @Value("${local.file.path}")
    private String bsAppFilePath;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AreaServiceMain--------------------------------------------------AreaServiceMain");
        String areaName = null;
        String path = bsAppFilePath;
        String url = bsAppUrl + "tbAreaManager/addArea.action";
        log.info("path:[{}], url:[{}]", path, url);
        try {
            List<String> allFileName = FileUtil.getAllFileName(path);

            for (String fileName : allFileName) {
                areaName = fileName;
                ArrayList<String> arrayList = FileUtil.readFile(path + fileName);
                Map<String, String> map = AreaZDJS.saveDate(fileName, arrayList);
                Response response = OkHttpClientUtil.getInstance().postData(url, map, cookie);
                String string = response.body().string();
                if(!"{\"displayMessage\":\"success\"}".equals(string)){
                    return;
                }
                log.info("[{}] SUCCESS, OkHttpStatus:[{}]", fileName, string);
                log.info("--------------------------------------------------");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("insert area: areaName:[{}]", areaName);
            e.printStackTrace();
            return;
        }

        log.info("AreaServiceMain--------------------------------------------------AreaServiceMain");
    }
}
