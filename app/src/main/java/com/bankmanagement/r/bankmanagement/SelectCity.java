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


/**
 * The city layout that contains a list of cities.
 *
 *  @Copyright © 2017 Bank Crowd System
 *
 */
public class SelectCity extends Fragment {
    private Retrofit retrofit_object;

    View myView;
    String activity;
    String service;
    List<String> cityName = new ArrayList<>();
    Button home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.select_city, container, false);


        activity = getArguments().getString("activity");
        service = getArguments().getString("service");

        retrofit_object = new Retrofit.Builder().
                baseUrl("http://bankproject1.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetServerData service = retrofit_object.create(GetServerData.class);


        //get Locations Info
        Call<JsonElement> get_Cities = service.getData();
        get_Cities.enqueue(new Callback<JsonElement>() {
                               @Override
                               public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                   JsonArray Data = response.body().getAsJsonArray();
                                   Log.w("DD",""+Data);
                                   cityName.removeAll(cityName);
                                  // if(cityName.isEmpty()){
                                   for (int i = 0; i < Data.size(); i++) {
                                       String city = Data.get(i).getAsJsonObject().get("C_Name").toString().replace("\"", "");
                                       Log.w("C",""+city);
                                       cityName.add(city);

                                   }
                               //}
                                   plv(myView);
                                   Log.w("cityName11:",""+cityName);
                               }
                               @Override
                               public void onFailure(Call<JsonElement> call, Throwable t) {
                                   Log.w("EE","error");
                               }
                           }

        );

        //back to the home button
        home =(Button) myView.findViewById(R.id.Home);
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

        rc(myView);
        return myView;

    }

    public void onStart() {

        super.onStart();

    }

    private void plv(View view) {

       /* CityModel cityModel = new CityModel(getActivity());
        allCity = cityModel.getCity(service);*/

        Log.w("cityName:",""+cityName);

        ArrayAdapter<?> aa = new ArrayAdapter<>(getActivity(), R.layout.city, cityName);
        ListView list = (ListView) view.findViewById(R.id.city);
        list.setAdapter(aa);
    }

    private void rc(final View view) {
        ListView list = (ListView) view.findViewById(R.id.city);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clicked, int position, long id) {

                ListView list = (ListView) view.findViewById(R.id.city);
                String selectedFromList = (list.getItemAtPosition(position)).toString();
                Log.w("selectedFromList:", "" + selectedFromList);
                Bundle bundle = new Bundle();
                bundle.putString("city", selectedFromList);
                Log.w("selectedFromList:",""+selectedFromList);
                bundle.putString("service", service);
                Log.w("service:",""+service);


                    SelecteBranch sb = new SelecteBranch();
                    sb.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();


                    ft.replace(R.id.content_frame, sb);
                    ft.addToBackStack(null);
                    ft.commit();


            }
        });
    }

    //Connection Interface
    public interface GetServerData {
        @GET("getcities.php")
        Call<JsonElement> getData();
    }

}

