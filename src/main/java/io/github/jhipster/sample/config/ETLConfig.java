package io.github.jhipster.sample.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class ETLConfig {
	
	//public static final String DATAXURL = "/home/hadoop/platform/datax";//server
	public static final String DATAXURL = "C:/代码相关/平台/datax"; //local
	public static final String DATAX_TEXT_URL = DATAXURL + "/bin/textfiles";
	public static final String DATAX_TEXT_RES = DATAX_TEXT_URL + "/result.txt";
	
	public static void main(String args[]) throws IOException{
        File file = new File(DATAX_TEXT_RES);
        file.createNewFile();
		FileWriter f = new FileWriter(file);
		f.write("result");
		f.close();
		System.out.print(DATAX_TEXT_URL);
	}
}

