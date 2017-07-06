package io.github.jhipster.sample.web.rest;

/**
 * Created by WJ on 2017/5/24.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.Py;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 算法管理类
 */
@RestController
@RequestMapping("/api")
public class Algorithm {


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
     * @param key
     * @return
     */
    @GetMapping("/runLocal")
    @ResponseBody
    public String test(@RequestParam(value = "AlgorithmName") String key,
                       @RequestParam(value = "hasParams") int hasParams,
                       @RequestParam(value = "Params") String params
                       ) {
        String result = "";
        try {

            String cmd = "python src\\main\\webappfiles\\Algorithm\\"+key+".py ";
            if (hasParams == 1){
                cmd += params;
            }
            Process pr = Runtime.getRuntime().exec(cmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            while ((line = in.readLine()) != null) {
                result += line + "\n";
//                System.out.println(line);
            }
            in.close();
            pr.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
