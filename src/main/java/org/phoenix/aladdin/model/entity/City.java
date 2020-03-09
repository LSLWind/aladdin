package org.phoenix.aladdin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class City {
    @Id
    private int id;

    private String name;

    private String cityId;

    private String provinceId;


}
