package io.github.jhipster.sample.web.rest.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hubo on 2017/7/14.
 */
//public interface DataLabelInfoDAO extends CrudRepository<DataLabelInfo,LabelDataSetKey> {
//
//    @Query(value = "select * from label_data_set where tag = ?1",nativeQuery = true)
//    List<DataLabelInfo> findByTag(String tag);
//
//    @Modifying
//    @Transactional
//    @Query(value ="update label_data_set set tag=?2 where tag=?1 and since_id=?3" ,nativeQuery = true)
//    void update_label(String old_name, String new_name,String old_id);
//
//    @Query(value="select trim(substr(tag,2,LENGTH(tag))) from label_data_set GROUP BY trim(tag)",nativeQuery = true)
//    List<String> findAllTagswithnoflag();
//
//    @Query(value = "select count(*) from label_data_set where tag =?1",nativeQuery = true)
//    long countTag(String tag);
//
//    @Query(value="select since_id from label_data_set where tag=?1",nativeQuery = true)
//    Set<String> get_sinceidByTag(String name);
//
//    @Query(value="select since_id,weibo_time,weibo_content,create_time,?2 from label_data_set where tag = ?1",nativeQuery = true)
//    List<DataLabelInfo> replaceTag(String tag, String new_tag);
//}

@Repository
public class DataLabelInfoDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableInfoDAO tableInfoDAO;

    private static  final String Label_Table_Name="label_data_set";

    @Transactional(readOnly = true)
    public List<DataLabelInfo> findByTag(String tag)
    {

        return jdbcTemplate.query("select * from "+Label_Table_Name+" where tag = ? "
            ,new Object[]{tag} , new DataLabelInfoRowMapper());
    }

    @Transactional
    public void update_label(String old_name, String new_name,String old_id)
    {
        String sql="update "+Label_Table_Name+" set tag=? where tag=? and since_id=? ";
        Object[] params=new Object[]{new_name,old_name,old_id};
        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR};
        jdbcTemplate.update(sql,params,types);
    }

    @Transactional(readOnly = true)
    public List<String> findAllTagswithnoflag()
    {

        return jdbcTemplate.queryForList("select trim(substr(tag,2,LENGTH(tag))) from "+Label_Table_Name+" GROUP BY trim(tag) "
            ,new Object[]{} , String.class);
    }

    @Transactional(readOnly = true)
    public long countTag(String tag)
    {

        return jdbcTemplate.queryForObject("select count(*) from "+Label_Table_Name+" where tag =? "
            ,new Object[]{tag} , Integer.class);
    }

    @Transactional(readOnly = true)
    public Set<String> get_sinceidByTag(String name)
    {

        return new HashSet<String>(jdbcTemplate.queryForList("select since_id from "+Label_Table_Name+" where tag=? "
            ,new Object[]{name} , String.class));
    }

    @Transactional(readOnly = true)
    public List<DataLabelInfo> replaceTag(String _tag, String new_tag)
    {

        return jdbcTemplate.query("select since_id,weibo_time,weibo_content,create_time,? as tag from "+Label_Table_Name+"  where tag = ? "
            , new Object[]{new_tag, _tag}, new DataLabelInfoRowMapper());
    }


    @Transactional
    public void create_table(String tablename)
    {
        String sql="CREATE TABLE `"+tablename+"` (\n" +
            "  `since_id` varchar(50) NOT NULL,\n" +
            "  `weibo_time` datetime DEFAULT NULL,\n" +
            "  `weibo_content` text,\n" +
            "  `create_time` datetime DEFAULT NULL,\n" +
            "  `tag` varchar(50) NOT NULL,\n" +
            "  PRIMARY KEY (`since_id`,`tag`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8 ;";
        jdbcTemplate.update(sql);
    }

    @Transactional
    public void add_table_comment(String tabelname,String comment)
    {
        String sql="alter table "+tabelname+" comment '"+comment+"'";
        jdbcTemplate.update(sql);
    }

    @Transactional
    public void insert(DataLabelInfo dataLabelInfo,String tablename)
    {
        try {
            String sql = "insert into " + tablename + " values(?,?,?,?,?)";
            Object[] params = new Object[]{dataLabelInfo.getKey().getSince_id(), dataLabelInfo.getWeibo_time(), dataLabelInfo.getWeibo_content(), dataLabelInfo.getCreate_time(), dataLabelInfo.getKey().getTag()};
            int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
            jdbcTemplate.update(sql, params, types);
        }catch (DuplicateKeyException e){
            return;
        }
    }

    @Transactional
    public void insert(List<DataLabelInfo> Infos,String tablename)
    {
            String sql = "insert into " + tablename + " values(?,?,?,?,?)";
            int[] types = new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR};
            for(int i=0;i<Infos.size();i++) {
                DataLabelInfo dataLabelInfo=Infos.get(i);
                Object[] params = new Object[]{dataLabelInfo.getKey().getSince_id(), dataLabelInfo.getWeibo_time(), dataLabelInfo.getWeibo_content(), dataLabelInfo.getCreate_time(), dataLabelInfo.getKey().getTag()};
                try {
                    jdbcTemplate.update(sql, params, types);
                }catch (DuplicateKeyException e){
                    continue;
                }
            }
    }

    @Transactional(readOnly = true)
    public boolean exists(LabelDataSetKey labelDataSetKey) {
        List<DataLabelInfo> dataLabelInfos = jdbcTemplate.query("select * from "+Label_Table_Name+" where since_id=? and tag=?",
            new Object[]{labelDataSetKey.getSince_id(),labelDataSetKey.getTag()}, new DataLabelInfoRowMapper());
        if(dataLabelInfos.size()<=0)
            return false;
        return true;
    }

    @Transactional(readOnly = true)
    public List<String> findTagbyId(String since_id)
    {

        return jdbcTemplate.queryForList("select tag from "+Label_Table_Name+" where since_id=? "
            ,new Object[]{since_id} , String.class);
    }

    @Transactional
    public void delete(LabelDataSetKey labelDataSetKey) {
        String sql="delete from "+Label_Table_Name+" where since_id=? and tag=?";
        Object[] params = new Object[]{labelDataSetKey.getSince_id(),labelDataSetKey.getTag()};
        int[] types=new int[]{Types.VARCHAR,Types.VARCHAR};
        jdbcTemplate.update(sql,params,types);

    }

    @Transactional(readOnly = true)
    public boolean exist_table(String create_name) {
        String database = tableInfoDAO.getDatabaseName();
        try {
            jdbcTemplate.queryForObject("select table_name from information_schema.tables where table_schema = ? and table_name=?",
                new Object[]{database, create_name}, String.class);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }

    @Transactional
    public void drop_table(String del_db) {
        String sql="drop table "+del_db;
        jdbcTemplate.update(sql);
    }

    @Transactional(readOnly = true)
    public List<DataLabelInfo> findAll(String look_db) {
        return jdbcTemplate.query("select * from "+look_db,new Object[]{},new DataLabelInfoRowMapper());
    }

    @Transactional(readOnly = true)
    public List<DataLabelInfo> findAllbypage(String db,int start,int page)
    {
        return jdbcTemplate.query("select * from "+db+" order by create_time desc limit ?,? ",new Object[]{start,page},new DataLabelInfoRowMapper());
    }

    @Transactional(readOnly = true)
    public int count(String db) {
        return jdbcTemplate.queryForObject("select count(*) from "+db,Integer.class);
    }
}


class DataLabelInfoRowMapper implements RowMapper<DataLabelInfo> {

    public DataLabelInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataLabelInfo dataLabelInfo = new DataLabelInfo();
        dataLabelInfo.setWeibo_time(rs.getString("weibo_time"));
        dataLabelInfo.setWeibo_content(rs.getString("weibo_content"));
        dataLabelInfo.setCreate_time(rs.getString("create_time"));
        dataLabelInfo.setKey(new LabelDataSetKey(rs.getString("since_id"),rs.getString("tag")));
        return dataLabelInfo;
    }
}

