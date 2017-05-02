package com.bankmanagement.r.bankmanagement;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class home extends Fragment {

    View myView;
    Button b1;
    Button b2;
    Button b3;
    Button b4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_home, container, false); // Required empty public constructor

        //buttons action
        //button 1 - go the nearest and less crowded activity
        b1 = (Button) myView.findViewById(R.id.nearest);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("activity", "nearest");
                SelectService select = new SelectService();
                select.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();


                ft.replace(R.id.content_frame, select);
                ft.addToBackStack(null);
                ft.commit();
            }

        });
        //button 2 - go the selectBranch activity
        b2 = (Button) myView.findViewById(R.id.selectBranch);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("activity", "selectBranch");
                SelectService select = new SelectService();
                select.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();


                ft.replace(R.id.content_frame, select);
                ft.addToBackStack(null);
                ft.commit();
            }

        });
        //button 3 - go the favorite Branch activity
        b3 = (Button) myView.findViewById(R.id.favorite);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                FavoriteBranch select = new FavoriteBranch();
                select.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.content_frame, select);
                ft.addToBackStack(null);
                ft.commit();
            }

        });
        //button 4 - go the contact us Branch activity
        b4 = (Button) myView.findViewById(R.id.contactus);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.content_frame, new ContactUs());
                ft.addToBackStack(null);
                ft.commit();
            }

        });


        return myView;


}
}
