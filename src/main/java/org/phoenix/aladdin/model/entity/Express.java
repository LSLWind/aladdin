package org.phoenix.aladdin.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import javafx.beans.DefaultProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Express {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    /**
     * 级联保存、更新、删除、刷新;延迟加载。当删除快件，会级联删除该快件的所有历史
     * 拥有mappedBy注解的实体类为关系被维护端
     * mappedBy="express"中的express是PackageHistory中的express属性
     */
    @OneToMany(mappedBy = "express",cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    @OrderBy("time")
    @JSONField(serialize = false)
    private List<PackageHistory> packageHistoryList;

    private long senderId;

    private String senderProvince;

    private String senderCity;

    private String senderCountry;

    private String senderAddress;

    private String senderPhoneNumber;

    private String senderTransportNode;

    private Long receiverId;

    private String receiverProvince;

    private String receiverCity;

    private String receiverCountry;

    private String receiverAddress;

    private String receiverPhoneNumber;

    private String receiverTransportNode;

    private String beginTime="";

    private String endTime="";

    private String barCode="";

    private String type="";

    private Float weight=(float)-1;

    private Float cost=(float)-1;

    private String status;

    private String remark="";

    private String moreInfo="";
}
