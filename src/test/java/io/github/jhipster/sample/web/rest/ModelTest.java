package io.github.jhipster.sample.web.rest;
import java.io.IOException;

import org.apache.spark.ml.Model;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.util.MLWritable;
import org.apache.spark.ml.util.MLWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;

public class ModelTest extends Model<ModelTest> implements MLWritable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void save(String arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MLWriter write() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelTest copy(ParamMap arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dataset<Row> transform(Dataset<?> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StructType transformSchema(StructType arg0) {
		// TODO Auto-generated method stub
		return null;
	}



}
