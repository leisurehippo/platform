package io.github.jhipster.sample.web.rest.model;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.jdo.annotations.Transactional;
import java.util.List;

/**
 * Created by hubo on 2017/7/14.
 */
public interface DataLabelInfoDAO extends CrudRepository<DataLabelInfo,LabelDataSetKey> {

}
