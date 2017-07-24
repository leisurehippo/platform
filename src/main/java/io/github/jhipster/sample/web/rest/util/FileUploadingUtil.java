package io.github.jhipster.sample.web.rest.util;

/**
 * Created by WJ on 2017/4/18.
 */

import java.io.*;
import java.util.*;
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
     * 上传单个文件，并返回其在服务器中的存储路径
     *
     * @param aFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean uploadFile(MultipartFile aFile, String path) throws IOException {
        String filePath = initFilePath(aFile.getOriginalFilename(), path);
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
     * 返回文件存储路径，为防止重名文件被覆盖，在文件名称中增加了随机数
     * @param name
     * @return
     */
    private static String initFilePath(String name, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getPath() + "/" + name).replaceAll(" ", "-");
    }

    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public boolean createFile(String filePath, String fileName,String filecontent){
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        Boolean bool = false;
        File file = new File(filePath + fileName);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                System.out.println("success create file");
                //创建文件成功后，写入内容到文件里
                writeFileContent(filePath + fileName, filecontent);
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

    /**
     * 列出指定目录下文件夹列表
     * @param path
     * @return
     */
    public List<String> listDir(String path, boolean isDir){
        List<String> results = new ArrayList<String>();
        File file = new File(path);
        if (file.exists()) {
            for (File file2 : file.listFiles()) {
                boolean flag = isDir ^ file2.isDirectory();
                if (!flag) {
                    String [] arrList = file2.getAbsolutePath().split("\\\\");
                    results.add(arrList[arrList.length-1]);
                }
            }
        }
        return results;
    }


    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param dirPath
     * @return
     */
    public boolean deleteDirectory(String dirPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();// 获得传入路径下的所有文件
        for (File file : files) {
            if (file.isFile()) {// 删除子文件
                flag = deleteFile(file.getAbsolutePath());
                System.out.println(file.getAbsolutePath() + " 删除成功");
                if (!flag)
                    break;// 如果删除失败，则跳出
            } else {// 运用递归，删除子目录
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag)
                    break;// 如果删除失败，则跳出
            }
        }
        return flag && dirFile.delete();
    }

    /**
     * 删除单个文件
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {// 路径为文件且不为空则进行删除
            file.delete();// 文件删除
            flag = true;
        }
        return flag;
    }
}
