package org.phoenix.aladdin.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserAddress {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column
    private Long userId;

    @Column
    private String address;
}
