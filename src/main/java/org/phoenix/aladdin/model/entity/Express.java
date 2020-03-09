package org.phoenix.aladdin.model.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.phoenix.aladdin.model.view.BookingExpressVO;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Express {
    @Id
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

    private int senderProvinceId;

    private int senderCityId;

    private int senderCountryId;

    private String senderAddress;

    private long senderPhoneNumber;

    private String senderTransportNode;

    private long receiverId;

    private int receiverProvinceId;

    private int receiverCityId;

    private int receiverCountryId;

    private String receiverAddress;

    private long receiverPhoneNumber;

    private String receiverTransportNode;

    private String beginTime;

    private String endTime;

    private String barCode;

    private String type;

    private Float weight;

    private Float cost;

    private String status;

    private String remark;

    private String moreInfo;

}
