package org.phoenix.aladdin.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpressLocationVO {
    private String time;
    private String transportNode;
    private String status;
    private String liablePerson;//负责人
}
