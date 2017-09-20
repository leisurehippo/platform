package io.github.jhipster.sample.web.rest;


import io.github.jhipster.sample.web.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hubo on 2017/9/11.
 */

@Service
public class DatalabelServiceBean {

    private static final Logger logger = LoggerFactory.getLogger(DatalabelServiceBean.class);
    @Autowired
    private DataLabelInfoDAO dataLabelInfoDAO;
    @Autowired
    private LabelRelationInfoDAO labelRelationInfoDAO;


    public void update_label_set(String old_name,String new_name) throws Exception{
        Set<String> oldids = dataLabelInfoDAO.get_sinceidByTag("+"+old_name);
        Set<String> newids = dataLabelInfoDAO.get_sinceidByTag("+"+new_name);
        Set<String> old_dif_new = new HashSet<String>(oldids);
        Set<String> old_u_new = new HashSet<String>(oldids);
        old_dif_new.removeAll(newids);
        old_u_new.retainAll(newids);
        //可能存在同一id打不同标签的情况，避免主键重复，需要删除同一id出现的记录，修正不同id下的标签
        for(String id:old_dif_new) {
            dataLabelInfoDAO.update_label("+" + old_name, "+" + new_name,id);
        }
        for(String id:old_u_new){
            dataLabelInfoDAO.delete(new LabelDataSetKey(id,"+"+old_name));
        }
    }

    /**
     * 标签重命名
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class, RuntimeException.class})
    public void rename(String old_name, String new_name) throws  Exception{
        old_name=old_name.trim();
        new_name=new_name.trim();
        labelRelationInfoDAO.update_parentlabel(old_name,new_name);
        labelRelationInfoDAO.update_childlabel(old_name,new_name);
        update_label_set(old_name,new_name);

    }
    /**
     * 标签合并后，需要将label_data_set里的标签名称也替换掉
     * 这是一个事务，标签关系表和标签内容表的操作必须保持同步
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class, RuntimeException.class})
    public void update_name(String old_name, String new_name) throws Exception {
            old_name=old_name.trim();
            new_name=new_name.trim();
            String parent = labelRelationInfoDAO.findParent(old_name);
            if (parent != null) {
                labelRelationInfoDAO.delete(old_name);
                labelRelationInfoDAO.update_parentlabel(old_name, new_name);
                logger.info("----------->>");
                update_label_set(old_name,new_name);
            }


    }
}
