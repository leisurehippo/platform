package io.github.jhipster.sample.web.rest.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

    public JSONObject jsonRead(String jsonObjStr) {
        try {
            return new JSONObject(jsonObjStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
