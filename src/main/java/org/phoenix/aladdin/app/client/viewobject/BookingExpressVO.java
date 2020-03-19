package org.phoenix.aladdin.app.client.viewobject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 预约快件信息VO 表单信息映射
 */
@Data
@AllArgsConstructor
public class BookingExpressVO {
    public static final String FOOD="食品";
    public static final String ELECTRONIC="电子产品";
    public static final String COSMETICS="化妆品";
    public static final String CLOTH="衣物";
    public static final String FILE="文件票件";
    public static final String OTHER="其他";
    //只读map,线程安全
    public static final Map<Byte,String> TYPE_MAP=new HashMap<>();
    static {
        TYPE_MAP.put((byte)1,FOOD);
        TYPE_MAP.put((byte)2,ELECTRONIC);
        TYPE_MAP.put((byte)3,COSMETICS);
        TYPE_MAP.put((byte)4,CLOTH);
        TYPE_MAP.put((byte)5,FILE);
        TYPE_MAP.put((byte)6,OTHER);
    }
    //发送方
    private String senderName;
    private String senderProvince;
    private String senderCity;
    private String senderCountry;
    private String senderPhoneNumber;
    private String senderAddress;

    //接收方
    private String receiverName;
    private String receiverProvince;
    private String receiverCity;
    private String receiverCountry;
    private String receiverPhoneNumber;
    private String receiverAddress;

    private Byte type;//类型
    private Float kg;
    private String remark;//备注
    private String moreInfo;//补充说明
    private String beginTime;//预约时间段开始时间
    private String endTime;//预约时间段结束时间

    private String id;//订单id
}
