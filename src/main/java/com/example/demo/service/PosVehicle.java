package com.example.demo.service;

import com.example.demo.constant.Constant;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.GpsUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 */

@Slf4j
public class PosVehicle {

    public static void main(String[] args) {
        writeFile(readFile());

//        initArea();
//        //海淀区
//        GpsHelper.Point point = new GpsHelper.Point(116.303713, 39.96593);
//        log.info("point: {}", GpsHelper.isPtInPoly(point.getX(), point.getY(), HAIDIAN.toArray(new GpsHelper.Point[HAIDIAN.size()])));
    }


    private static String areaPrefix = "亦庄";


    private static String beijingFilePath = Constant.FILE_PATH + "area/beijing";
    private static String daXingFilePath = Constant.FILE_PATH + "area/beijingzone/daxing";
    private static String tongZhouFilePath = Constant.FILE_PATH + "area/beijingzone/tongzhou";
    private static String haiDianFilePath = Constant.FILE_PATH + "area/haidian";
    private static String shunYiFilePath = Constant.FILE_PATH + "area/shunyi";
    private static String fangShanFilePath = Constant.FILE_PATH + "area/fangshan";

    // 0:北京全区域
    // 1:北京经济开发区(大兴区,通州区)
    // 2:海淀区
    // 3:顺义区,4房山区

    /**
     * 北京全区域
     */
    private static final List<GpsUtil.Point> BEIJING = new ArrayList<>();

    /**
     * 大兴区
     */
    private static final List<GpsUtil.Point> DAXING = new ArrayList<>();

    /**
     * 通州区
     */
    private static final List<GpsUtil.Point> TONGZHOU = new ArrayList<>();

    /**
     * 海淀区
     */
    private static final List<GpsUtil.Point> HAIDIAN = new ArrayList<>();

    /**
     * 顺义区
     */
    private static final List<GpsUtil.Point> SHUNYI = new ArrayList<>();

    /**
     * 房山区
     */
    private static final List<GpsUtil.Point> FANGSHAN = new ArrayList<>();

    /**
     * 初始化区域
     */
    public static void initArea() {
        try {

            readArea(beijingFilePath, BEIJING);
            readArea(daXingFilePath, DAXING);
            readArea(tongZhouFilePath, TONGZHOU);
            readArea(haiDianFilePath, HAIDIAN);
            readArea(shunYiFilePath, SHUNYI);
            readArea(fangShanFilePath, FANGSHAN);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取区域信息
     *
     * @param filePath
     * @param list
     */
    public static void readArea(String filePath, List<GpsUtil.Point> list) {
        log.info("初始化区域:{}", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            log.error("Can't Find " + filePath);
        }
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream((new File(filePath).getAbsolutePath()));
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                //new String(str,"UTF-8")
                String[] s = str.split(" ");
                GpsUtil.Point point = new GpsUtil.Point(Double.valueOf(s[0]), Double.valueOf(s[1]));
                list.add(point);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * @return
     */
    public static ArrayList<Map<String, List<String>>> readFile() {
        String path = "yizhuang/";
        ArrayList<Map<String, List<String>>> list = null;
        FileInputStream midFileInputStream = null;
        InputStreamReader midInputStreamReader = null;
        BufferedReader midBufferedReader = null;
        FileInputStream mifFileInputStream = null;
        InputStreamReader mifInputStreamReader = null;
        BufferedReader mifBufferedReader = null;
        try {
            midFileInputStream = new FileInputStream(Constant.FILE_PATH + path + "wl1.mid");
            midInputStreamReader = new InputStreamReader(midFileInputStream, "GB2312");
            midBufferedReader = new BufferedReader(midInputStreamReader);
            mifFileInputStream = new FileInputStream(Constant.FILE_PATH + path + "wln.mif");
            mifInputStreamReader = new InputStreamReader(mifFileInputStream, "GB2312");
            mifBufferedReader = new BufferedReader(mifInputStreamReader);
            list = readFile(midBufferedReader, mifBufferedReader);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(midFileInputStream, midInputStreamReader, midBufferedReader);
            close(mifFileInputStream, mifInputStreamReader, mifBufferedReader);
        }
        return list;
    }

    private static void close(FileInputStream fileInputStream, InputStreamReader inputStreamReader, BufferedReader bufferedReader) {
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<Map<String, List<String>>> readFile(BufferedReader midBufferedReader, BufferedReader mifBufferedReader) throws IOException {
        ArrayList<Map<String, List<String>>> arrayList = new ArrayList<>();
        // 双层循环：
        // .mid一行对应
        // .mif一段数据用 "REGION 1" 间隔
        String midStr, mifStr;
        while ((midStr = midBufferedReader.readLine()) != null) {
            Map<String, List<String>> map = new HashMap<>(16);
            List<String> list = new ArrayList<>();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(midStr);
            stringBuffer.append(",");
            while ((mifStr = mifBufferedReader.readLine()) != null) {
                // 如果等于 REGION 1 跳出
                if ("REGION 1".equals(mifStr.trim())) {
                    break;
                }
                // 如果是数据就加入coordinateList
                if (2 == mifStr.split(" ").length) {
                    list.add(mifStr);
                } else {
                    stringBuffer.append(mifStr).append(",");
                }

            }
            stringBuffer.append(".txt");
            String fileName = stringBuffer.toString();
            String length = fileName.split(",")[11];
            if (Integer.parseInt(length) != (list.size())) {
                log.error("文件个数不正确:{}", fileName);
            }
            map.put(fileName, list);
            arrayList.add(map);
        }
        log.info("总共区域: {} 个", arrayList.size());
        return arrayList;
    }


    /**
     * 写入文件
     *
     * @param list Map<String, HashMap> 集合
     */
    public static void writeFile(ArrayList<Map<String, List<String>>> list) {
        try {
            String separator = "_", prefix = areaPrefix + separator;
            for (int i = 0; i < list.size(); i++) {
                Map<String, List<String>> map = list.get(i);
                for (Object entry : map.entrySet()) {
                    Map.Entry<String, List<String>> item = (Map.Entry<String, List<String>>) entry;
                    String mapKey = item.getKey();
                    List<String> mapValueList = item.getValue();
                    String[] split = mapKey.split(",");

                    String path = Constant.FILE_PATH + "test/";
                    FileUtil.createDir(path);
                    //+ split[14];
                    String fileName = path + prefix + split[2] + separator + split[1] + separator + split[11];
                    FileUtil.createFile(fileName);
                    FileUtil.writeFile(mapValueList, fileName);
                }
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}