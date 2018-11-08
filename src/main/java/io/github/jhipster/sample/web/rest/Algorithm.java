package io.github.jhipster.sample.web.rest;

/**
 * Created by WJ on 2017/5/24.
 */

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 算法管理类
 */
@RestController
@RequestMapping("/api")
public class Algorithm {
    private static String ProjectPathPrefix = "src/main/webappfiles/Project/";


//    @GetMapping("/test")
//    @ResponseBody
//    public String runLocalAlgorithm(@RequestParam(value = "AlgorithmName") String key){
//        PythonInterpreter interpreter = new PythonInterpreter();
//
//        PySystemState sys = Py.getSystemState();
//        //sys.path.add("D:\\jython2.5.2\\Lib");
//        System.out.println(sys.path.toString());
//
//        interpreter.exec("print 'hello'");
//
//        interpreter.exec("import sys");
//        interpreter.exec("print(sys.path)");
//        interpreter.exec("sys.path.append('C:\\Python35\\lib\\site-packages')");
//        interpreter.exec("print(sys.path)");
//        interpreter.exec("import numpy");
//
//        interpreter.execfile("src\\main\\webappfiles\\PSOForARM\\pso.py");
//        return "ok"+key;
//    }


    /**
     * 运行本地代码
     * @param ProjectName
     * @param AlgorithmName
     * @param params
     * @param isPython
     * @return
     */
    @GetMapping("/runLocal")
    @ResponseBody
    public List<String> runLocal(@RequestParam(value = "ProjectName") String ProjectName,
                             @RequestParam(value = "AlgorithmName") String AlgorithmName,
                             @RequestParam(value = "Params") String params,//{"-f":"src/main/webappfiles/Project/ProjectName/Data/ss.csv","-s":0.1}
                             @RequestParam(value = "isPython") boolean isPython
                       ) {
        String result = "";
        List<String> results = new ArrayList<String>();
        try {
            String cmd = "";
            if (isPython) {
                cmd = "python " + ProjectPathPrefix + ProjectName + "/Algorithm/algorithm/" + AlgorithmName;
                JSONObject json_param = JSONObject.fromObject(params);
                Iterator param_iter = json_param.keys();
                while(param_iter.hasNext()){
                    String param_key = param_iter.next().toString();
                    String param_value = json_param.get(param_key).toString();
                    cmd += " " + param_key + " " + param_value;
                }
                System.out.println(cmd);

                Process pr = Runtime.getRuntime().exec(cmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line;

                while ((line = in.readLine()) != null) {
                    result += line + "\n";
                    results.add(line);
                }
                in.close();
                pr.waitFor();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
