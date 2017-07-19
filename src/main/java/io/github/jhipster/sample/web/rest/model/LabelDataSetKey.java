package io.github.jhipster.sample.web.rest.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by hubo on 2017/7/18.
 */
@Embeddable
public class LabelDataSetKey implements Serializable {
    private String since_id;
    private String tag;
    public LabelDataSetKey(String id,String _tag)
    {
        since_id=id;
        tag=_tag;
    }

    public LabelDataSetKey(){}

    public String getSince_id(){
        return since_id;
    }

    public String getTag(){
        return tag;
    }

    public void setSince_id(String id){
        since_id=id;
    }

    public void  setTag(String t){
        tag=t;
    }

}
