package io.github.jhipster.sample.service.util;

import java.time.LocalDate;

import org.json.JSONException;
import org.json.JSONObject;

public class AlgorithmTask {
	private String task_name;
	private LocalDate date;
	private String desc;
	
	public AlgorithmTask(String task_name, String desc){
		this.task_name = task_name;
		this.desc = desc;
		this.date = LocalDate.now();
	}
	
	public AlgorithmTask(String json_info){
		try {
			JSONObject json_task = new JSONObject(json_info);
			this.task_name = json_task.getString("task_name");
			this.desc = json_task.getString("desc");
			this.date = LocalDate.parse(json_task.getString("date"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	public String toString(){
		JSONObject res = new JSONObject();
		try {
			res.put("task_name", task_name);
			res.put("date", date);
			res.put("desc", desc);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res.toString();
	}
	

}
