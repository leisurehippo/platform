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
@Table(name="relation_label")
public class LabelRelationInfo implements Serializable{

    private static final long serialVersionUID = 1L;


    private String parent_label;
    @Id
    private String child_label;

    public LabelRelationInfo(String parent, String child)
    {
        parent_label=parent;
        child_label=child;
    }

    public LabelRelationInfo(){}

    public  String getParent_label(){return parent_label;}

    public String getChild_label(){return child_label;}

//    public LabelRelationKey getLabelRelationKey(){ return key;}




}
