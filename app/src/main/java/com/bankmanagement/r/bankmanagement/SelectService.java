package com.bankmanagement.r.bankmanagement;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * The service layout that contains a list of services.
 *
 *  @Copyright © 2017 Bank Crowd System
 *
 */

public class SelectService extends Fragment {

    View view;
    String activity;
    Button service1;
    Button service2;
    String branch;
   // String Latitude;
    //String Longitude;
    Button home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.select_service, container, false);


        activity = getArguments().getString("activity");
        branch=getArguments().getString("branch");
       // Latitude=getArguments().getString("Latitude");
       // Longitude=getArguments().getString(" Longitude");
        //customer service button
        service1 = (Button) view.findViewById(R.id.customer);

        service1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activity", activity);
                bundle.putString("service", "Customer_Service");
               // bundle.putString("Latitude",Latitude);
                //bundle.putString("Longitude", Longitude);
                if (activity == "nearest") {
                    NearestAndLessCrowded N = new NearestAndLessCrowded();

                    N.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();


                    ft.replace(R.id.content_frame, N);
                    ft.addToBackStack(null);
                    ft.commit();


                } else if (activity == "selectBranch") {
                    SelectCity sb = new SelectCity();
                    sb.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();


                    ft.replace(R.id.content_frame, sb);
                    ft.addToBackStack(null);
                    ft.commit();

                }else if (activity == "favorite") {
                    bundle.putString("branch", branch);
                    CrowdStatus sb = new CrowdStatus();
                    sb.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();


                    ft.replace(R.id.content_frame, sb);
                    ft.addToBackStack(null);
                    ft.commit();

                }

            }
        });

        //teller service button
        service2 = (Button) view.findViewById(R.id.teller);

        service2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activity", activity);
                bundle.putString("service", "Teller_Service");
                if (activity == "nearest") {
                    NearestAndLessCrowded N = new NearestAndLessCrowded();
                    N.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();


                    ft.replace(R.id.content_frame, N);
                    ft.addToBackStack(null);
                    ft.commit();


                } else if (activity == "selectBranch") {
                    SelectCity sb = new SelectCity();
                    sb.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();


                    ft.replace(R.id.content_frame, sb);
                    ft.addToBackStack(null);
                    ft.commit();

                }else if (activity == "favorite") {
                    CrowdStatus sb = new CrowdStatus();
                    sb.setArguments(bundle);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();


                    ft.replace(R.id.content_frame, sb);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            }
        });

        //back to the home button
        home =(Button) view.findViewById(R.id.Home);
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
        return view;
    }

}
