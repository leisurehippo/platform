package io.github.jhipster.sample.service.util;
import java.time.LocalDate;

/**
 * Base class of Template Fill module
 * the task info will be saved in the file system, not db.
 * And may changed to db sometime
 * @author zzs
 *
 */
public class TemplateFillTask {
	
	private String taskname;
	private String username;
	private String type;
	private LocalDate date;
	public String[] validType = {"线路","变压器","开关","其他"};
	
	public TemplateFillTask(String taskname, String username, String type){
		this.taskname = taskname;
		this.username = username;
		this.type = type;
		this.date = LocalDate.now();
	}
	
	public TemplateFillTask(String taskinfo){
		String[] info = taskinfo.split(" ");
		this.taskname = info[0];
		this.type = info[1];
		this.username = info[2];
		this.date = LocalDate.parse(info[3]);
	}
	
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String toString(){
		return this.taskname +" "+this.type+" "+this.username+
				" "+this.date.toString();
	}
	
	public boolean is_valid(){
		for(String type: this.validType){
			if (type.equals(this.type)){
				return true;
			}
		}
		return false;	
	}
	
	public static void main(String args[]){
		TemplateFillTask task = new TemplateFillTask("test", "root", "a1");
		LocalDate date = LocalDate.parse("2018-04-21");
		System.out.println(date.toString());
	}

}
