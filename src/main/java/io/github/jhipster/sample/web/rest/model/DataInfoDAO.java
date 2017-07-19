package io.github.jhipster.sample.web.rest.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hubo on 2017/7/14.
 */
public interface  DataInfoDAO extends CrudRepository<DataInfo,String> {

    @Query(value="select * from dataset where weibo_content like ?3 and weibo_time>=?1 and weibo_time<=?2  ",nativeQuery =true)
    List<DataInfo> fliterByTimeAndKey(String timestart,String timeend,String keysentence);

    @Query(value="select * from dataset where weibo_content like ?1 ",nativeQuery =true)
    List<DataInfo> findByKey(String keysentence);


}
