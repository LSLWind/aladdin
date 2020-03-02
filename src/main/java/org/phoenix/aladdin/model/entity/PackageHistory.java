package org.phoenix.aladdin.model.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PackageHistory {
    @Id
    private long id;


    //可选属性optional=false,表示express不能为空。删除历史不影响快件记录
    /*     */
    @ManyToOne()
    @JoinColumn(name="express_id")//设置在packageHistory表中的关联字段(外键)
    @JSONField(serialize = false)
    private Express express;//所属快件



    private long packageId;

    private String transportNodeId;

    private String time;

    private String status;

    private long personLiableId;//负责人
}
