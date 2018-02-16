package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            Sandwich sandwich = new Sandwich();
            sandwich.setMainName(obj.getJSONObject("name").getString("mainName"));
            sandwich.setAlsoKnownAs(jsonArrayToList(obj.getJSONObject("name").getJSONArray("alsoKnownAs")));
            sandwich.setPlaceOfOrigin(obj.getString("placeOfOrigin"));
            sandwich.setDescription(obj.getString("description"));
            sandwich.setImage(obj.getString("image"));
            sandwich.setIngredients(jsonArrayToList(obj.getJSONArray("ingredients")));
            return sandwich;
        } catch (Exception e) {
            Log.i("LogTag","Error in JsonUtils.parseSandwichJson: "+e.getLocalizedMessage());
            return null;
        }
    }
    private static List<String> jsonArrayToList(JSONArray aJSONArray) {
        try {
            ArrayList<String> arrayList = null;
            if (aJSONArray != null) {
                arrayList = new ArrayList<>();
                for (int i = 0;i<aJSONArray.length();i++) {
                    arrayList.add(aJSONArray.getString(i));
                }
            }
            return arrayList;
        } catch (Exception e) {
            Log.i("LogTag","Error in JsonUtils.parseSandwichJson: "+e.getLocalizedMessage());
            return null;
        }
    }
}
