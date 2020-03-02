package org.phoenix.aladdin.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {

    private long phoneNumber;

    @Size(min = 1,max = 15)
    private String userName;

    @NotNull
    @Size(min = 1,max = 15)
    private String pwd;

}
