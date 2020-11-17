package com.example.draw4u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

import static android.widget.Toast.*;

public class ExtractKeyword extends AppCompatActivity {

    ArrayList<String> keyword = new ArrayList<>();

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

        Thread t = new Thread(){
            public void run(){
                String requestURL = "http://34.64.108.156:8000/keyword_abstract/";
                JSONObject jsonObject = new JSONObject();
                try {
                    System.out.println(content);
                    jsonObject.put("diary", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestPost(requestURL, jsonObject.toString());
            }
        };

        t.start();

        try{
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        db.collection(currentUser.getUid()).document(fname)
                .update("keyword1",keyword.get(0).toString());
        db.collection(currentUser.getUid()).document(fname)
                .update("keyword2",keyword.get(1).toString());
        db.collection(currentUser.getUid()).document(fname)
                .update("keyword3",keyword.get(2).toString());

        setResult(Activity.RESULT_OK);
        finish();

    }

    public void requestPost(String requestURL, String jsonMessage){
        ArrayList<String> ret = new ArrayList<>();
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
            HttpPost postRequest = new HttpPost(requestURL); //POST 메소드 URL 새성
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Connection", "keep-alive");
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setHeader("Accept-Encoding","UTF-8");
            //postRequest.addHeader("Authorization", token); // token 이용시

            postRequest.setEntity(new StringEntity(jsonMessage,"UTF-8")); //json 메시지 입력

            HttpResponse response = client.execute(postRequest);

            //Response 출력
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);

                //body = new String(body.getBytes("iso-8859-1"),"utf-8");
                System.out.println(body);

                ArrayList<String> list = new ArrayList<>();

                String[] splitStr = body.split("\\[");

                for(int i=0; i<splitStr.length; i++){
                    list.add(splitStr[i]);
                }

                ArrayList<String> list2 = new ArrayList<>();

                String[] splitStr2 = list.get(1).split("\\]");

                for(int i=0; i<splitStr2.length; i++){
                    list2.add(splitStr2[i]);
                }

                ArrayList<String> list3 = new ArrayList<>();


                String[] splitStr3 = list2.get(0).split(",");

                for(int i=0; i<splitStr3.length; i++){
                    list3.add(splitStr3[i].replace(" ","").replace("\"",""));
                }
                keyword.add(list3.get(0));
                keyword.add(list3.get(1));
                keyword.add(list3.get(2));
            } else {
                System.out.println("response is error : " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }



}
