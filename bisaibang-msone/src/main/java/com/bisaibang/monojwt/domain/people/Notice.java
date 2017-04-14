package com.bisaibang.monojwt.domain.people;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Notice.
 */
@Entity
@Table(name = "notice")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "type")
    private String type;

    @Column(name = "state")
    private String state;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Notice title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Notice content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public Notice type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public Notice state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean isIsRead() {
        return isRead;
    }

    public Notice isRead(Boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public Notice createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public Notice user(User user) {
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
        Notice notice = (Notice) o;
        if (notice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notice{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", content='" + content + "'" +
            ", type='" + type + "'" +
            ", state='" + state + "'" +
            ", isRead='" + isRead + "'" +
            ", createDate='" + createDate + "'" +
            '}';
    }
}
