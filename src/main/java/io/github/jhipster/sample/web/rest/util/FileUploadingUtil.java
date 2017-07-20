package io.github.jhipster.sample.web.rest.util;

/**
 * Created by WJ on 2017/4/18.
 */

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import io.github.jhipster.sample.web.rest.AccountResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 *
 * @author Chris Mao(Zibing)
 *
 */
public class FileUploadingUtil {

    private final static Logger logger = LoggerFactory.getLogger(AccountResource.class);
    /**
     * 服务器上的保存路径，在使用到上传功能的Controller中对其进行赋值
     */
    public static String FILEDIR = "src\\main\\webappfiles\\";


    /**
     * 上传单个文件，并返回其在服务器中的存储路径
     *
     * @param aFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean uploadFile(MultipartFile aFile, String path) throws IOException {
        String filePath = initFilePath(aFile.getOriginalFilename(), path);
        System.out.println(filePath);
        try {
            write(aFile.getInputStream(), new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            logger.error("上传的文件: " + aFile.getName() + " 不存在！！");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 写入数据
     *
     * @param in
     * @param out
     * @throws IOException
     */
    private static void write(InputStream in, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * 遍历服务器目录，列举出目录中的所有文件（含子目录）
     * @return
     */
    public static Map<String, String> getFileMap() {
        logger.info(FileUploadingUtil.FILEDIR);
        Map<String, String> map = new HashMap<String, String>();
        File[] files = new File(FileUploadingUtil.FILEDIR).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    File[] files2 = file.listFiles();
                    if (files2 != null) {
                        for (File file2 : files2) {
                            String name = file2.getName();
                            logger.info(file2.getParentFile().getAbsolutePath());
                            logger.info(file2.getAbsolutePath());
                            map.put(file2.getParentFile().getName() + "/" + name,
                                name.substring(name.lastIndexOf("_") + 1));
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 返回文件存储路径，为防止重名文件被覆盖，在文件名称中增加了随机数
     * @param name
     * @return
     */
    private static String initFilePath(String name, String type) {
        System.out.println(FILEDIR + type);
        File file = new File(FILEDIR + type);
        if (!file.exists()) {
            System.out.println("ok");
            file.mkdirs();
        }
        return (file.getPath() + "\\" + name).replaceAll(" ", "-");
    }

    /**
     *
     * @param name
     * @return
     */
    private static int getFileDir(String name) {
        return name.hashCode() & 0xf;
    }


    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public boolean createFile(String fileName,String filecontent){
        Boolean bool = false;
        File file = new File(fileName);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file");
                //创建文件成功后，写入内容到文件里
                writeFileContent(fileName, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
}
