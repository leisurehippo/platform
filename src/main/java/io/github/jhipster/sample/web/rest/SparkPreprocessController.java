package io.github.jhipster.sample.web.rest;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.sample.service.AlgorithmPrepreocessService;

@RestController
@RequestMapping("/preprocess")
public class SparkPreprocessController {
	private AlgorithmPrepreocessService service =  new AlgorithmPrepreocessService();
	
	
    @GetMapping("/preview")
 	@ResponseBody
 	public String preview(String project, String task, String dataType,
			String fileName, boolean hdfs, String delimiter,boolean header) throws IOException, JSONException{
		JSONObject res = new JSONObject();
		res = service.previewFile(project, task, dataType, fileName, hdfs, delimiter, header);
		return res.toString();

    }
    
    @GetMapping("/transform")
    @ResponseBody
    public String transform(String project, String task, String dataType,
			String fileName, boolean hdfs, String delimiter, boolean header, String outType,
			String colMaps, String outFileName) throws IllegalArgumentException, JSONException, IOException{
		JSONObject res = new JSONObject();
		res = service.transform(project, task, dataType, fileName, hdfs, 
				delimiter, header, outType, colMaps, outFileName);
		return res.toString();

    }
    
    @GetMapping("/supported")
    @ResponseBody
    public String supported() throws JSONException{
		JSONObject res = new JSONObject();
		res.put("supported", service.supported());		
		return res.toString();

    }
    
}
