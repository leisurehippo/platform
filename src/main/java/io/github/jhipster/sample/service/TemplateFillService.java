package io.github.jhipster.sample.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import io.github.jhipster.sample.web.rest.util.FileUploadingUtil;

@Service
public class TemplateFillService {
	
	private final String PYTHON_PATH = "src/main/java/io/github/jhipster/sample/web/rest/python/templateFill/doc_transform.py";
	private static FileUploadingUtil fileUtil = new FileUploadingUtil();

	private boolean transform(String task_path){
		String[] pargs = new String[]{"python",PYTHON_PATH,task_path};
		
		
		return excute(pargs);
	}
	
	private boolean run(String task_path, boolean xianlu){
		ArrayList<String> pargs = new ArrayList<String>();
		pargs.add("python");
		pargs.add(PYTHON_PATH);
		pargs.add(" -o "+task_path);
		pargs.add(" -r " + task_path);
		if(xianlu){
			pargs.add(" -x ");
		}
		
		String[] args = (String[]) pargs.toArray();
		return excute(args);
	}
	
	public boolean runTask(String task_path, boolean xianlu){
		String txt_path = task_path + "/newdir";
		String docx_path = task_path + "/docx";
		init_path(txt_path);
		init_path(docx_path);
		
		boolean flag_t = transform(task_path);
		if(!flag_t){
			return flag_t;
		}
		return run(task_path,xianlu);
	}
	
	private void init_path(String path){
		File file = new File(path);
		if (file.exists()){
			fileUtil.deleteDirectory(path);
		}
		file.mkdir();
	}
	
	private boolean excute(String[] pargs) {
		try {
			Process process = Runtime.getRuntime().exec(pargs);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));  
	        String line;  
	
	        while ((line = in.readLine()) != null) {  
	        	System.out.println(line); //测试时使用，完成时删除
	
	        }  
	        in.close();  	        
			process.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
		return true;
	}
}
