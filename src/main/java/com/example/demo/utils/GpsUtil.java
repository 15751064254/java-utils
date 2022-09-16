package com.example.demo.utils;

/**
 *
 * @author Administrator
 */
public class GpsUtil {

    /**
     * 判断指定的经纬度坐标点是否落在指定的多边形区域内
     *
     * @param lon    指定点的经度
     * @param lat    指定点的纬度
     * @param points 指定多边形区域各个节点坐标
     * @return True 落在范围内 False 不在范围内
     */
    public static Boolean isPtInPoly(double lon, double lat, Point[] points) {
        int iSum, iCount, iIndex;
        double dLon1 = 0, dLon2 = 0, dLat1 = 0, dLat2 = 0, dLon = 0;

        if (points.length < 3) {
            return false;
        }

        iSum = 0;
        iCount = points.length;

        for (iIndex = 0; iIndex < iCount - 1; iIndex++) {
            if (iIndex == iCount - 1) {
                dLon1 = points[iIndex].getX();
                dLat1 = points[iIndex].getY();
                dLon2 = points[0].getX();
                dLat2 = points[0].getY();
            } else {
                dLon1 = points[iIndex].getX();
                dLat1 = points[iIndex].getY();
                dLon2 = points[iIndex + 1].getX();
                dLat2 = points[iIndex + 1].getY();
            }

            if (((lat >= dLat1) && (lat < dLat2)) || ((lat >= dLat2) && (lat < dLat1))) {
                if (Math.abs(dLat1 - dLat2) > 0) {
                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - lat)) / (dLat1 - dLat2);
                    if (dLon < lon) {
                        iSum++;
                    }

                }
            }
        }

        if ((iSum % 2) != 0)
            return true;
        return false;
    }


    /**
     * 坐标
     */
    public static class Point {

        private Double x;
        private Double y;

        public Point() {
            this.x = 0.0;
            this.y = 0.0;
        }

        public Point(double _x, double _y) {
            this.x = _x;
            this.y = _y;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }
    }

}

