package io.github.jhipster.sample.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class ETLConfig {
	
	public static final String DATAXURL = "/root/datax";//server
	//public static final String DATAXURL = "C:/代码相关/平台/datax"; //local
	public static final String DATAX_TEXT_URL = DATAXURL + "/bin/textfiles";
	public static final String DATAX_TEXT_RES = DATAX_TEXT_URL + "/result.txt";
	
}

