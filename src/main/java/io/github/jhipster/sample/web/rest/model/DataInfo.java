package io.github.jhipster.sample.web.rest.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by hubo on 2017/7/13.
 */

@Entity
@Table(name="dataset")
public class DataInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id private String since_id;
    private  String weibo_content;
    private String weibo_time;

    public String getSince_id(){
        return since_id;
    }


    public String getWeibo_content(){
        return weibo_content;
    }

    public String getWeibo_time(){
        return weibo_time;
    }

    public void setSince_id(String since_id){
        this.since_id=since_id;
    }

    public void setWeibo_content(String weibo_content){
        this.weibo_content=weibo_content;
    }

    public void setWeibo_time(String weibo_time){
        this.weibo_time=weibo_time;
    }


}
