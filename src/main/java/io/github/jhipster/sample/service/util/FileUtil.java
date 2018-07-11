package io.github.jhipster.sample.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import io.github.jhipster.sample.web.rest.AccountResource;
import org.apache.commons.io.FileUtils;


public class FileUtil {
    private final static Logger logger = LoggerFactory.getLogger(AccountResource.class);

	public static String ProjectPathPrefix;
	static{
		Properties properties = new Properties();
	    try {
			properties.load(FileUtil.class.getResourceAsStream("/path.properties"));
		    ProjectPathPrefix = properties.getProperty("local_project_path");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    		
	public  void saveToFile(String filePath, JSONObject resJson){
		File file = new File(filePath);
		System.out.println(filePath);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsolutePath(),false);
			fw.write(resJson.toString());
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 将文件类内容转存至另一文件中
	public  void saveToFile(String filePath, File f) throws IOException{
		    FileInputStream is = new FileInputStream(f);
	        InputStreamReader ir = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(ir);
	        
	        File output = new File(filePath);
	        if(!output.exists()){
				output.createNewFile();
			}
	        FileWriter fw = new FileWriter(output);
	        String data = null;
	        while((data = br.readLine())!=null){
	        	fw.write(data);
	        	fw.write("\n");
	        }
	        fw.close();
	        br.close();
	}
	
	public  void saveToFile(String filePath, String content){
		File file = new File(filePath);
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsolutePath(),false);
			fw.write(content);
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  boolean checkFile(String filePath){
		try{
			File file = new File(filePath);

			if (file.isFile() && file.exists())
	        { // 判断文件是否存在
	            return true;
	        }
	        else
	        {
	            System.out.println("找不到指定的文件");
	            return false;
	        }
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public  boolean removeFile(String filePath){
		try{
			File file = new File(filePath);
			if(checkFile(filePath)){
				return file.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
  public  List<String> list_dirs(String root_path){
		ArrayList<String> dirs = new ArrayList<String>();	
		File file = new File(root_path);
		if(file.exists()){
			for(File file2 : file.listFiles()){
				if(file2.isDirectory()){
					String dir_name = file2.getName();
					dirs.add(dir_name);
				}
			}
		}

		return dirs;
   }
	  
  public  List<String> list_files(String root_path){
		ArrayList<String> files = new ArrayList<String>();	
		File file = new File(root_path);
		if(file.exists()){
			for(File file2 : file.listFiles()){
				if(file2.isFile()){
					String file_name = file2.getName();
					files.add(file_name);
				}
			}
		}

		return files;
   }
   
  /**
   * 递归创建目录
   * @param path
   * @return
   */
  public boolean mkdirs(String path){
      File file = new File(path);
      return file.mkdirs();
  }
   
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
  private void write(InputStream in, OutputStream out) throws IOException {
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
   * 返回文件存储路径
   * @param name
   * @return
   */
  private String initFilePath(String name, String path) {
      File file = new File(path);
      if (!file.exists()) {
          file.mkdirs();
      }
      return (file.getPath() + "/" + name).replaceAll(" ", "-");
  }
  
  /**
   * 删除目录（文件夹）以及目录下的文件
   * @param dirPath
   * @return
   */
  public boolean deleteDirectory(String dirPath) {
      // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
	  /*
      File dirFile = new File(dirPath);
      // 如果dir对应的文件不存在，或者不是一个目录，则退出
      if(dirFile.isDirectory()){
	      File[] files = dirFile.listFiles();// 获得传入路径下的所有文件
	      for (File file : files) {
	    	  boolean success = deleteDirectory(file.getAbsolutePath());
	      }
      }
      return dirFile.delete();
      */
		try {
			FileUtils.deleteDirectory(new File(dirPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	  return true;
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
