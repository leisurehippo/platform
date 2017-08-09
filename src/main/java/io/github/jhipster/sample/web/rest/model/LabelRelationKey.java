package io.github.jhipster.sample.web.rest.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by hubo on 2017/7/18.
 */
@Embeddable
public class LabelRelationKey implements Serializable {
    private String parent_label;
    private String child_label;
    public LabelRelationKey(String parent, String child)
    {
        parent_label=parent;
        child_label=child;
    }

    public LabelRelationKey(){}

    public  String getParent_label(){return parent_label;}

    public String getChild_label(){return child_label;}

}
