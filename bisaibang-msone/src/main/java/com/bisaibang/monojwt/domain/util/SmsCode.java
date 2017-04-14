package com.bisaibang.monojwt.domain.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Created by szzz on 2016/7/6.
 * bsb v2
 */

@Entity
@Table(name = "sms_code")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmsCode  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "phone", length = 100, unique = true)
    private String phone;

    @Size(max = 20)
    @Column(name = "code", length = 20)
    private String code;

    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    private ZonedDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "SmsCode{" +
            "phone='" + phone + '\'' +
            ", code='" + code + '\'' +
            "}";
    }
}
