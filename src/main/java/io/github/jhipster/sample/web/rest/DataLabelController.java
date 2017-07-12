package io.github.jhipster.sample.web.rest;

/**
 * Created by HB on 2017/7/10.
 */

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DataLabelResponse {

    public List<Map<String,String>> keyset;
    public DataLabelResponse(){
       keyset = new ArrayList<Map<String,String>>();

    }
}

class DataRecord{
    private String since_id,content;
    public DataRecord(String _since_id,String _content){
        since_id=_since_id;
        content=_content;
    }
}

/**
 * 数据标注交互器
 */
@RestController
@RequestMapping("/api")


public class DataLabelController {

    private static final Logger logger = LoggerFactory.getLogger(DataLabelController.class);
    @GetMapping("/dataLabelservice")
    @ResponseBody
    private  String dataSelect(@RequestParam("keywords") String keywords, @RequestParam("selectdb") boolean selectdb,@RequestParam("dbname") String dbname,
                               @RequestParam("selectsina") boolean selectsina,@RequestParam("selecttime") boolean selecttime,@RequestParam("timestart") String timestart,
                               @RequestParam("timeend") String timeend,@RequestParam("selectoldlabel") boolean selectoldlabel,@RequestParam("oldlabel") String oldlabel,
                               @RequestParam("newlabel") String newlabel,@RequestParam("writedb") String writedb) {
        logger.info(keywords+'\n'+selectdb+'\n'+dbname+'\n'+selectsina+'\n'+selecttime+'\n'+timestart+'\n'+timeend+'\n'+selectoldlabel+'\n'+oldlabel+'\n'+newlabel+'\n'+writedb+'\n');
        DataLabelResponse dataLabelResponse = new DataLabelResponse();
        String [] keylist=keywords.split("\\+");
        String dbquery="%";
        String sinaquery=" ";
        for(int i=0;i<keylist.length;i++)
        {
            String keyword = keylist[i].trim();
            dbquery += keyword+"%";
            sinaquery+=keyword+" ";
        }
        if(selectdb)
        {
            List<DataRecord> dbdata = search_db(dbquery,dbname,selecttime,timestart,timeend);
        }
        if(selectsina)
        {
            List<DataRecord> sinadata=search_sina(sinaquery,selecttime,timestart,timeend);
        }

        Gson gson = new Gson();
        return gson.toJson(dataLabelResponse);
        }

    private List<DataRecord> search_sina(String sinaquery, boolean selecttime, String timestart, String timeend) {//从新浪爬取微博
        List<DataRecord> records =null;
        return records;
    }

    private List<DataRecord> search_db(String dbquery, String dbname, boolean selecttime, String timestart, String timeend) {//从数据库中查询关键词并返回结果
        List<DataRecord> records =null;
        return records;
    }
}


