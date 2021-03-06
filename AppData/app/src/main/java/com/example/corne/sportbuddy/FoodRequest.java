package com.example.corne.sportbuddy;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Context context;
    Callback activity;
    ArrayList<JSONObject> foodJSON = new ArrayList<>();
    ArrayList<String> food = new ArrayList<>();
    String input;

    // Constructor
    public FoodRequest(Context context, String input) {
        this.context = context;
        this.input = input;
    }

    public interface Callback {
        void gotFood(ArrayList<String> food);
        void gotFoodError(String message);
    }

    // Method that makes a queque and tries to add this request to it
    void getFood(Callback activity){
        this.activity = activity;
//        ToDo: do not hardcode this
        String url = "https://trackapi.nutritionix.com/v2/search/instant?query="+ input;

        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("x-app-id", "a39f9714");
                    params.put("x-app-key", "8bb9875979d2a2bf0adb3cdc00c6c14f");
                    params.put("x-remote-user-id", "0");
                    params.put("cache-control", "no-cache");
                    params.put("Postman-Token", "96cb6307-0f37-4f7e-a4d1-98ac42d6f23c");
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (Exception error) {
            Log.e("Developer", error.getMessage());
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        // Start method in InputActivity when the request fails
        activity.gotFoodError(error.getMessage());
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray foodArray = response.getJSONArray("common");
            for(int i = 0; i < foodArray.length(); i ++){
                JSONObject temp = foodArray.getJSONObject(i);
                foodJSON.add(temp);
                String foodString = temp.getString("food_name");
//                String foodString = temp.getString("serving_unit");
                food.add(foodString);
            }
        } catch (JSONException e) {
            Log.e("Developer", e.getMessage());
        }
        activity.gotFood(food);
    }
}
