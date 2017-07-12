package io.github.jhipster.sample.web.rest.model;

import org.apache.spark.ml.Model;
import org.apache.spark.ml.classification.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import io.github.jhipster.sample.web.rest.support.Classification;


public class SparkEstimate {

    public Model loadModel(String path, Classification classification) {
        Model model = null;
        if (classification == Classification.LR) model = LogisticRegressionModel.load(path);
        else if (classification == Classification.DT) model = DecisionTreeClassificationModel.load(path);
        else if (classification == Classification.NB) model = NaiveBayesModel.load(path);
        else if (classification == Classification.GBT) model = GBTClassificationModel.load(path);
        else if (classification == Classification.RF) model = RandomForestClassificationModel.load(path);
        else if (classification == Classification.mutPerception) model = MultilayerPerceptronClassificationModel.load(path);
        return model;
    }

    public Dataset<Row> predict(Dataset<Row> dataset, Model model) {
        Dataset<Row> results = model.transform(dataset);
        Dataset<Row> rows = results.select("features", "label", "probability", "prediction");
        for (Row r: rows.collectAsList()) {
            System.out.println("(" + r.get(0) + ", " + r.get(1) + ") -> prob=" + r.get(2) + ", prediction=" + r.get(3));
        }
        return rows;
    }

}
