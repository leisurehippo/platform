package io.github.jhipster.sample.web.rest.model;

import org.apache.spark.ml.Model;
import org.apache.spark.ml.clustering.*;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 *  KMEANS,
    LDA,
    B_KMEANS,
    GMM;
 * @author zzs
 *
 */
public class SparkCluster extends SparkModel{

    public Model kmeans(JSONObject paramPair, Dataset dataSet, String path) throws IOException, JSONException {
        KMeans kmeans = new KMeans();
        injectPara(paramPair, kmeans);
        KMeansModel kMeansModel = kmeans.fit(dataSet);
        //Vector[] centers = kMeansModel.clusterCenters();
        kMeansModel.save(path);
        return kMeansModel;
    }
    public Model lda(JSONObject paramPair, Dataset dataSet, String path) throws IOException, JSONException {
        LDA lda = new LDA();
        injectPara(paramPair, lda);        
        LDAModel ldaModel = lda.fit(dataSet);
        ldaModel.save(path);
        return ldaModel;
    }

    public Model b_kmeans(JSONObject paramPair, Dataset dataSet, String path) throws IOException, JSONException {
        BisectingKMeans bisectingKMeans = new BisectingKMeans();
        injectPara(paramPair, bisectingKMeans);                
        BisectingKMeansModel bisectingKMeansModel = bisectingKMeans.fit(dataSet);
        bisectingKMeansModel.save(path);
        return bisectingKMeansModel;
    }

    public Model gmm(JSONObject paramPair, Dataset dataSet, String path) throws IOException, JSONException {
        GaussianMixture gmm = new GaussianMixture();
        injectPara(paramPair, gmm);                        
        GaussianMixtureModel gaussianMixtureModel = gmm.fit(dataSet);
        gaussianMixtureModel.save(path);
        return gaussianMixtureModel;
    }
    
	@Override
	public Model FitModel(JSONObject paramPair, Dataset dataSet, String savePath, String algorithm) {
		try{
			switch(algorithm){
			    case "kmeans":	
			    	return kmeans(paramPair, dataSet, savePath);
			    case "b_kmeans":	
			    	return b_kmeans(paramPair, dataSet, savePath);
			    case "lda":	
			    	return lda(paramPair, dataSet, savePath);
			    case "gmm":	
			    	return gmm(paramPair, dataSet, savePath);
		        default:
		            break;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getParam(String algorithm) {
        String result = "";
		switch(algorithm){
		    case "kmeans":	
		        KMeans kmeans = new KMeans();
		        result = kmeans.explainParams();
		    	break;
		    case "b_kmeans":	
		        BisectingKMeans bisectingKMeans = new BisectingKMeans();
		        result = bisectingKMeans.explainParams();
		    	break;
		    case "lda":	
		        LDA lda = new LDA();
		        result = lda.explainParams();
		    	break;
		    case "gmm":	
		        GaussianMixture gmm = new GaussianMixture();
		        result = gmm.explainParams();
		    	break;		    	
            default:
                break;
		}
		return result;
	}

}
