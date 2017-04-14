package com.bisaibang.monojwt.service.dto;

import com.bisaibang.monojwt.config.Constants;
import com.bisaibang.monojwt.domain.generate.Authority;
import com.bisaibang.monojwt.domain.people.User;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(min = 1, max = 50)
    private String nickName;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(min = 11, max = 11)
    private String phone;

    private boolean activated = false;

    @Size(max = 20)
    private String confirmCode;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<String> authorities;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getLogin(), user.getFirstName(), user.getLastName(),user.getNickName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()));
    }

    public UserDTO(String login, String firstName, String lastName, String nickName,
                   String email, boolean activated, String langKey, Set<String> authorities) {

        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
    }

    public String getNickName() {
        return nickName;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", nickName='" + nickName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", activated=" + activated +
                ", confirmCode='" + confirmCode + '\'' +
                ", langKey='" + langKey + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
