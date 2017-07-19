package io.github.jhipster.sample.web.rest.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by hubo on 2017/7/13.
 */


@Entity
@Table(name="label_data_set")
public class DataLabelInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @EmbeddedId LabelDataSetKey key;
    private  String weibo_content;
    private String weibo_time;

    public DataLabelInfo(){
    }

    public DataLabelInfo(LabelDataSetKey _key,String time,String content){
        key=_key;
        weibo_content=content;
        weibo_time=time;

    }

    public LabelDataSetKey getKey(){
        return key;
    }

    public String getWeibo_content(){
        return weibo_content;
    }

    public String getWeibo_time(){
        return weibo_time;
    }

    public void setKey(LabelDataSetKey _key){
        this.key=_key;
    }

    public void setWeibo_content(String weibo_content){
        this.weibo_content=weibo_content;
    }

    public void setWeibo_time(String weibo_time){
        this.weibo_time=weibo_time;
    }

    public  String toString()
    {
        return  "since_id: "+key.getSince_id()+" tag: "+key.getTag()+" weibo_content: "+weibo_content+" weibo_time: "+weibo_time;
    }


}
