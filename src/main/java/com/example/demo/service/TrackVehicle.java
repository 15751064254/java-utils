package com.example.demo.service;

import com.example.demo.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * 轨迹提取
 *
 * @author Administrator
 */
@Slf4j
public class TrackVehicle {

    public static void main(String[] args) {
        writeFile();
    }

    private static String charsetName = "GB2312";
    private static String filePath = "E:/iscsi-temp1/";
    private static String fileName = "116140249879211370372.txt";

    private static Double WGS84 = 600000.0;


    private static ArrayList<String> readFile(BufferedReader bufferedReader) throws IOException {
        ArrayList<String> arrayList = new ArrayList();

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        // 保留两位小数
        numberFormat.setMaximumFractionDigits(7);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        numberFormat.setRoundingMode(RoundingMode.UP);

        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            String[] split = str.split(":");
            String lon = numberFormat.format(Double.parseDouble(split[7]) / WGS84);
            String lat = numberFormat.format(Double.parseDouble(split[8]) / WGS84);
            arrayList.add(lon + "," + lat);
        }
        log.info("总共位置信息: {} 个", arrayList.size());
        return arrayList;
    }

    public static void writeFile() {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {

            fileInputStream = new FileInputStream(filePath + fileName);
            inputStreamReader = new InputStreamReader(fileInputStream, charsetName);
            bufferedReader = new BufferedReader(inputStreamReader);
            ArrayList arrayList = readFile(bufferedReader);
            String path = filePath + "test/";
            FileUtil.createDir(path);
            String fileName = path + "test.txt";
            FileUtil.createFile(fileName);
            FileUtil.writeFile(arrayList, fileName);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtil.close(fileInputStream, inputStreamReader, bufferedReader);
        }
    }


}
