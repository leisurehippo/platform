package io.github.jhipster.sample.web.rest;

/**
 * Created by HB on 2017/7/10.
 */

import com.google.gson.Gson;
import io.github.jhipster.sample.web.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public String parentlabel;
    boolean ischildlabel;
    int page;

    public Param() {
    }

    public Param(String _keywords, String _dbname, boolean _selecttime, String _timestart, String _timeend,
                 boolean _selectoldlabel, String _oldlabel, String _newlabel, int _type, boolean _selectkey, int _page,
                 String parent,boolean ischild) {
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
        parentlabel=parent.trim();
        ischildlabel=ischild;
        String[] names=dbname.split("\\(");
        dbname=names[0];
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
        //0：成功    -1：失败    1：成功但是到达了最后一页    -2:新建标签出现重复  -3:(父)标签不存在，或者已经被删除
        response_code = 0;
        //前端维护自己在后台的分页，初始为0
        page = 0;
    }
}

//前端界面的初始化，返回的参数实体，包括现在已有的标签
//标签:true 表示是root标签  false表示非root标签
class LabelInitResponse {
    public Map<String,Boolean> all_label;
    public List<String> all_dbnames;

    public LabelInitResponse() {
        all_dbnames=new ArrayList<String>();
        all_label = new HashMap<String,Boolean>();
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
    @Autowired
    private TableInfoDAO tableInfoDAO;
    @Autowired
    private DatalabelServiceBean datalabelServiceBean;
    private static final String rootdir="./src/main/java/io/github/jhipster/sample/web/rest/";
    private static final String temp_path="/temp/";
    private static final String python_path="/python/";
    private static final String model_path="/label_model/";
    private static final String classfy_file="xgb_api.py";
    private static final String search_file="get_sina_weibo.py";
    private static final String img_path="./src/main/webapp/";
    private static final int num_per_page = 500; //每次返回至少num_per_page条数据
    private static final int num_per_search = 500;  //每次向数据库查询num_per_search条数据
    private  static  final String Root_Target_Name="(成为根标签)";


    /**
     * **********************************************************************************************
     *                                 请求响应函数
     * **********************************************************************************************
     */

    /**
     * 处理前端页面的初始化请求
     */
    @GetMapping("/Initservice")
    @ResponseBody
    public String returnInit(@RequestParam("get_dbname") boolean get_dbname) {
        List<LabelRelationInfo> all_labels = (List<LabelRelationInfo>) labelRelationInfoDAO.findAll();
        LabelInitResponse labelInitResponse = new LabelInitResponse();
        //返回标签以及是否是root标签
        for (int i = 0; i < all_labels.size(); i++) {
            String clabel = all_labels.get(i).getChild_label().trim();
            String plabel = all_labels.get(i).getParent_label().trim();
            boolean isroot = false;
            if(plabel.length()<=0)
                isroot=true;
            if(clabel.length()>0) {
                labelInitResponse.all_label.put(clabel, isroot);
            }
        }
        //返回数据库的可选表名以及表注释
        if(get_dbname)
        {
            String database = tableInfoDAO.getDatabaseName();
            List<TableInfo> tableInfos = tableInfoDAO.findTableAndComment(database);
            for(int j=0;j<tableInfos.size();j++)
            {
                String name = tableInfos.get(j).getTable_name().trim();
                String comment=tableInfos.get(j).getTable_comment().trim();
                String[] fname = name.split("_");
                if(fname[0].trim().equals("data"))
                {
                    labelInitResponse.all_dbnames.add(name+"("+comment+")");
                }
            }
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
                             @RequestParam("newlabel") String newlabel, @RequestParam("type") int type,@RequestParam("page") int page, @RequestParam("selectkey") boolean selectkey,
                             @RequestParam("parentlabel") String parentlabel,@RequestParam("ischildlabel") boolean ischildlabel, HttpSession session) throws IOException {
        logger.info(keywords + '\n' + dbname + '\n' + selecttime + '\n' + timestart + '\n' + timeend + '\n' + selectoldlabel + '\n'
            + oldlabel + '\n' + newlabel + '\n' + type + '\n'+ischildlabel+'\n'+parentlabel+'\n');
        DataLabelResponse dataLabelResponse = new DataLabelResponse();
        Param param = new Param(keywords, dbname, selecttime, timestart, timeend, selectoldlabel, oldlabel, newlabel, type, selectkey, page,parentlabel,ischildlabel);
        logger.info(param.keywords);
        logger.info(param.tag+"---->>>");

        //对数据库搜索页和新浪微博搜索页的请求分开处理
        if (type == 1)//数据库搜索页
        {
            dataLabelResponse = search_db(param, session);
        } else if (type == 0)//新浪搜索
        {
            search_sina(param,session);
        }

        Gson gson = new Gson();
        logger.info(session.getId());
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
                            @RequestParam("selectkey") boolean selectkey,@RequestParam("parentlabel") String parentlabel,@RequestParam("ischildlabel") boolean ischildlabel, HttpSession session) throws IOException {
        //干两件事：保存数据、查询下一批数据
        DataLabelResponse dataLabelResponse = new DataLabelResponse();
        if (labelresult == null)//前端没有勾选任何微博
            labelresult = new ArrayList<String>();
        String tag=null;
        dbname=dbname.trim().split("\\(")[0];
        if (selectoldlabel) {
            tag = "+"+ oldlabel.trim();
            if(!labelRelationInfoDAO.exists(oldlabel))//收到了一个不存在的已有标签
            {
                dataLabelResponse.success_count =  0;
                dataLabelResponse.response_code = -3;
                Gson gson = new Gson();
                return gson.toJson(dataLabelResponse);
            }
        } else {
            tag ="+"+  newlabel.trim();
        }
        int count = 0;
        String _parent="";
        String _child=tag.substring(1);
        if (ischildlabel) {
            _parent=parentlabel.trim();
            if(!labelRelationInfoDAO.exists(_parent)){//收到了不存在的父标签
                dataLabelResponse.success_count =  0;
                dataLabelResponse.response_code = -3;
                Gson gson = new Gson();
                return gson.toJson(dataLabelResponse);
            }
        }
        boolean isexist = false;
        if(!selectoldlabel) {//当创建新标签时
            //检查该标签关系是否存在
            String _par = labelRelationInfoDAO.findParent(_child);
            if (_par!=null && !_par.equals(_parent))//新建标签重复
            {
                dataLabelResponse.success_count = 0;
                dataLabelResponse.response_code = -2;
                Gson gson = new Gson();
                return gson.toJson(dataLabelResponse);
            }
            else if(_par!=null)
            {
                isexist=true;
            }
        }
        Map<String,Map> relation=null;
        Map<String,String> childtoparent=null;
        Map<String,List<String>> parenttochild=null;
        if(labelresult.size()>0)
        {
            relation = get_label_relation();
            childtoparent=relation.get("childtoparent");
            parenttochild= relation.get("parenttochild");
        }
        logger.info("labelresult size:" + labelresult.size());
        List<DataLabelInfo> dataLabelInfos = new ArrayList<DataLabelInfo>();
        try {
            if(labelresult.size()>0) { //保证至少写入一条记录后，才加上标签关系,用Insert是因为child一旦存在就会报错，使用save的话多人操作会互相修改
                //先插入内容表，再插入关系表，保证内容优先写入不浪费
                for (int i = 0; i < labelresult.size(); i++) {
                    //提交的的参数里只有id没有微博内容，去原始数据库里查询，这样可以避免在内存里保存微博内容，并且可以减少前端传参的量
                    DataInfo _data = dataInfoDAO.findBySince_id(dbname, labelresult.get(i));
                    dataLabelInfoDAO.save(new DataLabelInfo(new LabelDataSetKey(labelresult.get(i), tag), _data.getWeibo_time(), _data.getWeibo_content()));
                    count++;
                }
                if(!selectoldlabel) {//创建新标签需要写入数据库
//                    if(!isexist){
//                        labelRelationInfoDAO.save(new LabelRelationInfo(_parent,_child));
//                        isexist=true;
//                    }

                    try {
                        if(!isexist) {
                            labelRelationInfoDAO.insert(_parent, _child);
                            isexist=true;
                        }
                    }catch (Exception e)
                    {
                        String par = labelRelationInfoDAO.findParent(_child);
                        //插入新标签时，标签已经存在并且和新标签不同，报标签重复错误
                        if(!par.equals(_parent)) {
                            e.printStackTrace();
                            dataLabelResponse.success_count = 0;
                            dataLabelResponse.response_code = -2;
                            Gson gson = new Gson();
                            return gson.toJson(dataLabelResponse);
                        }
                        e.printStackTrace();
                        dataLabelResponse.success_count = 0;
                        dataLabelResponse.response_code = -1;
                        Gson gson = new Gson();
                        return gson.toJson(dataLabelResponse);
                    }
                }

            }
//               List<DataLabelInfo> infos= save_all_parents(tag.substring(1),childtoparent,parenttochild,_data);
//               dataLabelInfoDAO.save(infos);
        } catch (Exception e) {
            e.printStackTrace();
            dataLabelResponse.success_count = count;
            dataLabelResponse.response_code = -1;
            Gson gson = new Gson();
            return gson.toJson(dataLabelResponse);
        }



        Gson gson=new Gson();
        dataLabelResponse=gson.fromJson(dataSelect(keywords, dbname, selecttime, timestart, timeend, selectoldlabel, oldlabel, newlabel, type, page,selectkey,parentlabel,ischildlabel,session),DataLabelResponse.class);
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
     * **********************************************************************************************
     *                                 微博爬取相关函数
     * **********************************************************************************************
     */


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
    public void exec_cmd(String command) throws IOException {
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
        exec_cmd(command);
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
 * **********************************************************************************************
 *                                 核心处理逻辑函数
 * **********************************************************************************************
 */
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
            exec_cmd(command);
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
//            if (deletefile(infile))
//                logger.info("文件" + infile + "删除成功!");
//            else
//                logger.info("文件" + infile + "删除失败!");
//            if (deletefile(outfile))
//                logger.info("文件" + outfile + "删除成功!");
//            else
//                logger.info("文件" + outfile + "删除失败!");
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
        long now = System.currentTimeMillis();
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
        logger.info("cost:"+(System.currentTimeMillis()-now)/1000/60+" min");
        return dataLabelResponse;
    }


    /**
     * **********************************************************************************************
     *                                 标签控制函数
     * **********************************************************************************************
     */

    /**
     * 根据root节点和数据内的标签关系字典，得到从root开始的所有子节点,后序遍历
     */
    public List<String> get_all_child(String root,Map<String,List<String>> parenttochild) throws InterruptedException {
        Stack all_child = new Stack();
        List<String> children=new ArrayList<>();
        all_child.push(root);
        List<String> childlist = parenttochild.get(root);
        while(!all_child.empty())
        {
            if(childlist!=null) {
                for (int i = 0; i < childlist.size(); i++) {
                    all_child.push(childlist.get(i));
                }
            }
            String top_label= (String) all_child.peek();
            List<String> top_label_child = parenttochild.get(top_label);
            logger.info("!!!!!!!!!!!!"+top_label+"---------------->>"+(top_label_child==null));
            if(top_label_child==null)//栈顶元素时叶子结点
            {
                children.add((String) all_child.pop());//访问该叶子结点并弹出
                childlist=null;
            }
            else
            {
                if(children.size()>0&&top_label_child.contains(children.get(children.size()-1)))//栈顶元素不是叶子结点，但是上一个访问的是它的孩子，现在就该访问它了
                {
                    children.add((String) all_child.pop());
                    childlist=null;

                }
                else
                {
                    childlist=parenttochild.get(top_label);
                }
            }

        }
        logger.info("根节点为:"+root);
        for(int j =0;j<children.size();j++)
        {
            logger.info(children.get(j));
        }
        return children;
    }


    /**
     * 从数据库中获取标签关系,返回两个map，一个是child-parent，一个是parent-[child1,child2..]
     */
    public Map<String,Map> get_label_relation()
    {
        Map<String,Map>relation = new HashMap<String,Map>();
        List<LabelRelationInfo> labelRelationInfos = (List<LabelRelationInfo>) labelRelationInfoDAO.findAll();
        Map<String,String> parentmap = new HashMap<String,String> (); //child-parent
        Map<String,List<String>> childmap = new HashMap<String,List<String>> (); // parent-[child1,child2..]
        List<String> childlist = null;
        String child_label=null;
        String parent_label = null;
        for(int i=0;i<labelRelationInfos.size();i++)
        {
            child_label = labelRelationInfos.get(i).getChild_label().trim();
            parent_label = labelRelationInfos.get(i).getParent_label().trim();
            parentmap.put(child_label,parent_label);
            childlist = childmap.get(parent_label);
            if(childlist==null)
            {
                childlist=new ArrayList<String>();
            }
            childlist.add(child_label);
            childmap.put(parent_label,childlist);
        }
        relation.put("childtoparent",parentmap);
        relation.put("parenttochild",childmap);
        return relation;
    }

    /**
     * 将一个父标签，打到所有子标签中,返回所有的微博
     * 选择返回微博，而不是直接save，是考虑到数据库的回滚，避免只修改了部分子标签就挂掉了。将所有的微博一次性save，会变成一个事务，不会出现只有部分操作完成的情况
     * 但是这样的缺点是会读数据到内存，量大了就吃不消。优化的话，应该拼接sql语句并作为事务提交到数据库:
     * insert into tb1
     * select since_id,...,parent from  tb2 where tag = child1 or tag = child2 or ...
     * 这样将child全部加上parent标签，并且select操作将内存压力放到了数据库里而不是服务器上
     * 因为使用的是CrudRepository,需要改成jdbc才能拼接sql,这样还要自己封装crud接口，遇到内存瓶颈再说吧。
     */
    public List<DataLabelInfo> save_parent(String par,String childroot,Map<String,List<String>> parenttochild) throws InterruptedException {
        List<String> all_child = get_all_child(childroot,parenttochild);
        List<DataLabelInfo> dataLabelInfos = new ArrayList<DataLabelInfo>();
        for(int j =0;j<all_child.size();j++) {//取出所有子标签的所有微博
            String child = all_child.get(j);
            List<DataLabelInfo> Infos = dataLabelInfoDAO.findByTag('+' + child.trim());

            //将该子标签对应的所有微博都加上父标签
            for (int i = 0; i < Infos.size(); i++) {
                DataLabelInfo Info = Infos.get(i);
                DataLabelInfo newinfo = new DataLabelInfo(new LabelDataSetKey(Info.getKey().getSince_id(), '+' + par.trim()), Info.getWeibo_time(), Info.getWeibo_content());
                dataLabelInfos.add(newinfo);
            }

        }
        return dataLabelInfos;

    }

    /**
     * 找某个节点的所有父标签并打到子标签上
     * data:null 表示将所有的父标签打到所有的子标签的记录上  not null 表示将所有的父标签只打到这一条记录上
     */
    public  List<DataLabelInfo> save_all_parents(String child,Map<String,String> childtpparent,Map<String,List<String>> parenttochild,DataInfo data ) throws Exception {


        String parent = childtpparent.get(child).trim();
        List<DataLabelInfo> dataLabelInfos=new ArrayList<DataLabelInfo>();
        while(parent.length()>0)
        {
            //将每个父标签打到子标签中,子标签微博中已经包含了该子标签下所有子标签的微博
            if(data==null) {
                //List<DataLabelInfo> Infos= save_parent(parent,child,parenttochild);
                List<DataLabelInfo> Infos = dataLabelInfoDAO.findByTag("+"+child);
                for(int i=0;i<Infos.size();i++)
                {
                    DataLabelInfo Info = Infos.get(i);
                    DataLabelInfo newinfo = new DataLabelInfo(new LabelDataSetKey(Info.getKey().getSince_id(), '+' + parent.trim()), Info.getWeibo_time(), Info.getWeibo_content());
                    dataLabelInfos.add(newinfo);
                }

            }
            //将每个父标签打到data中
            else{
                dataLabelInfos.add(new DataLabelInfo(new LabelDataSetKey(data.getSince_id(), '+'+parent), data.getWeibo_time(), data.getWeibo_content()));

            }
            parent = childtpparent.get(parent).trim();
        }
        return dataLabelInfos;
    }

    /**
     * 标签新建
     */
    @GetMapping("/Labelcreateservice")
    @ResponseBody
    public String create_label(@RequestParam("new_label") String new_label,@RequestParam("new_parent_label") String new_parent_label,@RequestParam("select_parent") Boolean select_parent){

        Gson gson=new Gson();
        Map status = new HashMap<String,Boolean>();
        new_label=new_label.trim();
        new_parent_label=new_parent_label.trim();
        if(!select_parent){
            new_parent_label="";
        }
        if(new_label.length()<=0|| labelRelationInfoDAO.exists(new_label)){
            status.put("status",false);
            return gson.toJson(status);
        }
        try {
            labelRelationInfoDAO.insert(new_parent_label, new_label);
        }catch (Exception e){
            e.printStackTrace();
            status.put("status",false);
            return gson.toJson(status);
        }
        status.put("status",true);
        return gson.toJson(status);
    }


    /**
     * 标签合并
     */
    @GetMapping("/Labeljoinservice")
    @ResponseBody
    public String join_labels( @RequestParam("joined_labels") List<String> joined_labels,@RequestParam("join_target") String join_target )
    {

        Gson gson = new Gson();
        Map status = new HashMap<String,Boolean>();
        join_target=join_target.trim();

        try{
            if(join_target.length()<=0|| joined_labels.size()<1 || !labelRelationInfoDAO.exists(join_target))
            {
                status.put("status",false);
                return gson.toJson(status);
            }
            for(int i =0;i<joined_labels.size();i++) {
                String label = joined_labels.get(i).trim();
                if(label.equals(join_target)){
                    continue;
                }
                if(label.length()>0&& labelRelationInfoDAO.exists(label)) {
                    datalabelServiceBean.update_name(label, join_target);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            status.put("status",false);
            return gson.toJson(status);
        }
        status.put("status",true);
        return gson.toJson(status);
    }

    /**
     * 标签重命名
     */
    @GetMapping("/Labelrenameservice")
    @ResponseBody
    public String rename_labels( @RequestParam("old_name") String old_name,@RequestParam("new_name") String new_name )
    {

        Gson gson = new Gson();
        Map status = new HashMap<String,Boolean>();
        old_name=old_name.trim();
        new_name=new_name.trim();

        try{
            if(!labelRelationInfoDAO.exists(old_name)|| labelRelationInfoDAO.exists(new_name) ||new_name.length()<=0 || old_name.length()<=0)
            {
                status.put("status",false);
                return gson.toJson(status);
            }
            if(old_name.equals(new_name)){
                status.put("status",true);
                return gson.toJson(status);
            }
          datalabelServiceBean.rename(old_name,new_name);
        }catch (Exception e){
            e.printStackTrace();
            status.put("status",false);
            return gson.toJson(status);
        }
        status.put("status",true);
        return gson.toJson(status);
    }


    /**
     * 标签迁移
     */
    @GetMapping("/Labelchangeservice")
    @ResponseBody
    public String change_labels( @RequestParam("change_label") String change_label,@RequestParam("change_tlabel") String change_tlabel,@RequestParam("ischangeall") boolean ischangeall)
    {

        Gson gson = new Gson();
        Map status = new HashMap<String,Boolean>();
        change_label=change_label.trim();
        change_tlabel=change_tlabel.trim();
        try{
            if(!change_tlabel.equals(Root_Target_Name)) {

                if (!labelRelationInfoDAO.exists(change_label) || !labelRelationInfoDAO.exists(change_tlabel) || change_label.length() <= 0 || change_tlabel.length() <= 0) {
                    status.put("status", false);
                    return gson.toJson(status);
                }
                if (change_label.trim().equals(change_tlabel.trim())) {
                    status.put("status", true);
                    return gson.toJson(status);
                }
            }
            else{
                change_tlabel="";
            }
            if(!ischangeall)//不迁移子标签
            {
                String parent = labelRelationInfoDAO.findParent(change_label);
                List<String> child_list = labelRelationInfoDAO.findChild(change_label);
                for(int i=0;i<child_list.size();i++)
                {
                    labelRelationInfoDAO.update_parent(parent,child_list.get(i));
                }
            }

            labelRelationInfoDAO.update_parent(change_tlabel,change_label);
        }catch (Exception e){
            e.printStackTrace();
            status.put("status",false);
            return gson.toJson(status);
        }
        status.put("status",true);
        return gson.toJson(status);
    }

//    /**
//     * 标签删除
//     */
    @GetMapping("/Labeldelservice")
    @ResponseBody
    public String delete_labels(@RequestParam("del_label") String label,@RequestParam("isdelall") boolean delete_child)
    {
        Gson gson = new Gson();
        Map status = new HashMap<String ,Boolean>();
        label=label.trim();
        if(label.length()<=0)
        {
            status.put("status",false);
            return gson.toJson(status);
        }
        try {
            if (!delete_child) {
                List<String> labels = labelRelationInfoDAO.findChild(label);
                String parent = labelRelationInfoDAO.findParent(label);
                for (int i = 0; i < labels.size(); i++) {
                    labelRelationInfoDAO.update_parent(parent, labels.get(i));
                }
                labelRelationInfoDAO.delete(label);
            } else//删除所有子标签
            {
                Map<String, Map> relation = get_label_relation();
                Map<String, List<String>> parenttochild = relation.get("parenttochild");
                List<String> child_list = get_all_child(label, parenttochild);
                for (int i = 0; i < child_list.size(); i++) {
                    logger.info(child_list.get(i));
                    labelRelationInfoDAO.delete(child_list.get(i));
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            status.put("status",false);
            return gson.toJson(status);
        }
        status.put("status",true);
        return gson.toJson(status);
    }

    /**
     * 标签汇聚
     */
    @GetMapping("/Labelmergeservice")
    @ResponseBody
    public String merge_labels(@RequestParam("merged_labels") List<String> merged_labels,@RequestParam("merge_label") String merge_label) throws Exception {
        merge_label=merge_label.trim();
        Gson gson = new Gson();
        Map status = new HashMap<String ,Boolean>();
        String p =labelRelationInfoDAO.findParent(merge_label);
        //汇聚数量必须大于1个，汇聚标签不能为空，汇聚标签要么不存在，要么只能为root标签
        if(merged_labels.size()<1 || merge_label.length()<=0|| (p!=null&&p.length()>0) )
        {
            status.put("status",false);
            return gson.toJson(status);
        }
        Map<String, Map> relation = get_label_relation();
        Map<String,String> childtoparent = relation.get("childtoparent");
        Map<String,List<String>>parenttochild= relation.get("parenttochild");
        List<DataLabelInfo> dataLabelInfos = new ArrayList<DataLabelInfo>();
        try {
            labelRelationInfoDAO.save(new LabelRelationInfo("", merge_label));

            for (int i = 0; i < merged_labels.size(); i++) {
                if(merged_labels.get(i).trim().length()>0) {
                    if(merge_label.equals(merged_labels.get(i).trim()))
                        continue;
                    if(labelRelationInfoDAO.exists(merged_labels.get(i).trim())) {//待汇聚标签必须存在，否则忽略
                        labelRelationInfoDAO.save(new LabelRelationInfo(merge_label, merged_labels.get(i).trim()));
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            status.put("status",false);
            return gson.toJson(status);
        }
        status.put("status",true);
        return gson.toJson(status);

    }


    /**
     * 建立标签树，并做成图片保存
     */
    @GetMapping("/Labeldrawservice")
    @ResponseBody
    public String get_label_tree(HttpSession session)
    {
        Gson gson = new Gson();
        Map status = new HashMap<String,Boolean>();
        boolean result =true;
        String img_file=img_path+"tree.png";
        String img_dot=rootdir+temp_path+"tree_"+session.getId()+".dot";

        try{
            Map<String, Map> relation = get_label_relation();
            Map<String,String> childtoparent = relation.get("childtoparent");
            Map<String,List<String>> parenttochild = relation.get("parenttochild");
            List<String> all_data_tags= dataLabelInfoDAO.findAllTagswithnoflag();
            Set<String> all_relation_tags=new HashSet<String>(childtoparent.keySet());
            Set<String> all_relation_parent = new HashSet<String>(parenttochild.keySet());
            all_relation_parent.remove("");
            Set<String> error_parent=new HashSet<String>(all_relation_parent);//标签关系表中的异常父标签，就是只在parent_label中出现而不在child_label中出现,父标签为空的不算
            Set<String> data_tag_set = new HashSet<String>(all_data_tags) ;
            error_parent.removeAll(all_relation_tags);
            Set<String> error_data =new HashSet<String>(data_tag_set);//内容表中的异常标签，就是内容表中有这个标签而关系表没有
            error_data.removeAll(all_relation_tags);
            Map<String,Long> tag_attr_map = new HashMap<String,Long>();//包括关系表内所有的标签和内容表内的所有标签

            for(String label : all_data_tags)
            {
                label=label.trim();
                tag_attr_map.put(label,dataLabelInfoDAO.countTag("+"+label));
            }
            all_relation_tags.addAll(all_relation_parent);//包含了标签表所有的子标签和父标签
            for(String label:all_relation_tags){
                if(tag_attr_map.get(label.trim())==null) {
                    tag_attr_map.put(label.trim(), 0L);
                }
            }


            save_dot_file(img_dot,childtoparent,tag_attr_map,error_data,error_parent);
            String cmd="dot -Tpng "+img_dot+" -o "+img_file;
            exec_cmd(cmd);
            result=true;
        }catch (Exception e){
            e.printStackTrace();
            result=false;
        }finally {
//            if(deletefile(img_dot))
//                logger.info("文件" + img_dot + "删除成功!");
//            else
//                logger.info("文件" + img_dot + "删除失败!");
        }
        status.put("status",result);
        return gson.toJson(status);
    }

    private void save_dot_file(String img_dot,Map<String,String> childtoparent,Map<String,Long> tag_attr_map,Set<String> error_data,Set<String> error_parent) throws IOException {
        FileWriter file = new FileWriter(img_dot);

        file.write("digraph tree{node[fontname = \"Microsoft YaHei\"];\n ");
        for(String child : tag_attr_map.keySet()){
            child=child.trim();
            String parent=childtoparent.get(child);
            if(parent==null){
                parent="";
            }
            parent=parent.trim();
            Long child_num = tag_attr_map.get(child);
            Long parent_num=0L;
            if(tag_attr_map.containsKey(parent)) {
                parent_num = tag_attr_map.get(parent);
            }
            String child_with_num="\""+child+"("+child_num+")\"";;
            String parent_with_num="\""+parent+"("+Math.abs(parent_num)+")\"";
            file.write(child_with_num+";"+'\n');
            if(error_data.contains(child)) {
                file.write(child_with_num+"[color=red style=filled]" + ";" + '\n');
            }
            if(error_parent.contains(child) || child.length()<=0){
                file.write(child_with_num+"[color=gold style=filled]" + ";" + '\n');
            }
            if(parent.length()>0) {//父标签是空的不画出来，因为是root标签
                file.write(parent_with_num + "->" + child_with_num + '\n');
            }
        }
        file.write("}");
        file.close();
    }


    /**
     * 训练模型
     */
    @GetMapping("/Labeltrainservice")
    @ResponseBody
    public String train( @RequestParam("train_label") String train_label,@RequestParam("train_all") boolean train_all,HttpSession session)
    {
        boolean result =false;
        Map status = new HashMap<String ,Boolean>();
        Gson gson = new Gson();
        String path = rootdir+temp_path+"train_"+session.getId()+".txt";
        try{
            if(!labelRelationInfoDAO.exists(train_label) || train_label.trim().length()<=0)
            {
                status.put("status",false);
                return gson.toJson(status);
            }
            List<DataLabelInfo> dataLabelInfos = dataLabelInfoDAO.findByTag(train_label.trim());

            FileWriter file = new FileWriter(path);
            //写入该标签所有的微博到文件中
            for(int i=0;i<dataLabelInfos.size();i++)
            {
                DataLabelInfo info = dataLabelInfos.get(i);
                file.write(info.getKey().getSince_id()+'\t'+info.getWeibo_time()+'\t'+info.getWeibo_content()+'\t'+info.getKey().getTag()+'\n');
            }
            file.close();
            String type = "retrain";
            String model_dir = rootdir+model_path;
            String pyfile = rootdir+python_path+classfy_file;
            if(!train_all)
            {
                type="update";
            }
            String cmd = "python "+  pyfile + " "+type+" "+model_dir;
            exec_cmd(cmd);
            result=true;

        }catch (Exception e){
            e.printStackTrace();
            result=false;

        }finally {
            if (deletefile(path))
                logger.info("文件" + path + "删除成功!");
            else
                logger.info("文件" + path + "删除失败!");

        }
        status.put("status",result);
        return gson.toJson(status);
    }


}


