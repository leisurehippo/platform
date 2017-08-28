package io.github.jhipster.sample.web.rest.model;

import org.apache.spark.sql.catalyst.expressions.Sin;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by hubo on 2017/7/13.
 */


@Entity
@Table(name="sina_temp_catch")
public class SinaDataInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @EmbeddedId SinaDataKey key;
    private  String weibo_content;
    private String weibo_time;
    private String keywords;
    private String weibo_author;

    public SinaDataInfo(){
    }

    public SinaDataInfo(SinaDataKey _key, String content,String time,String keyword,String author){
        key=_key;
        weibo_content=content;
        weibo_time=time;
        keywords=keyword;
        weibo_author=author;

    }

    public SinaDataKey getKey(){
        return key;
    }

    public String getWeibo_content(){
        return weibo_content;
    }

    public String getWeibo_time(){
        return weibo_time;
    }

    public  String getKeywords(){return keywords;}

    public  String getWeibo_author(){return weibo_author;}

    public void setKey(SinaDataKey _key){
        this.key=_key;
    }

    public void setWeibo_content(String weibo_content){
        this.weibo_content=weibo_content;
    }

    public void setWeibo_time(String weibo_time){
        this.weibo_time=weibo_time;
    }

    public void setKeywords(String words){this.keywords=words;}

    public void setWeibo_author(String author){this.weibo_author=author;}

    public  String toString()
    {
        return  "since_id: "+key.getSince_id()+" create_time: "+key.getCreate_time()+" session_id:"+key.getSession_id()+" weibo_content: "+
            weibo_content+" weibo_time: "+weibo_time+" keywords:"+keywords+" weibo_author:"+weibo_author;
    }


}
