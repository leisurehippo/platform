package io.github.jhipster.sample.web.rest.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by hubo on 2017/7/14.
 */
public interface LabelRelationInfoDAO extends CrudRepository<LabelRelationInfo,String> {

    @Query(value = "select parent_label from label_relation where child_label = ?1",nativeQuery = true)
    String findParent(String child);
}
