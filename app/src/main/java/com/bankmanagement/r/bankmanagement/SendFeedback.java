package com.bankmanagement.r.bankmanagement;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * The Feedback layout that use for sending feedback form users to the database.
 *
 * @Copyright © 2017 Bank Crowd System
 */
public class SendFeedback extends Fragment {

    View myView;
    private Retrofit retrofit_object;
    APIService services;
    String a1;
    String a2;
    String a3;
    String a4;
    EditText q1;
    EditText q2;
    EditText q3;
    EditText q4;
    TextView home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.send_feedback, container, false);
        q1 = (EditText) myView.findViewById(R.id.a1);
        q2 = (EditText) myView.findViewById(R.id.a2);
        q3 = (EditText) myView.findViewById(R.id.a3);
        q4 = (EditText) myView.findViewById(R.id.a4);
        retrofit_object = new Retrofit.Builder().
                baseUrl(getResources().getString(R.string.baseUrlApi))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        services = retrofit_object.create(APIService.class);
        Button btn = (Button) myView.findViewById(R.id.Send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick();
            }
        });


        return myView;

    }

    public void onclick() {
        a1 = q1.getText().toString();
        a2 = q2.getText().toString();
        a3 = q3.getText().toString();
        a4 = q4.getText().toString();
        int a = Integer.parseInt("1234");
        if (a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty()) {
            Toast.makeText(getActivity(), "All Fields REQUIRED!", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(a1) > 4 || Integer.parseInt(a2) > 4 || Integer.parseInt(a3) > 4) {
            Toast.makeText(getActivity(), "Only values 1-4 are accepting for rating",
                    Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(a1) < 1 || Integer.parseInt(a2) < 1 || Integer.parseInt(a3) < 1) {
            Toast.makeText(getActivity(), "Only values 1-4 are accepting for rating",
                    Toast.LENGTH_SHORT).show();
        } else {
            sendPost(a1, a2, a3, a4, MainActivity.UserPhone);
            Toast.makeText(getActivity(), "Thank you", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendPost(String performance, String quality, String design, String comments, String user) {
        services.savePost(performance, quality, design, comments, user).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    Log.i("isSuccessful", "post submitted to API." + response.message());
                    Log.i("isSuccessful000", "" + response.raw());
                    Log.i("isSuccessful111", "" + response.body().toString());
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e("onFailure", t.getLocalizedMessage());
            }
        });
    }

    public interface APIService {
        @FormUrlEncoded
        @POST("feadback.php")
        Call<JsonElement> savePost(@Field("F_Performance") String performance,
                                   @Field("F_Quality") String quality,
                                   @Field("F_Design") String design,
                                   @Field("F_Comments") String comments,
                                   @Field("phone") String phone);
    }

}
