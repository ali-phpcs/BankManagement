package com.bankmanagement.r.bankmanagement;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Contact us layout used to allow the user to know the contact information.
 * It also redirect the user to the email or phone default applications
 *
 *  @Copyright © 2017 Bank Crowd System
 *
 */
public class ContactUs extends Fragment {

    View myView;
    TextView mail;
    TextView phone;
    Button home;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.contact_us, container, false);

        //open email app to send a an email
        mail = (TextView)  myView.findViewById(R.id.email);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + "bcm-support@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body");
                startActivity(Intent.createChooser(emailIntent, "Send email using..."));

            }});


        //open phone app to call
        phone = (TextView)  myView.findViewById(R.id.telephone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("tel:013843356"));
                startActivity(i);
            }
        });

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

        return myView;


    }
}
