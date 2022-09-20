package com.example.demo.utils;

import cn.hutool.json.JSONUtil;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * CA 签名工具类
 */
public class SignUtil {

//    {
//        "businessInfo":{
//        "bn":"1663305694468210",
//                "job":"投保单签名",
//                "orgCode":"78476884-2",
//                "uuid":"20220916052134536"
//    },
//        "pdfInfo":[
//        {
//            "attContent":"JVBERi0xLjQKJeLjz9MKMiAAolJUVPRgo=",
//                "attType":1,
//                "docType":"pdf",
//                "mergeStatement":false,
//                "name":"HXDEV000000000022686_A306_0_0740",
//                "signControl":[
//            {
//                "isMerge":true,
//                    "keyword":{
//                "allowMultiLocate":true,
//                        "h":"30",
//                        "text":"声明与授权人签名：",
//                        "w":"40"
//            },
//                "role":"投保人"
//            }
//            ]
//        }
//    ],
//        "signers":[
//        {
//            "attContent":"data:image/png;base64,/9j/4AAQSkZJRBRRRQB//Z",
//                "attType":1,
//                "dn":"1699",
//                "icn":"110101199001011552",
//                "icnType":"居民身份证",
//                "isSign":true,
//                "isTakePhoto":false,
//                "name":"张锡林",
//                "role":"投保人"
//        }
//    ]
//    }
//


    /**
     * 投保声明与授权书
     */
    public static final String STATEMENT_TEMPLATE_PATH = "pdf\\02_投保声明与授权书.pdf";

    /**
     * 签名
     */
    public static final String SIGN_IMG_PATH = "pdf\\HXJDT000000000026805_S101_1_64556.gif";

    /**
     * CA签名地址
     */
    //public static final String SIGN_CA_URL = "http://10.5.32.123:18805/jdtdev/sign/merge";
    public static final String SIGN_CA_URL = "http://127.0.0.1:18805/jdtdev/sign/merge";

    public static void main(String[] args) throws Exception {

//------------------------------------------------------------------------------------------------------------------------


        String uuid = UUID.randomUUID().toString().replace("-", "");
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setUuid(uuid);
        businessInfo.setBn("11140399002200433888");
        businessInfo.setOrgCode("9020101");
        businessInfo.setJob("电子投保单合成签名");
        businessInfo.setStatement(new Statement());

        List<Signers> signersArrayList = new ArrayList<>();
        List<SignControl> signControlArrayList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Signers sign = new Signers();
            sign.setDn(String.valueOf(new Random().nextInt(10000)));
            sign.setRole("投保人");//签名类型 投保人 被保人
            sign.setName("曹玉飞");
            sign.setBirthday("1990-07-06");
            sign.setGender(0);
            sign.setIcnType("居民身份证");
            sign.setIcn("110101199807078764");
            sign.setPhoneNumber("13800138601");
            sign.setAttType(1);//0 文件路径、1文件流
//            sign.setAttContent("data:image/png;base64,");
            sign.setAttContent("data:image/png;base64," + encodeBase64File(SIGN_IMG_PATH));
            sign.setIsSign(true);
            sign.setIsTakePhoto(false);
            sign.setPhotoInfo(new ArrayList<PhotoInfo>());
            signersArrayList.add(sign);

            SignControl signControl = new SignControl();
            signControl.setRole(sign.getRole());
            signControl.setIsMerge(true);

            Keyword keyword = new Keyword();
            keyword.setW("40");
            keyword.setH("30");
            keyword.setText("声明与授权人签名：");
            keyword.setAllowMultiLocate(true);
            signControl.setKeyword(keyword);

            //PosInfo posInfo = new PosInfo();
            //posInfo.setPage(0);
            //posInfo.setX(0);
            //posInfo.setY(0);
            //posInfo.setW(0);
            //posInfo.setH(0);
            //signControl.setPosInfo(posInfo);
            signControlArrayList.add(signControl);
        }


        PdfInfo pdfInfo = new PdfInfo();
        pdfInfo.setName("PDF名称");
        pdfInfo.setMergeStatement(false);
        pdfInfo.setDocType("pdf");
        pdfInfo.setAttType(1);//0 文件路径、1文件流
//        pdfInfo.setAttContent("JVBERi0xLjcNJeLjz9MNCjQxIDAgb2JqDTw8L0xpbmVhcml6ZWQgMS9MIDE2Nzk4Ni9PID");
        pdfInfo.setAttContent(encodeBase64File(STATEMENT_TEMPLATE_PATH));
        pdfInfo.setSignControl(signControlArrayList);
        //pdfInfo.setDateInfo(new DateInfo());

        Reserved reserved = new Reserved();
        reserved.setKeyname("");
        reserved.setKeyvalue("");



        JsonRootBean jsonRootBean = new JsonRootBean();
        jsonRootBean.setSigners(signersArrayList);
        List<PdfInfo> pdfInfoArrayList = new ArrayList<>();
        pdfInfoArrayList.add(pdfInfo);
        jsonRootBean.setPdfInfo(pdfInfoArrayList);
        jsonRootBean.setBusinessInfo(businessInfo);
        List<Reserved> reservedArrayList = new ArrayList<>();
        reservedArrayList.add(reserved);
        jsonRootBean.setReserved(reservedArrayList);
//------------------------------------------------------------------------------------------------------------------------

        System.out.println(JSONUtil.toJsonStr(jsonRootBean));
        System.out.println();

        Map<String, Object> map = new HashMap<>();
        map.put("reqObject", jsonRootBean);
        map.put("outPath", "C:\\work\\hexiehealth\\demo\\pdf\\CA投保声明与授权书.pdf");
        String jsonStr = JSONUtil.toJsonStr(map);


        String responseStr = OkHttpClientUtil.getInstance().postJson(SIGN_CA_URL, jsonStr);
        System.out.println(responseStr);

////        logger.info("调用签名平台 请求:ip:[{}], port:[{}], timeOutInMilliSeconds:[{}]", signIp, port, timeOutInMilliSeconds);
//        long actionBeginMs = System.currentTimeMillis();
//        try {
//            String outPath = "C:\\work\\hexiehealth\\demo\\pdf\\CA投保声明与授权书.pdf";
//            byte[] sb = JSONUtil.toJsonStr(jsonRootBean).getBytes();
//            IDGClient client = new IDGClient(signIp, port, timeOutInMilliSeconds);
//            IDGWSServiceStub.SubmitJobByAttatchResponse submitJobByAttatchResponse = client.submitJob(sb, "", "", "", "", new String[]{"", ""});
//            InputStream[] attachments = submitJobByAttatchResponse.getAttachments();
//            for (InputStream stream : attachments) {
//                submitJobByAttatchResponse.saveAttachment(stream, outPath);
//            }
////            return true;
//        } catch (Exception e) {
////            logger.error("indigo合并签名完成失败:{}", e.getMessage());
//            e.printStackTrace();
////            return false;
//        } finally {
////            logger.info("调用签名平台 调用耗时:[{}]", (System.currentTimeMillis() - actionBeginMs) / 1000.0);
//        }

    }

    /**
     * 文件 base64 字符串
     * @param path
     * @return
     * @throws IOException
     */
    public static String encodeBase64File(String path) throws IOException {
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        fileInputStream.read(buffer);
        fileInputStream.close();
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String encodeStr = base64Encoder.encode(buffer);
        return encodeStr;
    }

    public static class JsonRootBean {

        /**
         * 签名人，可以为多个
         */
        private List<Signers> signers;

        /**
         * PDF文档，可以为多个
         */
        private List<PdfInfo> pdfInfo;

        /**
         * 业务信息字段，唯一
         */
        private BusinessInfo businessInfo;

        /**
         * 保留字段
         */
        private List<Reserved> reserved;

        public void setSigners(List<Signers> signers) {
            this.signers = signers;
        }

        public List<Signers> getSigners() {
            return signers;
        }

        public void setPdfInfo(List<PdfInfo> pdfInfo) {
            this.pdfInfo = pdfInfo;
        }

        public List<PdfInfo> getPdfInfo() {
            return pdfInfo;
        }

        public void setBusinessInfo(BusinessInfo businessInfo) {
            this.businessInfo = businessInfo;
        }

        public BusinessInfo getBusinessInfo() {
            return businessInfo;
        }

        public void setReserved(List<Reserved> reserved) {
            this.reserved = reserved;
        }

        public List<Reserved> getReserved() {
            return reserved;
        }

    }

    /**
     * 拍照信息，可以为多个
     */
    public static class PhotoInfo {

        /**
         * 附件类型定义 必须（photoInfo(Array)>0） 0:文件路径，1:数据流
         */
        private int attType;

        /**
         * 拍照内容 必须（photoInfo(Array)>0） 文件路径或者Base64编码的图像流
         */
        private String attContent;


        /**
         * 拍照类型描述 不需要
         */
        private String photoType;

        /**
         * 归档打包名称 不需要
         */
        private String archiveName;

        /**
         * 显示名称 可选（String labeltxt = "影像：" + desc;）
         */
        private String displayName;


        public void setPhotoType(String photoType) {
            this.photoType = photoType;
        }

        public String getPhotoType() {
            return photoType;
        }

        public void setArchiveName(String archiveName) {
            this.archiveName = archiveName;
        }

        public String getArchiveName() {
            return archiveName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setAttType(int attType) {
            this.attType = attType;
        }

        public int getAttType() {
            return attType;
        }

        public void setAttContent(String attContent) {
            this.attContent = attContent;
        }

        public String getAttContent() {
            return attContent;
        }

    }

    /**
     * 签名人，可以为多个
     */
    public static class Signers {

        /**
         * 角色名称 必须 tb代表投保人 bb代表被保人
         */
        private String role;

        /**
         * 附件类型定义 必须 0:文件路径，1:数据流
         */
        private int attType;

        /**
         * 签字内容 必须 文件路径或者Base64编码的图像流
         */
        private String attContent;

        /**
         * 是否拍照 必须
         */
        private Boolean isTakePhoto;

        /**
         * 是否签字 不需要
         */
        private Boolean isSign;

        /**
         * 移动设备号，非必填
         */
        private String dn;

        /**
         * 姓名 不需要
         */
        private String name;

        /**
         * 出生日期 不需要
         */
        private String birthday;

        /**
         * 性别 不需要
         */
        private int gender;

        /**
         * 证件类型描述 不需要
         */
        private String icnType;

        /**
         * 证件号码 不需要
         */
        private String icn;

        /**
         * 联系电话 不需要
         */
        private String phoneNumber;

        /**
         * 拍照信息，可以为多个 可选
         */
        private List<PhotoInfo> photoInfo;


        public void setDn(String dn) {
            this.dn = dn;
        }

        public String getDn() {
            return dn;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getGender() {
            return gender;
        }

        public void setIcnType(String icnType) {
            this.icnType = icnType;
        }

        public String getIcnType() {
            return icnType;
        }

        public void setIcn(String icn) {
            this.icn = icn;
        }

        public String getIcn() {
            return icn;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }

        public void setIsSign(Boolean isSign) {
            this.isSign = isSign;
        }

        public Boolean getIsSign() {
            return isSign;
        }

        public void setIsTakePhoto(Boolean isTakePhoto) {
            this.isTakePhoto = isTakePhoto;
        }

        public Boolean getIsTakePhoto() {
            return isTakePhoto;
        }

        public void setPhotoInfo(List<PhotoInfo> photoInfo) {
            this.photoInfo = photoInfo;
        }

        public List<PhotoInfo> getPhotoInfo() {
            return photoInfo;
        }

        public void setAttType(int attType) {
            this.attType = attType;
        }

        public int getAttType() {
            return attType;
        }

        public void setAttContent(String attContent) {
            this.attContent = attContent;
        }

        public String getAttContent() {
            return attContent;
        }

    }

    /**
     * 位置信息
     */
    public static class PosInfo {

        /**
         * 页码 必须(mergeStatement=true && statement!=null  && isMerge=true)
         */
        private int page;

        /**
         * 水平位置 必须(mergeStatement=true && statement!=null  && isMerge=true)
         */
        private float x;

        /**
         * 垂直位置 必须(mergeStatement=true && statement!=null  && isMerge=true)
         */
        private float y;

        /**
         * 所占宽度 必须(mergeStatement=true && statement!=null  && isMerge=true)
         */
        private float w;

        /**
         * 所占高度 必须(mergeStatement=true && statement!=null  && isMerge=true)
         */
        private float h;

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage() {
            return page;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getW() {
            return w;
        }

        public void setW(float w) {
            this.w = w;
        }

        public float getH() {
            return h;
        }

        public void setH(float h) {
            this.h = h;
        }
    }


    /**
     * 签名日期 可选
     */
    public static class DateInfo {

        /**
         * 日期字符串 必须(dateInfo != null)
         */
        private String format;

        /**
         * 位置信息 必须(dateInfo != null)
         */
        private PosInfo posInfo;

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public void setPosInfo(PosInfo posInfo) {
            this.posInfo = posInfo;
        }

        public PosInfo getPosInfo() {
            return posInfo;
        }

    }


    /**
     * 签名要求，可以为多个
     */
    public static class SignControl {

        /**
         * 角色名称 必须
         */
        private String role;

        /**
         * 是否合并到文档 必须
         */
        private Boolean isMerge;

        /**
         * 位置信息
         */
        private PosInfo posInfo;

        /**
         * 位置信息
         */
        private Keyword keyword;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Boolean getIsMerge() {
            return isMerge;
        }

        public void setIsMerge(Boolean merge) {
            isMerge = merge;
        }

        public PosInfo getPosInfo() {
            return posInfo;
        }

        public void setPosInfo(PosInfo posInfo) {
            this.posInfo = posInfo;
        }

        public Keyword getKeyword() {
            return keyword;
        }

        public void setKeyword(Keyword keyword) {
            this.keyword = keyword;
        }
    }

    /**
     * 文字位置信息
     */
    public static class Keyword {

        /**
         * 所占宽度
         */
        private String w;

        /**
         * 所占高度
         */
        private String h;

        /**
         * 文字内容
         */
        private String text;

        /**
         * 允许多定位
         */
        private Boolean allowMultiLocate = true;

        public String getW() {
            return w;
        }

        public void setW(String w) {
            this.w = w;
        }

        public String getH() {
            return h;
        }

        public void setH(String h) {
            this.h = h;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Boolean isAllowMultiLocate() {
            return allowMultiLocate;
        }

        public void setAllowMultiLocate(Boolean allowMultiLocate) {
            this.allowMultiLocate = allowMultiLocate;
        }
    }


    /**
     * PDF文档，可以为多个
     */
    public static class PdfInfo {

        /**
         * 文档名称 必须
         */
        private String name;

        /**
         * 是否合并声明 必须
         */
        private Boolean mergeStatement;


        /**
         * 单证类型描述 不需要
         */
        private String docType;

        /**
         * 签名日期 可选
         */
        private DateInfo dateInfo;

        /**
         * 附件类型 必须 0:文件路径，1:数据流
         */
        private int attType;

        /**
         * PDF附件内容 必须 文件路径或者Base64编码的图像流
         */
        private String attContent;

        /**
         * 签名要求，可以为多个
         */
        private List<SignControl> signControl;


//        /**
//         * 未知
//         */
//        private String type;
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getType() {
//            return type;
//        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setMergeStatement(Boolean mergeStatement) {
            this.mergeStatement = mergeStatement;
        }

        public Boolean getMergeStatement() {
            return mergeStatement;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public String getDocType() {
            return docType;
        }

        public void setDateInfo(DateInfo dateInfo) {
            this.dateInfo = dateInfo;
        }

        public DateInfo getDateInfo() {
            return dateInfo;
        }

        public void setSignControl(List<SignControl> signControl) {
            this.signControl = signControl;
        }

        public List<SignControl> getSignControl() {
            return signControl;
        }

        public void setAttType(int attType) {
            this.attType = attType;
        }

        public int getAttType() {
            return attType;
        }

        public void setAttContent(String attContent) {
            this.attContent = attContent;
        }

        public String getAttContent() {
            return attContent;
        }

    }


    /**
     * 声明图片
     */
    public static class Image {

        /**
         * 文字序号 不需要
         */
        private int sequence;

        /**
         * 附件类型  必须(mergeStatement=true &&statement !=null ) 0:文件路径，1:数据流
         */
        private int attType;

        /**
         * 附件内容 必须(mergeStatement=true &&statement !=null ) 文件路径或者Base64编码的图像流
         */
        private String attContent;

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public int getSequence() {
            return sequence;
        }

        public void setAttType(int attType) {
            this.attType = attType;
        }

        public int getAttType() {
            return attType;
        }

        public void setAttContent(String attContent) {
            this.attContent = attContent;
        }

        public String getAttContent() {
            return attContent;
        }

    }


    /**
     * 声明信息
     */
    public static class Statement {

        /**
         * 位置信息
         */
        private PosInfo posInfo;

        /**
         * 声明图像
         */
        private Image image;

        public void setPosInfo(PosInfo posInfo) {
            this.posInfo = posInfo;
        }

        public PosInfo getPosInfo() {
            return posInfo;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Image getImage() {
            return image;
        }

    }


    /**
     * 业务信息字段，唯一
     */
    public static class BusinessInfo {

        /**
         * 流水号 必须
         */
        private String uuid;

        /**
         * 业务编码 必须
         */
        private String bn;

        /**
         * 机构编码 必须
         */
        private String orgCode;

        /**
         * 任务名称 不需要
         */
        private String job;

        /**
         * 声明信息 可选（和mergeStatement=true使用）
         */
        private Statement statement;

        public void setBn(String bn) {
            this.bn = bn;
        }

        public String getBn() {
            return bn;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getUuid() {
            return uuid;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getJob() {
            return job;
        }

        public void setStatement(Statement statement) {
            this.statement = statement;
        }

        public Statement getStatement() {
            return statement;
        }

    }


    /**
     * 保留字段
     */
    public static class Reserved {

        /**
         * 扩展参数名 不需要
         */
        private String keyname;

        /**
         * 扩展参数值 不需要
         */
        private String keyvalue;

        public void setKeyname(String keyname) {
            this.keyname = keyname;
        }

        public String getKeyname() {
            return keyname;
        }

        public void setKeyvalue(String keyvalue) {
            this.keyvalue = keyvalue;
        }

        public String getKeyvalue() {
            return keyvalue;
        }

    }
}
