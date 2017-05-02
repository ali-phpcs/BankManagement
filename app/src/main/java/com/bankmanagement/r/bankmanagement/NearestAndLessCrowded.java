package com.bankmanagement.r.bankmanagement;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * A Nearest And Less Crowded screen that allow the user to see the top 3 of branches
 * that are nearest and less crowded.
 *
 *  @Copyright © 2017 Bank Crowd System
 *
 */
public class NearestAndLessCrowded extends Fragment {
    private Retrofit retrofit_object;
    View view;
    String service;
    double Latitude;
    double Longitude;
    Button home;
    List<String> branches = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.nearest_and_less_crowded, container, false);

        service = getArguments().getString("service");

        retrofit_object = new Retrofit.Builder().
                baseUrl(getResources().getString(R.string.baseUrlApi))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetServerData service_api = retrofit_object.create(GetServerData.class);
        //get Locations Info
        GPSTracker gps;
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        Log.i("okkk","11111");
        if(gps.canGetLocation()) {

            Latitude = gps.getLatitude();
            Longitude = gps.getLongitude();
            Log.i("latitude11", "" + Latitude);
            Log.i("longitude22", "" + Longitude);

            Call<JsonElement> get_Cities = service_api.getData(Latitude, Longitude);
            get_Cities.enqueue(new Callback<JsonElement>() {
                                   @Override
                                   public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                       JsonArray Data = response.body().getAsJsonArray();
                                       Log.w("DD", "" + Data);

                                       branches.removeAll(branches);
                                       // if(cityName.isEmpty()){
                                       for (int i = 0; i < Data.size(); i++) {
                                           String city = Data.get(i).getAsJsonObject().get("B_Name").toString().replace("\"", "");
                                           Log.w("C", "" + city);
                                           branches.add(city);

                                       }
                                       plv(view);
                                       Log.w("branch1:", "" + branches);
                                   }

                                   @Override
                                   public void onFailure(Call<JsonElement> call, Throwable t) {
                                       Log.w("EE", "error");
                                   }
                               }

            );

            //back to the home button
            home = (Button) view.findViewById(R.id.Home);
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
            rc(view);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        return view;

    }

    public void onStart() {

        super.onStart();

    }

    private void plv(View view) {

        Log.w("branch:", "" + branches);

        ArrayAdapter<?> aa = new ArrayAdapter<>(getActivity(), R.layout.city, branches);
        ListView list = (ListView) view.findViewById(R.id.NearestBranches);
        list.setAdapter(aa);
    }

    private void rc(final View view) {
        ListView list = (ListView) view.findViewById(R.id.NearestBranches);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clicked, int position, long id) {

                ListView list = (ListView) view.findViewById(R.id.NearestBranches);
                String selectedFromList = (list.getItemAtPosition(position)).toString();
                Log.w("selectedFromList:", "" + selectedFromList);
                Bundle bundle = new Bundle();
                bundle.putString("branch", selectedFromList);
                bundle.putString("service", service);
                Log.w("selectedFromList:", "" + selectedFromList);

                CrowdStatus N = new CrowdStatus();
                N.setArguments(bundle);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_frame, N);
                ft.addToBackStack(null);
                ft.commit();


            }
        });
    }


    //Connection Interface
    public interface GetServerData {
        @GET("NearestAndLessCrowd.php")
        Call<JsonElement> getData(@Query("latitude") double latitude, @Query("longitude") double longitude);
    }


}
