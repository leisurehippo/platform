package custom_model;

import org.apache.spark.ml.Model;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructType;

public class TestKmeansModel extends Model<TestKmeansModel>{

	@Override
	public String uid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestKmeansModel copy(ParamMap arg0) {
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
