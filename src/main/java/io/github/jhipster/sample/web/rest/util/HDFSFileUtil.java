package io.github.jhipster.sample.web.rest.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class HDFSFileUtil {

    private static FileSystem fs;
    private static String hadoop_master;
    private static String platform_root;
    static {
        try{
            Properties properties = new Properties();
            properties.load(HDFSFileUtil.class.getResourceAsStream("/cluster.properties"));
            hadoop_master = "hdfs://" + properties.getProperty("hadoop_master_ip") + ":" + properties.getProperty("hadoop_port");
            fs = FileSystem.get(new URI(hadoop_master), new Configuration(), "hadoop");
            platform_root = properties.getProperty("data_platform_root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String master(){
    	return hadoop_master;
    }
    public String root(){
    	return hadoop_master + platform_root;
    }
    
    public HDFSFileUtil() {
		// TODO Auto-generated constructor stub
	}
    
    public String HDFSPath(String path) {
        return hadoop_master + platform_root + path;
    }

    public void upload(String sourcePath, String targetPath, boolean isRemove) throws Exception{
        fs.copyFromLocalFile(isRemove, new Path(sourcePath), new Path(targetPath));
    }

    public boolean mkdir(String path) throws IllegalArgumentException, IOException{
        return fs.mkdirs(new Path(path));
    }

    public void download(String sourcePath ,String targetPath, boolean isRemove) throws IllegalArgumentException, IOException{
        fs.copyToLocalFile(isRemove, new Path(sourcePath), new Path(targetPath), true);
    }

    public boolean delFile(String path, boolean isDir) throws IllegalArgumentException, IOException{
        System.out.println(path);
        if(isDir)
            return fs.delete(new Path(path),true);
        else
            return fs.deleteOnExit(new Path(path));
    }

    public boolean rename(String sourcePath, String targetPath) throws IllegalArgumentException, IOException{
        return fs.rename(new Path(sourcePath),new Path(targetPath));
    }

    public boolean checkFile(String path) throws IllegalArgumentException, IOException{
        return fs.exists(new Path(path));
    }

    public ArrayList fileLoc(String path) throws IllegalArgumentException, IOException{
        FileStatus filestatus = fs.getFileStatus(new Path(path));
        BlockLocation[] blkLocations=fs.getFileBlockLocations(filestatus, 0, filestatus.getLen());
        ArrayList<String> arrayList = new ArrayList<String>();
        for(BlockLocation blkLocation: blkLocations){
            String[] hosts = blkLocation.getHosts();
            arrayList.add(hosts[0]);
        }
        return arrayList;
    }

    public long getSizeK(String path) throws Exception{
        long DataSize = fs.getFileStatus(new Path(path)).getLen();
        return DataSize/1024;
    }

    public List<String> list(String folder) throws IOException {
        Path path = new Path(folder);
        FileStatus[] list = fs.listStatus(path);
        List<String> nameList = new ArrayList<String>();
        for (FileStatus f : list) {
            String [] filePath = f.getPath().toString().split("/");
            nameList.add(filePath[filePath.length-1]);
        }
        return nameList;
    }
    

    
}
