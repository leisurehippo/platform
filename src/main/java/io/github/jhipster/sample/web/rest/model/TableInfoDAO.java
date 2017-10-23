package io.github.jhipster.sample.web.rest.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by hubo on 2017/7/14.
 */
//public interface  DataInfoDAO extends CrudRepository<DataInfo,String> {
//
//    @Query(value="select * from dataset where weibo_content like ?3 and weibo_time>=?1 and weibo_time<=?2 order by weibo_time limit ?3,?4",nativeQuery =true)
//    List<DataInfo> fliterByTimeAndKey(String timestart,String timeend,String keysentence,int start,int num_per_search);
//
//    @Query(value="select * from dataset where weibo_content like ?1 order by weibo_time limit ?2,?3",nativeQuery = true)
//    List<DataInfo> findByKey(String keysentence,int start,int num_per_search);
//
//    @Query(value="select * from dataset where since_id=?1 limit 1",nativeQuery = true)
//    DataInfo findBySince_id(String id);
//
//}
@Repository
public class TableInfoDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<TableInfo> findTableAndComment()
    {
        String database = getDatabaseName();
        return jdbcTemplate.query("select table_name,table_comment from information_schema.tables where table_schema = ? "
           ,new Object[]{database} , new TableInfoRowMapper());
    }

    @Transactional(readOnly = true)
    public String getDatabaseName()
    {
        return jdbcTemplate.queryForObject("select database();",String.class,new Object[]{});
    }

}


class TableInfoRowMapper implements RowMapper<TableInfo> {

    public TableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTable_name(rs.getString("table_name"));
        tableInfo.setTable_comment(rs.getString("table_comment"));
        return tableInfo;
    }
}



