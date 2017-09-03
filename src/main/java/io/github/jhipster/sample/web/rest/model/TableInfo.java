package io.github.jhipster.sample.web.rest.model;

/**
 * Created by hubo on 2017/7/13.
 */


public class TableInfo {
    private String table_name;
    private String table_comment;

    public TableInfo(){}

    public  TableInfo(String name,String comment)
    {
        table_comment=comment;
        table_name= name;
    }

    public String getTable_name(){
        return table_name;
    }

    public String getTable_comment(){
        return table_comment;
    }

    public void setTable_name(String name){
        table_name = name;
    }

    public void setTable_comment(String comment){
        table_comment=comment;
    }

}
