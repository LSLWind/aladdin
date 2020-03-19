package org.phoenix.aladdin.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserFeedback {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Long userId;

    @Column
    private String feedback;
}
