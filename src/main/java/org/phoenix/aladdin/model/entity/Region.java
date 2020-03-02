package org.phoenix.aladdin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Region {

    @Id
    @Column(nullable = false)
    private int id;

    @Column(length = 10,nullable = false)
    private String province;

    @Column(length = 10,nullable = false)
    private String city;

    @Column(length = 30,nullable = false)
    private String region;

}
