package com.example.demo.utils;

import org.apache.commons.collections.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtil {

    /**
     * 拆分集合
     *
     * @param <T>           泛型对象
     * @param resList       需要拆分的集合
     * @param subListLength 每个子集合的元素个数
     * @return 返回拆分后的各个集合组成的列表
     * 代码里面用到了guava和common的结合工具类
     **/
    public static <T> List<List<T>> split(List<T> resList, int subListLength) {
        if (CollectionUtils.isEmpty(resList) || subListLength <= 0) {
//            return Lists.newArrayList();
            return new ArrayList<>();
        }
//        List<List<T>> ret = Lists.newArrayList();
        List<List<T>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= subListLength) {
            // 数据量不足 subListLength 指定的大小
            ret.add(resList);
        } else {
            int pre = size / subListLength;
            int last = size % subListLength;
            // 前面pre个集合，每个大小都是 subListLength 个元素
            for (int i = 0; i < pre; i++) {
                //List<T> itemList = Lists.newArrayList();
                List<T> itemList = new ArrayList<>();
                for (int j = 0; j < subListLength; j++) {
                    itemList.add(resList.get(i * subListLength + j));
                }
                ret.add(itemList);
            }
            // last的进行处理
            if (last > 0) {
//                List<T> itemList = Lists.newArrayList();
                List<T> itemList = new ArrayList<>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * subListLength + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }


    static class Test {

        // 运行代码
        public static void main1(String[] args) {
//            List<String> list = Lists.newArrayList();
            List<String> list = new ArrayList<>();
            int size = 10;
            for (int i = 0; i < size; i++) {
                list.add("hello-" + i);
            }

            List<List<String>> temps = split(list, 2);
            int j = 0;
            for (List<String> obj : temps) {
                System.out.println(String.format("row:%s -> size:%s,data:%s", ++j, obj.size(), obj));
            }
        }


        /**
         * 产品失效测试
         * @param args
         * @throws ParseException
         */
        public static void main2(String[] args) throws ParseException {
            String startStr = "2022-04-27 00:00:00";
            String endStr = "2022-04-30 00:00:00";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date start = simpleDateFormat.parse(startStr);
            Date end = simpleDateFormat.parse(endStr);

            Date now = new Date();
            if (start.compareTo(now)==1||end.compareTo(now)==-1){
                System.out.println("删除");
            }
        }


        public static void main(String[] args) throws ParseException {
            test();
        }

        private static void test() throws ParseException {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            String formatPattern = "yyyy-MM-dd HH:mm:ss";
            simpleDateFormat.applyPattern(formatPattern);

            //所以产品
            //List<RiskInfoSimple> simpleAll = riskInfoService.getReleaseRiskInfoSimpleAll();
            List<RiskInfoSimple> simpleAll = new ArrayList<>();
            List<String> arraySimpleAllList = new ArrayList<>();
            arraySimpleAllList.add("2840042");
            arraySimpleAllList.add("2840052");
            arraySimpleAllList.add("1110192");
            arraySimpleAllList.add("1110082");
            arraySimpleAllList.add("2110132");
            arraySimpleAllList.add("2840202");
            arraySimpleAllList.add("2840212");
            for (int i = 0; i < arraySimpleAllList.size(); i++) {
                String riskCode = arraySimpleAllList.get(i);
                RiskInfoSimple riskInfoSimple = new RiskInfoSimple();
                riskInfoSimple.setRiskCode(riskCode);
                simpleAll.add(riskInfoSimple);
            }

            //获取历史上架产品
            //List<AgencyGoodsDateBean> agencyGoodsDateBeansHistory = agencyGoodsDateMapper.get(agencyCode);
            List<AgencyGoodsDateBean> agencyGoodsDateBeansHistory = new ArrayList<>();
            List<String> arrayRiskCodHistoryList = new ArrayList<>();
            arrayRiskCodHistoryList.add("2840042");//历史
            arrayRiskCodHistoryList.add("2840052");//历史
            arrayRiskCodHistoryList.add("1110192");//历史
//        arrayRiskCodHistoryList.add("1110082");//历史
//        arrayRiskCodHistoryList.add("2110132");//历史
            for (int i = 0; i < arrayRiskCodHistoryList.size(); i++) {
                String riskCode = arrayRiskCodHistoryList.get(i);
                AgencyGoodsDateBean agencyGoodsDateBean = new AgencyGoodsDateBean();
                agencyGoodsDateBean.setRiskCode(riskCode);
                agencyGoodsDateBean.setAgencyCode("151004");
                agencyGoodsDateBean.setStartSaleDate(simpleDateFormat.parse("2022-09-01 00:00:00"));
                agencyGoodsDateBean.setEndSaleDate(simpleDateFormat.parse("2022-09-10 23:59:59"));
                agencyGoodsDateBeansHistory.add(agencyGoodsDateBean);
            }
            //----------------------------------------------------------------------//
            List<AgencyGoodsDateApprovalBean> list = new ArrayList<>();
            List<String> arrayRiskCodApprovalList = new ArrayList<>();
            arrayRiskCodApprovalList.add("2840042");//修改时间
            arrayRiskCodApprovalList.add("2840052");//删除
//        arrayRiskCodApprovalList.add("1110192");//未改动
//        arrayRiskCodApprovalList.add("1110082");//未改动
//        arrayRiskCodApprovalList.add("2110132");//未改动
            arrayRiskCodApprovalList.add("2840202");//新增
            arrayRiskCodApprovalList.add("2840212");//新增
            for (int i = 0; i < arrayRiskCodApprovalList.size(); i++) {
                String riskCode = arrayRiskCodApprovalList.get(i);
                AgencyGoodsDateApprovalBean agencyGoodsDateApprovalBean = new AgencyGoodsDateApprovalBean();
                agencyGoodsDateApprovalBean.setAgencyApprovalId(Long.parseLong(String.valueOf(i)));
                agencyGoodsDateApprovalBean.setRiskCode(riskCode);
                agencyGoodsDateApprovalBean.setAgencyCode("151004");

                if (riskCode.equals("2840042")) {
                    agencyGoodsDateApprovalBean.setStatus(1);
                    agencyGoodsDateApprovalBean.setApprovalStatus(1);
                    agencyGoodsDateApprovalBean.setStartSaleDate(simpleDateFormat.parse("2022-11-11 00:00:00"));
                    agencyGoodsDateApprovalBean.setEndSaleDate(simpleDateFormat.parse("2022-11-12 23:59:59"));
                } else if (riskCode.equals("2840052")) {
                    agencyGoodsDateApprovalBean.setStatus(0);
                    agencyGoodsDateApprovalBean.setApprovalStatus(0);
                    agencyGoodsDateApprovalBean.setStartSaleDate(simpleDateFormat.parse("2022-09-01 00:00:00"));
                    agencyGoodsDateApprovalBean.setEndSaleDate(simpleDateFormat.parse("2022-09-10 23:59:59"));
                } else {
                    agencyGoodsDateApprovalBean.setStatus(1);
                    agencyGoodsDateApprovalBean.setApprovalStatus(1);
                    agencyGoodsDateApprovalBean.setStartSaleDate(simpleDateFormat.parse("2022-12-01 00:00:00"));
                    agencyGoodsDateApprovalBean.setEndSaleDate(simpleDateFormat.parse("2022-12-10 23:59:59"));
                }

                list.add(agencyGoodsDateApprovalBean);
            }

            //获取审批通过所以产品
            List<AgencyGoodsDateApprovalBean> agencyGoodsDateBeansApprovalAll = new ArrayList<>();
            agencyGoodsDateBeansApprovalAll.addAll(list);

            for (RiskInfoSimple riskInfoSimple : simpleAll) {

                Iterator<AgencyGoodsDateBean> iteratorHistory = agencyGoodsDateBeansHistory.iterator();
                while (iteratorHistory.hasNext()) {
                    AgencyGoodsDateBean agencyGoodsDateBean = iteratorHistory.next();
                    if (riskInfoSimple.getRiskCode().equals(agencyGoodsDateBean.getRiskCode())) {
                        //RiskInfoSimple riskInfoSimple = new RiskInfoSimple();
                        //riskInfoSimple.setId(0L);
                        //riskInfoSimple.setSort(0);
                        riskInfoSimple.setStatus(true);
                        riskInfoSimple.setApprovalStatus(true);
                        riskInfoSimple.setRiskCode(agencyGoodsDateBean.getRiskCode());
                        //riskInfoSimple.setRiskName("");
                        riskInfoSimple.setStartSaleDate(agencyGoodsDateBean.getStartSaleDate());
                        riskInfoSimple.setEndSaleDate(agencyGoodsDateBean.getEndSaleDate());
                        iteratorHistory.remove();
                        break;
                    }
                }
                Iterator<AgencyGoodsDateApprovalBean> iteratorApproval = agencyGoodsDateBeansApprovalAll.iterator();
                while (iteratorApproval.hasNext()) {
                    AgencyGoodsDateApprovalBean agencyGoodsDateApprovalBean = iteratorApproval.next();
                    if (riskInfoSimple.getRiskCode().equals(agencyGoodsDateApprovalBean.getRiskCode())) {
                        if (agencyGoodsDateApprovalBean.getApprovalStatus() == 1) {
                            //RiskInfoSimple riskInfoSimple = new RiskInfoSimple();
                            //riskInfoSimple.setId(0L);
                            //riskInfoSimple.setSort(0);
                            riskInfoSimple.setStatus(agencyGoodsDateApprovalBean.getStatus() == 1 ? true : false);
                            riskInfoSimple.setApprovalStatus(agencyGoodsDateApprovalBean.getApprovalStatus() == 1 ? true : false);
                            riskInfoSimple.setRiskCode(agencyGoodsDateApprovalBean.getRiskCode());
                            //riskInfoSimple.setRiskName("");
                            riskInfoSimple.setStartSaleDate(agencyGoodsDateApprovalBean.getStartSaleDate());
                            riskInfoSimple.setEndSaleDate(agencyGoodsDateApprovalBean.getEndSaleDate());
                            iteratorApproval.remove();
                            break;
                        } else {
                            //System.out.println(Tools.toJson(iteratorApproval));
                            iteratorApproval.remove();
                            break;
                        }
                    }
                }
            }
            List<RiskInfoSimple> collect = simpleAll.stream().filter(o -> o.getStatus()).collect(Collectors.toList());
            collect.stream().forEach(o -> System.out.println(o.getRiskCode()));
        }


    }

    static class RiskInfoSimple {
        private Long id;
        //上下架状态(false:下架,true:上架)
        private Boolean status = false;
        //审核状态(false:审核不通过,true:审批通过)
        private Boolean approvalStatus = true;

        private Integer sort = 99999;
        private String riskCode;
        private String riskName;
        private Date startSaleDate;
        private Date endSaleDate;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Boolean getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(Boolean approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public String getRiskCode() {
            return riskCode;
        }

        public void setRiskCode(String riskCode) {
            this.riskCode = riskCode;
        }

        public String getRiskName() {
            return riskName;
        }

        public void setRiskName(String riskName) {
            this.riskName = riskName;
        }

        public Date getStartSaleDate() {
            return startSaleDate;
        }

        public void setStartSaleDate(Date startSaleDate) {
            this.startSaleDate = startSaleDate;
        }

        public Date getEndSaleDate() {
            return endSaleDate;
        }

        public void setEndSaleDate(Date endSaleDate) {
            this.endSaleDate = endSaleDate;
        }
    }

    static class AgencyGoodsDateBean {
        private String riskCode;
        private String agencyCode;
        private Date startSaleDate;
        private Date endSaleDate;

        public String getRiskCode() {
            return riskCode;
        }

        public void setRiskCode(String riskCode) {
            this.riskCode = riskCode;
        }

        public String getAgencyCode() {
            return agencyCode;
        }

        public void setAgencyCode(String agencyCode) {
            this.agencyCode = agencyCode;
        }

        public Date getStartSaleDate() {
            return startSaleDate;
        }

        public void setStartSaleDate(Date startSaleDate) {
            this.startSaleDate = startSaleDate;
        }

        public Date getEndSaleDate() {
            return endSaleDate;
        }

        public void setEndSaleDate(Date endSaleDate) {
            this.endSaleDate = endSaleDate;
        }
    }

    static class AgencyGoodsDateApprovalBean {
        private static final long serialVersionUID = 1L;
        /**
         * ID主键 db_column: ID
         */
        private Long id;
        /**
         * 状态标记(0:删除,1:正常) db_column: STS
         */
        private Integer sts;
        /**
         * 创建时间 db_column: CREATE_DT
         */
        private Date createDt;
        /**
         * 修改时间 db_column: MODIFY_DT
         */
        private Date modifyDt;
        /**
         * 备注信息 db_column: REMARK
         */
        private String remark = "";

        private Long createId;
        private String createName;
        private Long modifyId;
        private String modifyName;

        private Long agencyApprovalId;

        private String riskCode;
        private String agencyCode;
        /**
         * 上下架状态(0:下架,1:上架)
         */
        private Integer status;

        /**
         * 审核状态(0:审核不通过,1:审批通过)
         */
        private Integer approvalStatus;
        private Date startSaleDate;
        private Date endSaleDate;

        public Long getCreateId() {
            return createId;
        }

        public void setCreateId(Long createId) {
            this.createId = createId;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public Long getModifyId() {
            return modifyId;
        }

        public void setModifyId(Long modifyId) {
            this.modifyId = modifyId;
        }

        public String getModifyName() {
            return modifyName;
        }

        public void setModifyName(String modifyName) {
            this.modifyName = modifyName;
        }

        public Long getAgencyApprovalId() {
            return agencyApprovalId;
        }

        public void setAgencyApprovalId(Long agencyApprovalId) {
            this.agencyApprovalId = agencyApprovalId;
        }

        public String getRiskCode() {
            return riskCode;
        }

        public void setRiskCode(String riskCode) {
            this.riskCode = riskCode;
        }

        public String getAgencyCode() {
            return agencyCode;
        }

        public void setAgencyCode(String agencyCode) {
            this.agencyCode = agencyCode;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(Integer approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public Date getStartSaleDate() {
            return startSaleDate;
        }

        public void setStartSaleDate(Date startSaleDate) {
            this.startSaleDate = startSaleDate;
        }

        public Date getEndSaleDate() {
            return endSaleDate;
        }

        public void setEndSaleDate(Date endSaleDate) {
            this.endSaleDate = endSaleDate;
        }
    }

}
