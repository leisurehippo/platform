package com.bisaibang.monojwt.web.rest.generate.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a Logback logger.
 */
public class ResetPasswordVM {

    private long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String phone;

    @Size(min = 1, max = 50)
    private String code;

    @Size(max = 50)
    private String newPassword;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordVM{" +
            "id=" + id +
            ", phone='" + phone + '\'' +
            ", code='" + code + '\'' +
            ", newPassword='" + newPassword + '\'' +
            '}';
    }
}
