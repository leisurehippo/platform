package io.github.jhipster.sample.service.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Transform {
	private List<TransformItem> items;
	
	public Transform(List<TransformItem> items) {
		this.items = items;
	}
	
	public Transform(){
		this.items = new ArrayList<TransformItem>();
	}
	
	public void add(TransformItem item){
		items.add(item);
	}
	
	public String toString(){
		JSONArray res = new JSONArray();
		try{
			for(TransformItem item : items){
				JSONObject json = new JSONObject(item.toString());
				res.put(json);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
	}
	
	public static void main(String []args){
		List<String> paras = new ArrayList<String>();
		paras.add("1");
		paras.add("3");
		String name = "dx_substr";
		TransformItem item = new TransformItem(name, 0, paras);
		TransformItem item1 = new TransformItem(name, 0, paras);
		TransformItem item2 = new TransformItem(name, 0, paras);
		Transform transform = new Transform();
		transform.add(item);
		transform.add(item1);
		transform.add(item2);
		System.out.println(transform.toString());
		
		
	}
}
