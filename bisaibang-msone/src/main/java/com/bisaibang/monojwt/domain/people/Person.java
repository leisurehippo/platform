package com.bisaibang.monojwt.domain.people;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "theme_url")
    private String themeUrl;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "type")
    private String type;

    @Column(name = "sex")
    private String sex;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "id_card")
    private String idCard;

    @Column(name = "id_game")
    private String idGame;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Person name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Person description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThemeUrl() {
        return themeUrl;
    }

    public Person themeUrl(String themeUrl) {
        this.themeUrl = themeUrl;
        return this;
    }

    public void setThemeUrl(String themeUrl) {
        this.themeUrl = themeUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Person avatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public Person type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public Person sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public Person phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public Person email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCard() {
        return idCard;
    }

    public Person idCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdGame() {
        return idGame;
    }

    public Person idGame(String idGame) {
        this.idGame = idGame;
        return this;
    }

    public void setIdGame(String idGame) {
        this.idGame = idGame;
    }

    public User getUser() {
        return user;
    }

    public Person user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        if (person.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", themeUrl='" + themeUrl + "'" +
            ", avatarUrl='" + avatarUrl + "'" +
            ", type='" + type + "'" +
            ", sex='" + sex + "'" +
            ", phone='" + phone + "'" +
            ", email='" + email + "'" +
            ", idCard='" + idCard + "'" +
            ", idGame='" + idGame + "'" +
            '}';
    }
}
