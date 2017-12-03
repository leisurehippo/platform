package io.github.jhipster.sample.web.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.sample.service.ETLService;

@RestController
@RequestMapping("/etl")
public class ETLController {
		
	
	@PostMapping("/runJob")
	@ResponseBody
	public String runJob(@RequestBody Map<String, Object> data) throws JSONException{
		ETLService service = new ETLService();
		String job = service.getJob(data);
		JSONObject res = new JSONObject();
		if(service.runJob(job)){
			res.put("message", "success");
		}
		else{
			res.put("message", "fail");	
		}
		return res.toString();
	}
	@PostMapping("/tables")
	@ResponseBody
	public String getTables(@RequestBody Map<String, Object> data){
		ETLService service = new ETLService();
		List<String> tables = service.getTables(data);
		
		JSONArray array = new JSONArray(tables);
		return array.toString();
	}
	@PostMapping("/columns")
	@ResponseBody
	public String getColumns(@RequestBody Map<String, Object> data){
		ETLService service = new ETLService();

		List<String> tables = service.getColumns(data);
		
		JSONArray array = new JSONArray(tables);
		return array.toString();
	}
	@PostMapping("/checkConn")
	@ResponseBody
	public String checkConnection(@RequestBody Map<String, Object> data){
		ETLService service = new ETLService();
		if(service.checkConn(data)){
			return "success";
		}
		return "fail";
	}

}
