package io.github.jhipster.sample.web.rest.model;

import javax.persistence.Embeddable;
import java.io.Serializable;


/**
 * Created by hubo on 2017/8/10.
 */

@Embeddable
public class SinaDataKey implements Serializable {
    private String session_id;
    private String create_time;
    private String since_id;
    public SinaDataKey(){}

    public  SinaDataKey(String session,String time,String since)
    {
        session_id=session;
        create_time=time;
        since_id=since;
    }

    public String getSession_id()
    {
        return session_id;
    }

    public  String getCreate_time()
    {
        return create_time;
    }

    public String getSince_id()
    {
        return since_id;
    }

}
