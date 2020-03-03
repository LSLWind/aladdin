package org.phoenix.aladdin.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    private long id;

    @Column(length = 15,nullable = false)
    private String name;

    @Column(length = 15,nullable = false)
    private String password;//存储的密码是MD5值

    @Column(nullable = false)
    private long phoneNumber;

    @Column(length = 50)
    private String address;

    private Byte status;

}
