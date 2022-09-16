package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Slf4j
public class FileUtil {

    private static String filePath = "E:/iscsi-temp1/";

    public static void main(String[] args) {
        String path = filePath + "test/";
        getAllFileName(path);
    }

    /**
     * 获取某个文件夹下的所有文件
     *
     * @param path 文件夹的路径
     * @return fileNameList 存放文件名称的list
     */
    public static List getAllFileName(String path) {
        List<String> fileNameList = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList == null) {
            return fileNameList;
        }

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                //fileNameList.add(tempList[i].toString());
                fileNameList.add(tempList[i].getName());
                //System.out.println(tempList[i].toString());
                //System.out.println(tempList[i].getName());
            }
            if (tempList[i].isDirectory()) {
                getAllFileName(tempList[i].getAbsolutePath());
            }
        }
        return fileNameList;
    }

    /**
     * @return
     */
    public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> list = new ArrayList<>();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            inputStreamReader = new InputStreamReader(fileInputStream, "GB2312");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                list.add(str);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fileInputStream, inputStreamReader, bufferedReader);
            return list;
        }
    }

    public static void close(FileInputStream fileInputStream, InputStreamReader inputStreamReader, BufferedReader bufferedReader) {
        try {

            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }


            if (fileInputStream != null) {
                fileInputStream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 把list写入文件
     *
     * @param list
     * @param fileName
     * @return
     */
    public static boolean writeFile(List<String> list, String fileName) {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (int i = 0; i < list.size(); i++) {
                bufferedWriter.write(list.get(i));
                /// windows下的文本文件换行符:\r\n
                //  linux/unix下的文本文件换行符:\r
                //  Mac下的文本文件换行符:\n
                //  String line = System.getProperty("line.separator");
                //
                bufferedWriter.write("\r");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            close(fileOutputStream, outputStreamWriter, bufferedWriter);
            return true;
        }
    }

    public static void close(FileOutputStream fileOutputStream, OutputStreamWriter outputStreamWriter, BufferedWriter bufferedWriter) {
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }

            if (fileOutputStream != null) {
                fileOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建文件
     *
     * @param fileName
     * @throws IOException
     */
    public static void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if (newFile) {
                log.info("创建文件成功:{}", fileName);
            } else {
                log.error("创建文件失败");
            }
        }
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            // 创建目录
            boolean mkdirs = dir.mkdirs();
            if (mkdirs) {
                log.info("创建目录成功:{}", path);
            } else {
                log.error("创建目录失败");
            }
        }
    }
}
