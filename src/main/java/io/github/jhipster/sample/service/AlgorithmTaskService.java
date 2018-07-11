package io.github.jhipster.sample.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import io.github.jhipster.sample.service.util.AlgorithmTask;
import io.github.jhipster.sample.service.util.FileUtil;
import io.github.jhipster.sample.web.rest.util.HDFSFileUtil;

@Service
public class AlgorithmTaskService {

	// local project path
    private static String ProjectPathPrefix = "src/main/webappfiles/Project/";
    private static FileUtil fileUtil = new FileUtil();
    private HDFSFileUtil hdfsFileUtil = new HDFSFileUtil();
	//valid project name
    private static String [] VALIDPRO = {"elec", "test"}; 
    
    public boolean checkProject(String project){
    	for(String name : VALIDPRO){
    		if(name.equals(project)){
    			return true;
    		}
    	}
    	return false;
    }
    
	public boolean addTask(String project, String task, String desc) throws IllegalArgumentException, IOException{
		String task_path = ProjectPathPrefix + project + '/' +task;
	    String hdfsPrefix = hdfsFileUtil.root() + project + '/' + task;
	    
        if (! fileUtil.checkFile(task_path)) {
            String algo_path = task_path + '/' + "algo";            
            fileUtil.mkdirs(algo_path);
            hdfsFileUtil.mkdir(hdfsPrefix + '/' + "algo");
            
            String data_path = task_path + '/' + "data";
            fileUtil.mkdirs(data_path);
            hdfsFileUtil.mkdir(hdfsPrefix + '/' + "data");
            
            String para_path = task_path + '/' + "para";
            fileUtil.mkdirs(para_path);
            hdfsFileUtil.mkdir(hdfsPrefix + '/' + "para");
            
            String res_path = task_path + '/' + "result";
            fileUtil.mkdirs(res_path);
            hdfsFileUtil.mkdir(hdfsPrefix + '/' + "result");
            
            String model_path = task_path + '/' + "model";
            fileUtil.mkdirs(model_path);
            hdfsFileUtil.mkdir(hdfsPrefix + '/' + "model");
            
            AlgorithmTask task_info = new AlgorithmTask(task,desc);
 	        fileUtil.saveToFile(task_path +'/'+"log.txt", task_info.toString());
            return true;
        }
		return false;
	}
	
	public boolean delTask(String project, String task){
		String task_path = ProjectPathPrefix + project + '/' +task;
		boolean flag = false;
		try {
			flag = hdfsFileUtil.delFile(hdfsFileUtil.root() + project + "/" + task, true);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileUtil.deleteDirectory(task_path) && flag;
	}
	
	public AlgorithmTask getTask(String project, String taskName){
		String task_path = ProjectPathPrefix + project + '/' +taskName;
		String log_path = task_path + "/log.txt";
		AlgorithmTask task = null;
	    try{
		   BufferedReader reader =new BufferedReader(new FileReader(log_path));
		   String taskinfo = reader.readLine();
		   task = new AlgorithmTask(taskinfo);
		   reader.close();

	    }catch (Exception e) {
		   e.printStackTrace();
	    }
	    return task;
	}
	
	public List<AlgorithmTask> getAllTasks(String project){
		List<AlgorithmTask> tasks = new ArrayList<AlgorithmTask>();
		String root_path = ProjectPathPrefix + project;
		List<String> task_names =  fileUtil.list_dirs(root_path);
		for(String task_name: task_names){
			tasks.add(getTask(project, task_name));
		}
		return tasks;
	}
	
	public boolean editTask(String project, String taskName, String desc) throws IOException{
		String task_path = ProjectPathPrefix + project + '/' +taskName;
		String log_path = task_path + "/log.txt";
		AlgorithmTask task = getTask(project, taskName);
		if(task == null){
			return false;
		}
		task.setDesc(desc);
		fileUtil.saveToFile(log_path, task.toString());
		return true;
		
	}

	public List<AlgorithmTask> searchTaskByTime(String project, String beginTime, String endTime){
		List<AlgorithmTask> tasks = getAllTasks(project);
		LocalDate begin = LocalDate.parse(beginTime);
		LocalDate end = LocalDate.parse(endTime);
		List<AlgorithmTask> res = new ArrayList<AlgorithmTask>();
		for(AlgorithmTask task : tasks){
			   if(task.getDate().compareTo(begin)>=0 && task.getDate().compareTo(end)<=0){
				   res.add(task);
			   }
		}
		return res;
	}
	
	public List<AlgorithmTask> searchTaskByName(String project, String task_name){
		List<AlgorithmTask> tasks = getAllTasks(project);
		List<AlgorithmTask> res = new ArrayList<AlgorithmTask>();
		for(AlgorithmTask task : tasks){
		   if(task.getTask_name().contains(task_name)){
			   res.add(task);
		   }
		}
		return res;
	}
	

}
