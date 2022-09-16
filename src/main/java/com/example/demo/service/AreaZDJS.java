package com.example.demo.service;

import com.example.demo.constant.Constant;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.OkHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
public class AreaZDJS {


    public static void main(String[] args) {

        log.info("--------------------------------------------------");
        String path = Constant.FILE_PATH + "test/";
        String url = "http://127.0.0.1:8080/tbAreaManager/addArea.action";
        String cookie = "";
        String areaName = null;
        log.info("path:[{}], url:[{}]", path, url);
        try {
            List<String> allFileName = FileUtil.getAllFileName(path);
            for (String fileName : allFileName) {
                areaName = fileName;
                ArrayList<String> arrayList = FileUtil.readFile(path + fileName);
                Map<String, String> map = AreaZDJS.saveDate(fileName, arrayList);
                Response response = OkHttpClientUtil.getInstance().postData(url, map, cookie);
                String string = response.body().string();
                if (!"{\"displayMessage\":\"success\"}".equals(string)) {
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
        log.info("--------------------------------------------------");
    }


    public static Map<String, String> saveDate(String roadStr, ArrayList<String> trAreaList) {
        Map<String, String> paramMap = new HashMap<>(16);
        //固定数据
        paramMap.put("tbArea.opId", "1");
        paramMap.put("tbArea.entId", "1");
        paramMap.put("tbArea.tbAreaStatus", "1");
//        paramMap.put("trVehicleArea.areaId", null);
//        paramMap.put("trVehicleArea.id", null);
        paramMap.put("tbArea.saveAcType", "1");
        paramMap.put("tbArea.areaShape", "2");
        paramMap.put("trVehicleArea.areaBegintime", "1567612800");
        paramMap.put("trVehicleArea.areaEndtime", "1569859200");
        paramMap.put("trVehicleArea.areaUsetype", "7,14,8,11,2");
        paramMap.put("trVehicleArea.areaMaxspeed", "60");
        paramMap.put("trVehicleArea.superspeedTimes", "10");
        paramMap.put("trVehicleArea.connectedStatus", "0");
        paramMap.put("trVehicleArea.isOpen", "1");
        paramMap.put("trVehicleArea.regionCode", "1");
//        paramMap.put("trVehicleArea.bannedPassStartTime", null);
//        paramMap.put("trVehicleArea.bannedPassEndTime", null);
//        paramMap.put("tbArea.areaId", null);
        paramMap.put("trVehicleArea.areaDecide", "1");
        paramMap.put("isBindAllVehicle", "1");
        //动态数据
        String[] roadStrArr = roadStr.split("_");
        String areaName = roadStrArr[0] + roadStrArr[2] + roadStrArr[1];
        paramMap.put("tbArea.areaName", areaName);
        paramMap.put("trVehicleArea.roadLevel", String.valueOf(roadStrArr[2].charAt(1)));
        for (int i = 0; i < trAreaList.size(); i++) {
            String[] arr = trAreaList.get(i).split(" ");
            String maplon = "trAreaList[" + i + "].maplon";
            String maplat = "trAreaList[" + i + "].maplat";
            BigDecimal base = new BigDecimal(100000);
            BigDecimal lon = new BigDecimal(arr[0]).multiply(base);
            BigDecimal lat = new BigDecimal(arr[1]).multiply(base);
            paramMap.put(maplon, String.valueOf(lon.setScale(0, BigDecimal.ROUND_DOWN).intValue()));
            paramMap.put(maplat, String.valueOf(lat.setScale(0, BigDecimal.ROUND_DOWN).intValue()));
        }
        log.debug("areaName:[{}], trAreaList:[{}]", areaName, trAreaList.size());
        return paramMap;
    }


}
