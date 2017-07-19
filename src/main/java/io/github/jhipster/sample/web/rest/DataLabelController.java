package io.github.jhipster.sample.web.rest;

/**
 * Created by HB on 2017/7/10.
 */

import com.google.gson.Gson;
import io.github.jhipster.sample.web.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ResponseEntity{
    public String since_id;
    public String weibo_content;
    public ResponseEntity(){}
    public ResponseEntity(String id, String content){
        since_id=id;
        weibo_content=content;
    }
}

class DataLabelResponse {

    public List<ResponseEntity> dataset;
    public DataLabelResponse(){
       dataset = new ArrayList<ResponseEntity>();
    }
}

//class DataRecord{
//    private String since_id,content;
//    public DataRecord(String _since_id,String _content){
//        since_id=_since_id;
//        content=_content;
//    }
//}

/**
 * 数据标注交互器
 */
@RestController
@RequestMapping("/api")


public class DataLabelController {

    private static final Logger logger = LoggerFactory.getLogger(DataLabelController.class);
    @Autowired
    private DataInfoDAO dataInfoDAO;
    @Autowired
    private DataLabelInfoDAO dataLabelInfoDAO;
    Map<String,DataInfo> datamap = new HashMap<String,DataInfo>();
    String _key;
    @GetMapping("/dataLabelservice")
    @ResponseBody
    public  String dataSelect(@RequestParam("keywords") String keywords, @RequestParam("selectdb") boolean selectdb,@RequestParam("dbname") String dbname,
                               @RequestParam("selectsina") boolean selectsina,@RequestParam("selecttime") boolean selecttime,@RequestParam("timestart") String timestart,
                               @RequestParam("timeend") String timeend,@RequestParam("selectoldlabel") boolean selectoldlabel,@RequestParam("oldlabel") String oldlabel,
                               @RequestParam("newlabel") String newlabel,@RequestParam("writedb") String writedb) {
        logger.info(keywords+'\n'+selectdb+'\n'+dbname+'\n'+selectsina+'\n'+selecttime+'\n'+timestart+'\n'+timeend+'\n'+selectoldlabel+'\n'+oldlabel+'\n'+newlabel+'\n'+writedb+'\n');
        DataLabelResponse dataLabelResponse = new DataLabelResponse();
        String [] keylist=keywords.split("\\+");
        String dbquery="%";
        String sinaquery=" ";
        _key=newlabel;
        for(int i=0;i<keylist.length;i++)
        {
            String keyword = keylist[i].trim();
            dbquery += keyword+"%";
            sinaquery+=keyword+" ";
        }
        logger.info(dbquery);
        if(selectdb)
        {
            List<DataInfo>  dbdata = search_db(dbquery,selecttime,timestart,timeend);
            logger.info("-------->>>"+dbdata.size());
            for(int i=0;i<dbdata.size();i++){
                logger.info("-------->>>"+dbdata.get(i).getWeibo_content());
               dataLabelResponse.dataset.add(new ResponseEntity(dbdata.get(i).getSince_id(),dbdata.get(i).getWeibo_content()));
               datamap.put(dbdata.get(i).getSince_id(),dbdata.get(i));
            }

        }
        if(selectsina)
        {
            List<DataInfo> sinadata=search_sina(sinaquery,selecttime,timestart,timeend);
        }

        Gson gson = new Gson();
        return gson.toJson(dataLabelResponse);
        }

    @PostMapping("/Submitservice")
    public void Getsubmit(@RequestBody Map<String,List<String>> postdata){
        List<String> labelresult=postdata.get("labelresult");
        for(int i=0;i<labelresult.size();i++)
        {
            DataInfo _data = datamap.get(labelresult.get(i));
            dataLabelInfoDAO.save(new DataLabelInfo(new LabelDataSetKey(labelresult.get(i),_key),_data.getWeibo_time(),_data.getWeibo_content()));
            logger.info(_data.getWeibo_content()+"----------"+_key);
        }

    }



    public List<DataInfo> search_sina(String sinaquery, boolean selecttime, String timestart, String timeend) {//从新浪爬取微博
        List<DataInfo> records =null;
        return records;
    }

    public List<DataInfo> search_db(String dbquery, boolean selecttime, String timestart, String timeend) {//从数据库中查询关键词并返回结果
        List<DataInfo> records =null;
        if(selecttime)
        {

            records=dataInfoDAO.fliterByTimeAndKey(timestart,timeend,dbquery);

        }
        else{
            records=dataInfoDAO.findByKey(dbquery);
            //records=(List<DataInfo>)dataInfoDAO.findAll();
        }

        return records;
    }
}


