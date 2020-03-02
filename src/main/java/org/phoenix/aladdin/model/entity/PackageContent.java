package org.phoenix.aladdin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PackageContent {

    @Id
    private long id;

    private long expressId;

    private long packageId;
}
