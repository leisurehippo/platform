package com.bisaibang.monojwt.web.rest.generate;

import com.fasterxml.jackson.annotation.JsonIgnore;

    import javax.persistence.*;
    import javax.validation.constraints.Size;
    import java.io.Serializable;


@Entity
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final ResponseMessage INSTANCE = new ResponseMessage();

    private ResponseMessage() {}


    public static ResponseMessage message(String message) {
        INSTANCE.setMessage(message);
        return INSTANCE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @Size(max = 50)
    @JsonIgnore
    private String flag;

    @Size(max = 50)
    private String Message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public ResponseMessage set(String Message) {
        this.Message = Message;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
            ", flag='" + flag + '\'' +
            ", Message='" + Message + '\'' +
            "}";
    }
}
