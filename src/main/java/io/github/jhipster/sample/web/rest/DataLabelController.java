package io.github.jhipster.sample.web.rest;

/**
 * Created by HB on 2017/7/10.
 */

import com.google.gson.Gson;
import io.github.jhipster.sample.web.rest.model.*;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        //0：成功    -1：失败    1：成功但是到达了最后一页    -2:新建标签出现重复
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
    private static final String rootdir="./src/main/java/io/github/jhipster/sample/web/rest/";
    private static final String temp_path="/temp/";
    private static final String python_path="/python/";
    private static final String model_path="/label_model/";
    private static final String classfy_file="xgb_api.py";
    private static final String search_file="get_sina_weibo.py";
    private static final String img_path="./src/main/webapp/";
    private static final int num_per_page = 500; //每次返回至少num_per_page条数据
    private static final int num_per_search = 500;  //每次向数据库查询num_per_search条数据


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
            String clabel = all_labels.get(i).getChild_label();
            String plabel = all_labels.get(i).getParent_label();
            boolean isroot = false;
            if(plabel.trim().length()<=0)
                isroot=true;

            labelInitResponse.all_label.put(clabel,isroot);
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
            if(!labelRelationInfoDAO.exists(oldlabel))//收到了一个不存在的已有标签，忽略
            {
                dataLabelResponse.success_count =  0;
                dataLabelResponse.response_code = 0;
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
        }

        //检查该标签关系是否存在
        String par=labelRelationInfoDAO.findParent(tag.substring(1));
        boolean isexist=true;
        if(par!=null && !par.trim().equals(_parent))//新建标签重复
        {
            dataLabelResponse.success_count = count;
            dataLabelResponse.response_code = -2;
            Gson gson = new Gson();
            return gson.toJson(dataLabelResponse);
        }
        if(par==null)
        {
            isexist=false;
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
        for (int i = 0; i < labelresult.size(); i++) {
            //提交的的参数里只有id没有微博内容，去原始数据库里查询，这样可以避免在内存里保存微博内容，并且可以减少前端传参的量
            DataInfo _data = dataInfoDAO.findBySince_id(dbname, labelresult.get(i));
            //除了标签本身，每次还需要将该标签所有的父标签都加入
            try {
                dataLabelInfoDAO.save(new DataLabelInfo(new LabelDataSetKey(labelresult.get(i), tag), _data.getWeibo_time(), _data.getWeibo_content()));
                count++;
                //保证至少写入一条记录后，才加上标签关系
                if(!isexist)
                    labelRelationInfoDAO.save(new LabelRelationInfo(_parent,_child));
               List<DataLabelInfo> infos= save_all_parents(tag.substring(1),childtoparent,parenttochild,_data);
               dataLabelInfoDAO.save(infos);
            } catch (Exception e) {
                e.printStackTrace();
                dataLabelResponse.success_count = count;
                dataLabelResponse.response_code = -1;
                Gson gson = new Gson();
                return gson.toJson(dataLabelResponse);
            }
            logger.info(_data.getWeibo_content() + "----------" + tag);

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
    public List<String> get_all_child(String root,Map<String,List<String>> parenttochild)
    {
        Stack all_child = new Stack();
        List<String> children=new ArrayList<>();
        all_child.push(root);
        List<String> childlist = parenttochild.get(root);
        while(childlist!=null)
        {
            for(int i=0;i<childlist.size();i++)
            {
                all_child.push(childlist.get(i));
            }
            String top_label= (String) all_child.peek();
            List<String> top_label_child = parenttochild.get(top_label);
            if(top_label_child.size()<=0)//栈顶元素时叶子结点
            {
                children.add((String) all_child.pop());//访问该叶子结点并弹出
            }
            else
            {
                if(parenttochild.get(top_label).contains(children.get(children.size()-1)))//栈顶元素不是叶子结点，但是上一个访问的是它的孩子，现在就该访问它了
                {
                    children.add((String) all_child.pop());
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
            child_label = labelRelationInfos.get(i).getChild_label();
            parent_label = labelRelationInfos.get(i).getParent_label();
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
    public List<DataLabelInfo> save_parent(String par,String childroot,Map<String,List<String>> parenttochild) {
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
     * 标签迁移
     */
    @GetMapping("/Labelchangeservice")
    @ResponseBody
    public String change_labels( String parent,String childroot)
    {

        Gson gson = new Gson();
        return gson.toJson(true);
    }

    /**
     * 标签删除
     */
    @GetMapping("/Labeldelservice")
    @ResponseBody
    public String delete_labels(String label,boolean delete_child)
    {
        Gson gson = new Gson();
        return gson.toJson(true);
    }

    /**
     * 标签汇聚
     */
    @GetMapping("/Labelmergeservice")
    @ResponseBody
    public String merge_labels(@RequestParam("merged_labels") String[] merged_labels,@RequestParam("merge_label") String merge_label) throws Exception {
        merge_label=merge_label.trim();
        String p =labelRelationInfoDAO.findParent(merge_label);
        //汇聚数量必须大于2个，汇聚标签不能为空，汇聚标签要么不存在，要么只能为root标签
        if(merged_labels.length<2 || merge_label.length()<=0|| (p!=null&&p.length()>0) )
        {
            Gson gson = new Gson();
            return gson.toJson(false);
        }
        Map<String, Map> relation = get_label_relation();
        Map<String,String> childtoparent = relation.get("childtoparent");
        Map<String,List<String>>parenttochild= relation.get("parenttochild");
        List<DataLabelInfo> dataLabelInfos = new ArrayList<DataLabelInfo>();
        labelRelationInfoDAO.save(new LabelRelationInfo("",merge_label));


        for(int i=0;i<merged_labels.length;i++)
        {
            labelRelationInfoDAO.save(new LabelRelationInfo(merge_label,merged_labels[i].trim()));
            dataLabelInfoDAO.save(save_all_parents(merged_labels[i],childtoparent,parenttochild,null));
        }


        Gson gson = new Gson();
        return gson.toJson(true);

    }

    /**
     * 建立标签树，并做成图片保存
     */
    public String get_label_tree()
    {
        Gson gson = new Gson();
        return gson.toJson(false);
    }


}


