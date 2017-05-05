package com.bankmanagement.r.bankmanagement;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * The Phone Number layout that offers login to the application via phone number.
 *
 * @Copyright © 2017 Bank Crowd System
 */

public class PhoneNumber extends AppCompatActivity {
    Button b1;
    EditText phone;
    String msg;
    private Retrofit retrofit_object;
    static SqlData sql;

    @Override
//For the back button , un enable it if the user logged out
    public void onBackPressed() {
        String message;
        try {
            Intent intent = getIntent();
            message = intent.getStringExtra("ToLogin");
            if (message.equals("true")) {
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        sql = new SqlData(this);
        if(sql.isLogin())
        {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            in.putExtra("user'sPhone", sql.getPhoneNumber());
            startActivity(in);
        }
        sql.closeDB();
        phone = (EditText) findViewById(R.id.phoneNumber);
        b1 = (Button) findViewById(R.id.sign_in_button);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GPSTracker gps;
                gps = new GPSTracker(PhoneNumber.this);

                // check if GPS enabled
                Log.i("okkk","11111");
                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Log.i("latitude",""+latitude);
                    Log.i("longitude",""+longitude);
                }
                boolean check = validate();
                retrofit_object = new Retrofit.Builder().
                        baseUrl(getResources().getString(R.string.baseUrlApi))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GetServerData service_api = retrofit_object.create(GetServerData.class);
                if (check == true) {
                    Call<JsonElement> get_Cities = service_api.getData(phone.getText().toString());
                    get_Cities.enqueue(new Callback<JsonElement>() {
                                           @Override
                                           public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                         msg = response.body().getAsJsonObject().get("msg").toString();
                                               Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                                           }

                                           @Override
                                           public void onFailure(Call<JsonElement> call, Throwable t) {
                                               Log.w("EE", "error");
                                           }
                                       }

                    );

                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    in.putExtra("user'sPhone", phone.getText().toString());
                    startActivity(in);
                    //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    //check that the phone number is valid
    private boolean validate() {

        boolean valid;

        if (phone.length() == 0) {
            Toast.makeText(getApplicationContext(), "please enter your phone number.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length() != 10) {
            Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            valid = Patterns.PHONE.matcher(phone.getText().toString()).matches();
            if (valid == true) {
                //Toast.makeText(getApplicationContext(), "phone number is valid", Toast.LENGTH_SHORT).show();
                sql.loginUser(phone.getText().toString());
                return true;
            } else if (valid == false) {
                Toast.makeText(getApplicationContext(), "Please enter valid 10 digit phone number", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;


    }

    //Connection Interface
    public interface GetServerData {
        @GET("userLogin.php")
        Call<JsonElement> getData(@Query("phone") String phone);
    }

}


