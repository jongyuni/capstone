package com.example.draw4u;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import okhttp3

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.widget.Toast.*;

public class ExtractKeyword extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String fname;
        String content;
        Intent intent;
        intent = getIntent();
        fname = intent.getStringExtra("fname");
        content = intent.getStringExtra("content");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("diary", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.post(fname, jsonObject.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void post(String fname, String jsonMessage) {
        String requestURL = "http://34.64.108.156:8000/";
        String diarydate = fname;
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonMessage))
                    .build();

            //비동기 처리 (enqueue 사용)
            client.newCall(request).enqueue(new Callback() {
                //비동기 처리를 위해 Callback 구현
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //System.out.println("Response Body is " + response.body().string());
                    String parsing = response.body().string();
                    ArrayList<String> list = new ArrayList<>();
                    String[] splitStr = parsing.split("\\]");
                    for(int i=0; i<splitStr.length; i++){
                        list.add(splitStr[i]);
                    }
                    //System.out.println(list.get(0));
                    ArrayList<String> list2 = new ArrayList<>();
                    String[] splitStr2 = list.get(0).split("\\[");
                    for(int i=0; i<splitStr2.length; i++){
                        list2.add(splitStr2[i]);
                    }
                    System.out.println(list2.get(4).replace(" ",""));
                }
            });
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }



}
