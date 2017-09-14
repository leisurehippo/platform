package io.github.jhipster.sample.web.rest.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by hubo on 2017/7/14.
 */
public interface LabelRelationInfoDAO extends CrudRepository<LabelRelationInfo,String> {

    @Query(value = "select parent_label from label_relation where child_label = ?1",nativeQuery = true)
    String findParent(String child);

    @Modifying
    @Transactional
    @Query(value="insert into label_relation values(?1,?2)",nativeQuery = true)
    void insert(String parent,String child);

    @Query(value="select child_label from label_relation where parent_label=?1",nativeQuery = true)
    List<String> findChild(String label);

    @Modifying
    @Transactional
    @Query(value="update label_relation set parent_label=?1 where child_label=?2",nativeQuery = true)
    void update_parent(String parent, String child);

    @Modifying
    @Transactional
    @Query(value="update label_relation set parent_label = ?2 where parent_label = ?1",nativeQuery  =true)
    void update_parentlabel(String oldparent,String new_parent);

    @Modifying
    @Transactional
    @Query(value="update label_relation set child_label = ?2 where child_label = ?1",nativeQuery  =true)
    void update_childlabel(String oldchild,String new_child);

    @Modifying
    @Transactional
    @Query(value ="delete from label_relation where child_label=?1" ,nativeQuery = true)
    void delete(String child);


}
