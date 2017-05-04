package com.bankmanagement.r.bankmanagement;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
 * The favourite layout that contains a list for favorite branches of the user.
 *
 *  @Copyright © 2017 Bank Crowd System
 *
 */
public class FavoriteBranch extends Fragment {

    View view;
    private Retrofit retrofit_object;
    String service;
    String city;
    List<String> branchelist = new ArrayList<>();
    TextView home;
    ListView list_view;
    ArrayAdapter<String> adapter;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list_items=new ArrayList<String>();
    int count=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.favorite_branch, container, false);
        list_view=(ListView) view.findViewById(R.id.favoriteList);
        adapter=new ArrayAdapter<String>(view.getContext(),R.layout.city, branchelist);
        list_view.setAdapter(adapter);
        list_view.setChoiceMode(list_view.CHOICE_MODE_MULTIPLE_MODAL);

        retrofit_object = new Retrofit.Builder().
                baseUrl(getResources().getString(R.string.baseUrlApi))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetServerData service_api = retrofit_object.create(GetServerData.class);

        Call<JsonElement> get_Cities = service_api.getData(MainActivity.UserPhone);
        get_Cities.enqueue(new Callback<JsonElement>() {
                               @Override
                               public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                                   JsonArray Data = response.body().getAsJsonArray();
                                   Log.w("DD", "" + Data);

                                   branchelist.removeAll(branchelist);
                                   // if(cityName.isEmpty()){
                                   for (int i = 0; i < Data.size(); i++) {
                                       String city = Data.get(i).getAsJsonObject().get("B_Name").toString().replace("\"", "");
                                       Log.w("C",""+city);
                                       branchelist.add(city);

                                   }
                                   //}
                                   plv(view);
                                   Log.w("branch1:",""+branchelist);
                               }
                               @Override
                               public void onFailure(Call<JsonElement> call, Throwable t) {
                                   Log.w("EE","error");
                               }
                           }

        );
        //Go to Home layout
        home =(TextView) view.findViewById(R.id.Home);
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





        //delete from the list

        list_view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
              count =count+1;
                actionMode.setTitle(count+ " Items selected");
               list_items.add(branchelist.get(i));

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

               MenuInflater inflater =actionMode.getMenuInflater();
               inflater.inflate(R.menu.my_context_menu,menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.delete_id:
                        for(String msg :list_items)
                        {
                            adapter.remove(msg);

                    }
                       Toast.makeText(view.getContext(), "Item removed", Toast.LENGTH_SHORT).show();
                        count=0;
                        actionMode.finish();
                       return true;

                   default:
                       return false;

                }

                     //  return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });


        rc(view);
        return view;

    }
    public void onStart() {

        super.onStart();

    }

    private void plv(View view) {

        Log.w("branch:",""+branchelist);

        list_view=(ListView) view.findViewById(R.id.favoriteList);
        adapter=new ArrayAdapter<String>(view.getContext(),R.layout.city, branchelist);
        list_view.setAdapter(adapter);
    }

    private void rc(final View view) {
        ListView list = (ListView) view.findViewById(R.id.favoriteList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View clicked, int position, long id) {

                ListView list = (ListView) view.findViewById(R.id.favoriteList);
                String selectedFromList = (list.getItemAtPosition(position)).toString();
                Log.w("selectedFromList:", "" + selectedFromList);
                Bundle bundle = new Bundle();
                bundle.putString("branch", selectedFromList);
                bundle.putString("activity", "favorite");
                SelectService N = new SelectService();
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
        @GET("getFavoriteList.php")
        Call<JsonElement> getData(@Query("phone") String phone);
    }


}
