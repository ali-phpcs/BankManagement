package com.bankmanagement.r.bankmanagement;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The crowd status layout provide information about the current state of a particular branch.
 *
 * @Copyright © 2017 Bank Crowd System
 */
public class CrowdStatus extends Fragment {

    View myView;
    TextView countOfTticket;
    String TimeforWating;
    TextView BranchName;
    TextView serviceType;
    TextView waiting_time;
    String branch;
    String service;
    Button FavoriteBranch;
    private Retrofit retrofit_object;
    Button route;
    TextView home;
    double latitude;
    double longitude;
    Button refresh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.crowd_status, container, false);


        branch = getArguments().getString("branch");
        service = getArguments().getString("service");
        Log.w("branch:", getArguments().getString("branch"));
        Log.w("service:", getArguments().getString("service"));
        serviceType = (TextView) myView.findViewById(R.id.serviceType);
        countOfTticket = (TextView) myView.findViewById(R.id.wating);
        BranchName = (TextView) myView.findViewById(R.id.BranchName);
        waiting_time = (TextView) myView.findViewById(R.id.waiting_time);
        FavoriteBranch = (Button) myView.findViewById(R.id.AddToFavorite);

        BranchName.setText(branch);
        if (service == "Teller_Service") {
            serviceType.setText("Teller Service");
        } else {
            serviceType.setText("Customer Service");
        }
        retrofit_object = new Retrofit.Builder().
                baseUrl(getResources().getString(R.string.baseUrlApi))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetServerData service_api = retrofit_object.create(GetServerData.class);


        //Getting vrowd statues of the selected branch from the database
        Call<JsonElement> get_Cities = service_api.getData(branch, service);
        get_Cities.enqueue(new Callback<JsonElement>() {
                               @Override
                               public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                   JsonArray Data = response.body().getAsJsonArray();
                                   countOfTticket.setText(Data.get(0).getAsJsonObject().get("count").toString().replace("\"", ""));
                                   TimeforWating = Data.get(0).getAsJsonObject().get("time_for_wating").toString().replace("\"", "");
                                   String startTime = "00:00";
                                   int minutes = Integer.parseInt(TimeforWating);
                                   int h = minutes / 60 + Integer.parseInt(startTime.substring(0, 1));
                                   int m = minutes % 60 + Integer.parseInt(startTime.substring(3, 4));
                                   String newtime = h + ":" + m;
                                   String MH;
                                   if (h != 0) {
                                       MH = "hours";
                                   } else {
                                       MH = "minutes";
                                       newtime = "" + m;
                                   }
                                   waiting_time.setText(newtime + " " + MH);
                                   Log.w("DD", "" + Data);
                               }

                               @Override
                               public void onFailure(Call<JsonElement> call, Throwable t) {
                                   Log.w("EE", "error");
                               }
                           });

        route = (Button) myView.findViewById(R.id.Route);
        route.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Ali please add query here to
                // retrieve the latitude and longitude of the branch location
                latitude=40.714728;
                longitude=-73.998672;
                String lable=getArguments().getString("branch")+" Branch";
                String uriBegin="geo:"+latitude+","+ longitude;
                String query= latitude + "," + longitude+ "(" + lable+")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin+ "?q=" + encodedQuery + "&z=16";
                Uri uri =Uri.parse(uriString);
                Intent intent =new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                Toast.makeText(getActivity(), "Location of the branch", Toast.LENGTH_SHORT).show();
            }


    });

        refresh = (Button) myView.findViewById(R.id.Refresh);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //refresh the values of the crowd status

            }


        });
        FavoriteBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit_object = new Retrofit.Builder().
                        baseUrl(getResources().getString(R.string.baseUrlApi))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GetServerDataF service_apif = retrofit_object.create(GetServerDataF.class);
                branch = getArguments().getString("branch");
                Log.i("MainActivity.UserPhone",MainActivity.UserPhone);
                Log.i("branch",branch);
                Call<JsonElement> addToF = service_apif.getDataf(MainActivity.UserPhone,branch);
                addToF.enqueue(new Callback<JsonElement>() {
                                       @Override
                                       public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                           Toast.makeText(getActivity(), "You have successfully added an branch to favorites list", Toast.LENGTH_SHORT).show();

                                       }

                                       @Override
                                       public void onFailure(Call<JsonElement> call, Throwable t) {
                                           Log.w("EE", t.getLocalizedMessage().toString());
                                           //Log.w("EE", t.getCause().toString());
                                       }
                                   }


                );
            }
        });

        //Go to Home layout
        home =(TextView) myView.findViewById(R.id.Home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_frame, new home());
                ft.addToBackStack(null);
                ft.commit();

            }
        });

        return myView;

    }
    //Connection GetServerDataF
    public interface GetServerDataF {
        @GET("FavoriteBranch.php")
        Call<JsonElement> getDataf(@Query("phone") String phone, @Query("branch") String branch);
    }

    //Connection Interface
    public interface GetServerData {
        @GET("getCrowd.php")
        Call<JsonElement> getData(@Query("b_name") String b_name, @Query("service_type") String service_type);
    }

}
