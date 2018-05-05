package io.github.jhipster.sample.service.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Base class of ETL module
 * @author zzs
 *
 */
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
	
}
