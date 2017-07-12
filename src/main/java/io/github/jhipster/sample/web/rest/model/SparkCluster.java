package io.github.jhipster.sample.web.rest.model;

import org.apache.spark.ml.clustering.*;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SparkCluster {

    public Vector[] kmeans(JSONObject paramPair, Dataset dataSet) throws IOException, JSONException {
        KMeans kmeans = new KMeans()
                .setK(paramPair.getInt("K"))
                .setSeed(paramPair.getLong("seed"))
                .setInitSteps(paramPair.getInt("initSteps"))
                .setTol(paramPair.getDouble("tol"));
        KMeansModel kMeansModel = kmeans.fit(dataSet);
        Vector[] centers = kMeansModel.clusterCenters();
        return centers;
    }
    public Dataset<Row> lda(JSONObject paramPair, Dataset dataSet) throws IOException, JSONException {
        LDA lda = new LDA();
        LDAModel ldaModel = lda.fit(dataSet);
        Dataset<Row> topics = ldaModel.describeTopics();
        return topics;
    }

    public Vector[] b_kmeans(JSONObject paramPair, Dataset dataSet) throws IOException, JSONException {
        BisectingKMeans bisectingKMeans = new BisectingKMeans();
        BisectingKMeansModel bisectingKMeansModel = bisectingKMeans.fit(dataSet);
        Vector[] centers = bisectingKMeansModel.clusterCenters();
        return centers;
    }

    public GaussianMixtureModel gmm(JSONObject paramPair, Dataset dataSet) throws IOException, JSONException {
        GaussianMixture gmm = new GaussianMixture();
        GaussianMixtureModel gaussianMixtureModel = gmm.fit(dataSet);
        return gaussianMixtureModel;
    }

}
