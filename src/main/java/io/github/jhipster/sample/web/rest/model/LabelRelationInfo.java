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
@Table(name="label_relation")
public class LabelRelationInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private  LabelRelationKey key;

    public LabelRelationInfo(LabelRelationKey _key)
    {
        key=_key;
    }

    public LabelRelationInfo(){}

    public LabelRelationKey getLabelRelationKey() {
        return key;
    }

//    public LabelRelationKey getLabelRelationKey(){ return key;}




}
