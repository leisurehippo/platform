package io.github.jhipster.sample.web.rest;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Test;

import io.github.jhipster.sample.web.rest.util.SparkUtil;

public class test {

	@Test
	public void test_hello() throws Exception{
		System.out.println("test");
		SparkUtil sparkUtil = new SparkUtil();
		String path = "elec/testHDFS/data/kmeans_data.txt";
		Dataset<Row> data = sparkUtil.readFromHDFS(path, "text");
		data.show();
	}
}
