package com.bisaibang.monojwt.service.util;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by shadow on 2015/11/28.
 *
 */
public class QiniuUtil {

    private final Logger log = LoggerFactory.getLogger(QiniuUtil.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    static String ACCESS_KEY = "MNQ_RpES0nxyOv1HhtpmyNuEqmPcu_NWkAHPyi5-";
    static String SECRET_KEY = "kCxkMIFin2e58NB6-9PC17rep_n6IuJ1zQf7RpXP";
    //要上传的空间
    String bucketName;
    //上传到七牛后保存的文件名
    String key;
    //上传文件的路径
    File file;

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFile(File file) {
        this.file = file;
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(String fileName){
        StringMap policy = new StringMap();
        policy.put("scope",bucketName+":"+fileName);
        policy.put("mimeLimit","image/*");
        return auth.uploadToken(bucketName,key,120, policy, true);
    }

    public void upload() throws IOException{
        //创建上传对象
        UploadManager uploadManager = new UploadManager();
        try {
            log.debug(file.getAbsolutePath());
            //调用put方法上传
            Response res = uploadManager.put(file, key, getUpToken(file.getName()));
            //打印返回的信息
            log.debug(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            log.debug(r.toString());

            try {
                //响应的文本信息
                log.debug(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
    public String download(){
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String URL = "http://omhemfx8a.bkt.clouddn.com/"+key;
        return auth.privateDownloadUrl(URL,0);
    }
    public String delete(){
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth);
        //要测试的空间和key，并且这个key在你空间中存在
        try {
            //调用delete方法移动文件
            bucketManager.delete(bucketName, key);
        } catch (QiniuException e) {
            //捕获异常信息
            return e.response.toString();
        }
        return "";
    }
}
