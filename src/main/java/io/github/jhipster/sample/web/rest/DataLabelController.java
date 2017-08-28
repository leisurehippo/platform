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

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//参数类，对参数进行统一处理
class Param {
    public String keywords;
    public String dbname;
    public boolean selecttime;
    public String timestart;
    public String timeend;
    public boolean selectoldlabel;
    public String oldlabel;
    public String newlabel;
    public int type;
    public boolean selectkey;
    public String tag;
    int page;

    public Param() {
    }

    public Param(String _keywords, String _dbname, boolean _selecttime, String _timestart, String _timeend,
                 boolean _selectoldlabel, String _oldlabel, String _newlabel, int _type, boolean _selectkey, int _page) {
        keywords = _keywords.trim();
        dbname = _dbname.trim();
        selectkey = _selectkey;
        selecttime = _selecttime;
        timestart = _timestart.trim();
        timeend = _timeend.trim();
        selectoldlabel = _selectoldlabel;
        oldlabel = _oldlabel.trim();
        newlabel = _newlabel.trim();
        page = _page;
        type = _type;
        if (!selectkey)
            keywords = "";
        if (selectoldlabel) {
            tag = "+" + oldlabel;
        } else {
            tag = "+" + newlabel;
        }

        if (type == 1) {
            String[] keylist = keywords.trim().split("\\+");
            keywords = "%";
            //对搜索关键字进行格式转换，数据库用%做分隔符，新浪微博用空格分割
            for (int i = 0; i < keylist.length; i++) {
                String keyword = keylist[i].trim();
                    keywords += keyword + "%";
            }
        }

    }


}

//返回的数据实体，包括微博的id和内容
class ResponseEntity {
    public String since_id;
    public String weibo_content;

    public ResponseEntity() {
    }

    public ResponseEntity(String id, String content) {
        since_id = id;
        weibo_content = content;
    }
}

//处理提交请求后，返回的内容，包括本次处理结果以及下一批微博数据
class DataLabelResponse {

    public List<ResponseEntity> dataset;
    public int success_count;
    public int response_code;
    public int page;

    public DataLabelResponse() {
        dataset = new ArrayList<ResponseEntity>();
        //成功处理的微博数量
        success_count = 0;
        //0：成功 -1：失败 1：成功但是到达了最后一页
        response_code = 0;
        //前端维护自己在后台的分页，初始为0
        page = 0;
    }
}

//前端界面的初始化，返回的参数实体，包括现在已有的标签
class LabelInitResponse {
    public List<String> all_label;

    public LabelInitResponse() {
        all_label = new ArrayList<String>();
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
    @Autowired
    private LabelRelationInfoDAO labelRelationInfoDAO;
    @Autowired
    private SinaDataInfoDAO sinaDataInfoDAO;
    private String rootdir="./src/main/java/io/github/jhipster/sample/web/rest/";
    private String temp_path="/temp/";
    private String python_path="/python/";
    private String model_path="/label_model/";
    private String classfy_file="xgb_api.py";
    private String search_file="get_sina_weibo.py";
    private static final int num_per_page = 1000; //每次返回至少num_per_page条数据
    private static final int num_per_search = 500;  //每次向数据库查询num_per_search条数据

    /**
     * 处理前端页面的初始化请求
     */
    @GetMapping("/Initservice")
    @ResponseBody
    public String returnInit() {
        List<LabelRelationInfo> all_labels = (List<LabelRelationInfo>) labelRelationInfoDAO.findAll();
        LabelInitResponse labelInitResponse = new LabelInitResponse();
        for (int i = 0; i < all_labels.size(); i++) {
            labelInitResponse.all_label.add(all_labels.get(i).getLabelRelationKey().getParent_label());
        }
        Gson gson = new Gson();
        return gson.toJson(labelInitResponse);
    }

    /**
     * 处理搜索请求
     */
    @GetMapping("/dataLabelservice")
    @ResponseBody
    public String dataSelect(@RequestParam("keywords") String keywords, @RequestParam("dbname") String dbname,
                             @RequestParam("selecttime") boolean selecttime, @RequestParam("timestart") String timestart,
                             @RequestParam("timeend") String timeend, @RequestParam("selectoldlabel") boolean selectoldlabel, @RequestParam("oldlabel") String oldlabel,
                             @RequestParam("newlabel") String newlabel, @RequestParam("type") int type,@RequestParam("page") int page,
                             @RequestParam("selectkey") boolean selectkey, HttpSession session) throws IOException {
        logger.info(keywords + '\n' + dbname + '\n' + selecttime + '\n' + timestart + '\n' + timeend + '\n' + selectoldlabel + '\n'
            + oldlabel + '\n' + newlabel + '\n' + type + '\n');
        DataLabelResponse dataLabelResponse = null;
        Param param = new Param(keywords, dbname, selecttime, timestart, timeend, selectoldlabel, oldlabel, newlabel, type, selectkey, page);
        logger.info(param.keywords);
        //对数据库搜索页和新浪微博搜索页的请求分开处理
        if (type == 1)//数据库搜索页
        {
            dataLabelResponse = search_db(param, session);
        } else if (type == 0)//新浪搜索
        {
            search_sina(param,session);
        }

        Gson gson = new Gson();
        return gson.toJson(dataLabelResponse);
    }


    /**
     * 处理提交请求，返回下一页
     */
    @GetMapping("/Submitservice")
    public String Getsubmit(@RequestParam(value = "labelresult", required = false) List<String> labelresult, @RequestParam("keywords") String keywords, @RequestParam("dbname") String dbname,
                            @RequestParam("selecttime") boolean selecttime, @RequestParam("timestart") String timestart,
                            @RequestParam("timeend") String timeend, @RequestParam("selectoldlabel") boolean selectoldlabel, @RequestParam("oldlabel") String oldlabel,
                            @RequestParam("newlabel") String newlabel, @RequestParam("type") int type,@RequestParam("page") int page,
                            @RequestParam("selectkey") boolean selectkey, HttpSession session) throws IOException {
        //干两件事：保存数据、查询下一批数据
        if (labelresult == null)//前端没有勾选任何微博
            labelresult = new ArrayList<String>();
        String tag=null;
        if (selectoldlabel) {
            tag = "+" + oldlabel;
        } else {
            tag = "+" + newlabel;
        }
        int count = 0;
        DataLabelResponse dataLabelResponse = new DataLabelResponse();
        //写入数据库
        logger.info("labelresult size:" + labelresult.size());
        for (int i = 0; i < labelresult.size(); i++) {
            //提交的的参数里只有id没有微博内容，去原始数据库里查询，这样可以避免在内存里保存微博内容，并且可以减少前端传参的量
            DataInfo _data = dataInfoDAO.findBySince_id(dbname, labelresult.get(i));

            try {
                dataLabelInfoDAO.save(new DataLabelInfo(new LabelDataSetKey(labelresult.get(i), tag), _data.getWeibo_time(), _data.getWeibo_content()));
                count++;
            } catch (Exception e) {
                dataLabelResponse.success_count = count;
                dataLabelResponse.response_code = -1;
                Gson gson = new Gson();
                return gson.toJson(dataLabelResponse);
            }
            logger.info(_data.getWeibo_content() + "----------" + tag);

        }
        Gson gson=new Gson();
        dataLabelResponse=gson.fromJson(dataSelect(keywords, dbname, selecttime, timestart, timeend, selectoldlabel, oldlabel, newlabel, type, page,selectkey,session),DataLabelResponse.class);
        dataLabelResponse.success_count = count;
        //查询下一批数据
//        dataLabelResponse = search_db(dbname, dbquery, selecttime, timestart, timeend, tag, page, session);
//
//
//        Gson gson = new Gson();
        return gson.toJson(dataLabelResponse);

    }

//    public Map<List<String>,Boolean> get_label_data()
//    {
//        Map<List<String>,Boolean> labelled_map=new HashMap<List<String>,Boolean>();
//        List<DataLabelInfo> label_data= (List<DataLabelInfo>) dataLabelInfoDAO.findAll();
//        for(int i=0;i<label_data.size();i++)
//        {
//            String label_id=label_data.get(i).getKey().getSince_id();
//            String label_tag=label_data.get(i).getKey().getTag();
//            labelled_map.put(Arrays.asList(label_id,label_tag),true);
//        }
//        return labelled_map;
//    }

    /**
     * 处理微博爬取请求
     */
    public DataLabelResponse search_sina(Param param,HttpSession session) throws IOException {
        get_sina_weibo(param,session.getId());//爬虫结果写入数据库
        DataLabelResponse dataLabelResponse = new DataLabelResponse();
       return dataLabelResponse;
    }



    /**
     * 使用命令行调用python，将结果写入到文件中
     */
    public void use_python(String command) throws IOException {
        Process p = Runtime.getRuntime().exec(command);//需要检查是否出错
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String li = null;
        StringBuilder sb = new StringBuilder();
        while ((li = br.readLine()) != null) {
            sb.append(li + "\n");
        }
        logger.info("~~~~~~~~~~~~~~~~~~~~~~~");
        logger.info(command);
        logger.info(String.valueOf(p.exitValue()));
        logger.info(sb.toString());
    }

    /**
     * 调用爬虫并将结果写入到数据库
     */
    public void get_sina_weibo(Param param,String session_id) throws IOException {
        String save_path = rootdir+temp_path+"sina_"+session_id+".txt";
        String py_path = rootdir+python_path+search_file;
        String command = "python "+ py_path+" "+save_path;
        use_python(command);
        List<SinaDataInfo> sinadata = new ArrayList<SinaDataInfo>();
        List<DataInfo> data = new ArrayList<DataInfo>();
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(save_path));
            BufferedReader bufread = new BufferedReader(read);
            String lines = null;

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String now_time = df.format(new Date());//
            while ((lines = bufread.readLine()) != null) {
                String[] line = lines.trim().split("\t");
                sinadata.add(new SinaDataInfo(new SinaDataKey(session_id, now_time, line[0].trim()), line[1].trim(), line[2].trim(), line[3].trim(), line[4].trim()));
                data.add(new DataInfo(line[0].trim(), line[2].trim(), line[1].trim()));
            }
            read.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //同时写入到原始数据库和爬虫数据库
        sinaDataInfoDAO.save(sinadata);
        dataInfoDAO.save(data);



    }


    /**
     * 返回微博内容时，需要对原始微博用分类器筛选，并且去掉已经被标注过的重复微博
     */
    public List<DataInfo> fliter(List<DataInfo> data, String tag, String session_id) throws IOException {
        String infile = rootdir+temp_path+"temp_" + session_id + ".txt";
        String outfile = rootdir+temp_path+"out_" + session_id + ".txt";

        try {
            FileWriter file = new FileWriter(infile);
            for (int i = 0; i < data.size(); i++) {
                String id = data.get(i).getSince_id();
                String content = data.get(i).getWeibo_content();
                content = content.replace('\n', ' ');
                content = content.replace('\t', ' ');
                String time = data.get(i).getWeibo_time();
                if (!dataLabelInfoDAO.exists(new LabelDataSetKey(id, tag)))//去重
                {
                    //去重后的微博写入文件，再调用python脚本进行筛选
                    file.write(id + '\t' + time + '\t' + content + '\t' + tag + '\n');
                }
            }
            file.close();

            //筛选

            String model_dir = rootdir+model_path;
            String pyfile = rootdir+python_path+classfy_file;
            String command = "python " + pyfile + " predict " + infile + " " + model_dir + " " + outfile;
            //command = "python ./src/main/java/io/github/jhipster/sample/web/rest/python/test.py";
            //        PythonInterpreter interpreter = new PythonInterpreter();
            //        interpreter.execfile("./src/main/java/io/github/jhipster/sample/web/rest/python/test.py");
            use_python(command);
            data = new ArrayList<DataInfo>();
            try {
                InputStreamReader read = new InputStreamReader(new FileInputStream(outfile));
                BufferedReader bufread = new BufferedReader(read);
                String lines = null;

                while ((lines = bufread.readLine()) != null) {
                    String[] line = lines.trim().split("\t");
                    if (line.length == 3)
                        data.add(new DataInfo(line[0].trim(), line[1].trim(), line[2].trim()));
                }
                read.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (deletefile(infile))
                logger.info("文件" + infile + "删除成功!");
            else
                logger.info("文件" + infile + "删除失败!");
            if (deletefile(outfile))
                logger.info("文件" + outfile + "删除成功!");
            else
                logger.info("文件" + outfile + "删除失败!");
        }
        return data;
    }

    /**
     * 用于和python交互的临时数据文件需要删除
     */
    public boolean deletefile(String filepath) {
        File file = new File(filepath);
        if (!file.exists())
            return true;
        if (file.delete())
            return true;
        else
            return false;
    }


    /**
     * 从数据库查询微博，经过筛选和去重后返回结果
     */
    public DataLabelResponse search_db(Param param ,HttpSession session) throws IOException {
        int pagestart = 0;
        List<DataInfo>   dbdata = new ArrayList<DataInfo>();
        DataLabelResponse dataLabelResponse = new DataLabelResponse();
        logger.info("page:" + param.page + " " + param.keywords);
        //每次向数据库查询num_per_search条微博，筛选后加入到结果队列中，不停循环直到结果队列满足一定长度
        while (dbdata.size() < num_per_page) {
            pagestart = param.page * num_per_search;
            List<DataInfo> temp = null;
            if (param.selecttime) {
                temp = dataInfoDAO.fliterByTimeAndKey(param.dbname, param.timestart, param.timeend, param.keywords, pagestart, num_per_search);
            } else {
                temp = dataInfoDAO.findByKey(param.dbname, param.keywords, pagestart, num_per_search);
                //records=(List<DataInfo>)dataInfoDAO.findAll();
            }
            logger.info("temp size:" + temp.size() + " " + pagestart);
            //数据库查询结果为空，表示已经到达最后一页，此时队列长度不够也应该结束循环
            if (temp.size() < 1) {
                break;
            }
            temp = fliter(temp, param.tag, session.getId());//筛选以及去重
            dbdata.addAll(temp);
            param.page++;
        }
        logger.info("-------->>>" + dbdata.size() + "--->" + param.page);
        for (int i = 0; i < dbdata.size(); i++) {
            logger.info("-------->>>" + dbdata.get(i).getWeibo_content());
            String id = dbdata.get(i).getSince_id();
            String content = dbdata.get(i).getWeibo_content();
            dataLabelResponse.dataset.add(new ResponseEntity(id, content));

        }
        //查不到任何数据，已经到达最后一页
        if (dbdata.size() == 0) {
            dataLabelResponse.response_code = 1;
        } else
            dataLabelResponse.response_code = 0;
        dataLabelResponse.page = param.page;
        return dataLabelResponse;
    }


}


