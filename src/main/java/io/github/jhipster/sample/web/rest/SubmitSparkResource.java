package io.github.jhipster.sample.web.rest;

/**
 * Created by Gsy on 2017/5/27.
 */

import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class SubmitSparkResource {
    private static final Logger logger = LoggerFactory.getLogger(SubmitSparkResource.class);
//    动态执行spark-submit
    @GetMapping("/submit")
    @ResponseBody
    public ResponseEntity submitSpark() throws Exception  {
        Process spark = new SparkLauncher()
            .setAppResource("/my/app.jar")
            .setMainClass("my.spark.app.Main")
            .setMaster("local")
            .setConf(SparkLauncher.DRIVER_MEMORY, "2g")
            .launch();
        spark.waitFor();
        return new ResponseEntity<> ("SUCCESS", HttpStatus.OK);
    }

}
