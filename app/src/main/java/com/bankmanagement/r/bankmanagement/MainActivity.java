package com.bankmanagement.r.bankmanagement;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 *The main layout that contains a menu navigation.
 *
 *  @Copyright © 2017 Bank Crowd System
 *
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    public String userPhone;
    static String UserPhone;
    MySQLite mySQLite;
    SQLiteDatabase sqliteDB;
    static SqlData sql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sql = new SqlData(this);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, new home());
        ft.addToBackStack(null);
        ft.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);



        //show the user's phone number on the navigation header
        userPhone = getIntent().getStringExtra("user'sPhone");
        UserPhone = userPhone;
        TextView user = (TextView) header.findViewById(R.id.userPhone);
        user.setText(userPhone);


        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        //Display icon to open/ close navigation list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

    }

    //Enable the open/close icon
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_nearest) {

            Bundle bundle = new Bundle();
            bundle.putString("activity", "nearest");


            SelectService f3 = new SelectService();
            f3.setArguments(bundle);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();


            ft.replace(R.id.content_frame, f3);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_select) {

            Bundle bundle = new Bundle();
            bundle.putString("activity", "selectBranch");
            SelectService f3 = new SelectService();
            f3.setArguments(bundle);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();


            ft.replace(R.id.content_frame, f3);
            ft.addToBackStack(null);
            ft.commit();


        } else if (id == R.id.nav_favorit) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.content_frame, new FavoriteBranch());
            ft.addToBackStack(null);
            ft.commit();


        } else if (id == R.id.nav_contactus) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.content_frame, new ContactUs());
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_feedback) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.replace(R.id.content_frame, new SendFeedback());
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(getApplicationContext(), PhoneNumber.class);
            //send intent "To login = true" in order to unenable the back button after logout
            sql.logOut();
            UserPhone = "";
            intent.putExtra("ToLogin", true);
            getApplicationContext().startActivity(intent);
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}