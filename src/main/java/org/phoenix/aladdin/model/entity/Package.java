package org.phoenix.aladdin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Package {
    @Id
    private Long id;

    private String senderTransportNodeId;

    private String receiverTransportNodeId;

    private String type;

    private String status;

    private int expressCount;

    private String barCode;

    private String beginTime;

    private String endTime;

}
