package io.github.jhipster.sample.web.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
public class SparkTest {


	
	
	public void testOpt() throws JSONException{
		String a = "1.0Es-6";
		JSONObject paramPair = new JSONObject();
		paramPair.put("a", a);
		System.out.println(Double.isNaN(paramPair.optDouble("a")));
	}

	@Test
	public void test(){
		String dataName = "asss.tx,t";
		for(String s : dataName.split("\\.")){
			System.out.println(s);
		}

	}

	public void testLoadData() throws Exception{
	
	}
}
