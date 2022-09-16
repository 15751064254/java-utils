package com.example.demo.service;

import com.zjxl.transmap.rgc.thrift.Coord;
import com.zjxl.transmap.rgc.thrift.MRGCInput;
import com.zjxl.transmap.rgc.thrift.RGCInput;
import com.zjxl.transmap.rgc.thrift.Result;
import com.zjxl.transmap.rgc.thrift.client.ProtocalServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
public class RgcThriftClient {

    private static String host = "thrift.transmap.com.cn";
    private static int port = 4000;
    private static int num = 600000;

    /**
     * 根据多个GPS点集合获取实际位置
     *
     * @param points
     * @param lonKey
     * @param latKey
     * @return
     */
    public static List<String> mrgc(List<Map<String, String>> points, String lonKey, String latKey) throws TException {

        List<Coord> coordList = new ArrayList<Coord>();
        for (Map<String, String> m : points) {
            Coord c = new Coord();
            System.out.println(m.get(lonKey) + "\t" + m.get(latKey));
            try {
                if (StringUtils.isNotEmpty(m.get(lonKey))
                        && StringUtils.isNotEmpty(m.get(latKey))) {
                    c.x = Double.parseDouble(m.get(lonKey)) / 600000;
                    c.y = Double.parseDouble(m.get(latKey)) / 600000;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("lonkey = " + m.get(lonKey) + "  latKey =" + m.get(latKey), ex);
            }
            coordList.add(c);
        }
        MRGCInput input = new MRGCInput();
        input.coords = coordList;
        input.mode = 6;
        input.k = "123";
        //long a = System.currentTimeMillis();
        ProtocalServiceClient client = ProtocalServiceClient.getInstance(host, port);
        //log.info("get connection time:" + (System.currentTimeMillis() - a));
        List<Result> rgcResult = client.mrgc(input);
        client.close();
        //log.info("mrgc total time:" + (System.currentTimeMillis() - a));
        List<String> rs = new ArrayList<String>();
        for (Result r : rgcResult) {
            String address = "";
            String name = "";
            String angle = "";
            if (null == r.pinfo) {
                if (null != r.ainfo
                        && null != r.ainfo.provincename
                        && null != r.ainfo.cityname
                        && null != r.ainfo.countryname) {
                    rs.add(r.ainfo.provincename + "," + r.ainfo.cityname + "," + r.ainfo.countryname);
                } else {
                    rs.add("--");
                }
                continue;
            }
            if (null != r.pinfo.address) {
                address = r.pinfo.address;
            }

            if (null != r.pinfo.name) {
                name = r.pinfo.name;
            }

            if (null != r.pinfo.angle) {
                angle = r.pinfo.angle;
            }
            String info = address + "," + name + "," + angle + "方向," + r.pinfo.distance + "米";
            rs.add(info);
        }
        return rs;
    }

    /**
     * 根据单个GPS点获取实际位置
     *
     * @param points
     * @param lonKey
     * @param latKey
     * @return
     */
    public static String rgc(List<Map<String, String>> points, String lonKey, String latKey) throws TException {
        Map<String, String> map = points.get(0);
        if (map.isEmpty()) {
            return null;
        }

        Coord c = new Coord();
        try {
            c.x = Double.parseDouble(map.get(lonKey)) / 600000;
            c.y = Double.parseDouble(map.get(latKey)) / 600000;
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.error("lonkey = " + map.get(lonKey) + "  latKey =" + map.get(latKey),ex);
        }
        RGCInput input = new RGCInput();
        input.coord = c;
        input.mode = 6;
        input.k = "123";
        long a = System.currentTimeMillis();
        //logger.info("start find mrgc  ip:" + Sysconf.getRGCTHRIRTIP() + " port:" + Sysconf.getRGCTHRIRTPORT());
        ProtocalServiceClient client = ProtocalServiceClient.getInstance(host, port);
        log.info("get connection time:" + (System.currentTimeMillis() - a));
        Result result = client.rgc(input);
        client.close();
        log.info("mrgc total time:" + (System.currentTimeMillis() - a));
        StringBuilder rs = new StringBuilder("");
        String address = "";
        String name = "";
        String angle = "";
        if (null == result.pinfo) {
            if (null != result.ainfo && null != result.ainfo.provincename && null != result.ainfo.cityname && null != result.ainfo.countryname) {
                rs.append(result.ainfo.provincename + "," + result.ainfo.cityname + "," + result.ainfo.countryname);
            } else {
                rs.append("--");
            }
        }
        if (null != result.pinfo) {
            if (null != result.pinfo.address) {
                address = result.pinfo.address;
            }

            if (null != result.pinfo.name) {
                name = result.pinfo.name;
            }

            if (null != result.pinfo.angle) {
                angle = result.pinfo.angle;
            }
            String info = address + "," + name + "," + angle + "方向," + result.pinfo.distance + "米";
            rs.append(info);
        }
        return rs.toString();
    }

    public static void main(String[] args) {
        try {
            List<Map<String, String>> points = new ArrayList<Map<String, String>>();

            Map<String, String> map = new HashMap<String, String>();
            map.put("lng", String.valueOf(106.174722 * num));
            map.put("lat", String.valueOf(23.903137 * num));
            points.add(map);
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put("lng", String.valueOf(106.160028 * num));
            map2.put("lat", String.valueOf(23.903530 * num));
            points.add(map2);
            List<String> mrgc = RgcThriftClient.mrgc(points, "lng", "lat");
            for (String s : mrgc) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main1(String[] args) throws TException {
        try {
            List<Coord> coordList = new ArrayList<Coord>();
            //String str="115.67853886900,38.59464318240;";
            String str = "106.174722,23.903137;";
            String[] strs = str.split(";");
            for (String s : strs) {
                Coord coord = new Coord();
                coord.x = Double.parseDouble(s.split(",")[0]);
                coord.y = Double.parseDouble(s.split(",")[1]);
                coordList.add(coord);
            }

            long a = System.currentTimeMillis();
            for (Coord c : coordList) {
                RGCInput input = new RGCInput();
                input.coord = c;
                input.mode = 6;
                input.k = "123";
                Result result = ProtocalServiceClient.getInstance(host, port).rgc(input);
                if (result.ainfo == null && result.rinfo == null && result.pinfo == null) {
                    System.out.println("no data match！！");
                } else {
                    System.out.println(result.toString());
                }
            }
            System.out.println("rgc total time:" + (System.currentTimeMillis() - a));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
