package io.github.jhipster.sample.web.rest.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by hubo on 2017/7/14.
 */
public interface DataLabelInfoDAO extends CrudRepository<DataLabelInfo,LabelDataSetKey> {

    @Query(value = "select * from label_data_set where tag = ?1",nativeQuery = true)
    List<DataLabelInfo> findByTag(String tag);

    @Modifying
    @Transactional
    @Query(value ="update label_data_set set tag=?2 where tag=?1 and since_id=?3" ,nativeQuery = true)
    void update_label(String old_name, String new_name,String old_id);

    @Query(value="select trim(substr(tag,2,LENGTH(tag))) from label_data_set GROUP BY trim(tag)",nativeQuery = true)
    List<String> findAllTagswithnoflag();

    @Query(value = "select count(*) from label_data_set where tag =?1",nativeQuery = true)
    long countTag(String tag);

    @Query(value="select since_id from label_data_set where tag=?1",nativeQuery = true)
    Set<String> get_sinceidByTag(String name);
}
