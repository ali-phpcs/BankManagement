package com.bankmanagement.r.bankmanagement;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class TestAdapter extends BaseAdapter {

    private Context mContext;
    private List<DataUnion> mdata;

    private Retrofit retrofit_object;

    private TestAdapter mAdapter = this;


    public TestAdapter(Context mContext, List<DataUnion> mdata) {
        this.mContext = mContext;
        this.mdata = mdata;
    }


    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public DataUnion getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView textTitle;

        Button buttonDelete;
        ListView list;

        public ViewHolder(View view) {
            textTitle = (TextView) view.findViewById(R.id.textview);

            buttonDelete = (Button) view.findViewById(R.id.button1);
            view.setTag(this);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_template, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        DataUnion small_union = mdata.get(position);
        holder.textTitle.setText(small_union.row1);
        holder.buttonDelete.setText(small_union.itag);


        holder.textTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    System.out.println("" + getItem(position).row1);
//
                    SelectService N = new SelectService();
                    Bundle bundle = new Bundle();
                    bundle.putString("branch", getItem(position).row1);
                    bundle.putString("activity", "favorite");
                    N.setArguments(bundle);
                    FragmentManager fm = ((Activity) mContext).getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.content_frame, N);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                return false;
            }
        });

        holder.buttonDelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    System.out.println("delete" + position);
                    System.out.println("" + getItem(position).row1);
                    delete(getItem(position).row1);
                    mdata.remove(position);
                    mAdapter.notifyDataSetChanged();

//                holder.textTitle.setText("CHanged!!!!");
                }
                return false;
            }
        });

        return convertView;

    }

    public void delete(String branch) {

        retrofit_object = new Retrofit.Builder().
                baseUrl(mContext.getResources().getString(R.string.baseUrlApi))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetServerDataF service_apif = retrofit_object.create(GetServerDataF.class);
        System.out.println("MainActivity.UserPhone" + MainActivity.UserPhone);
        System.out.println("branch" + branch);
        Call<JsonElement> addToF = service_apif.getDataf(MainActivity.UserPhone, branch);
        addToF.enqueue(new Callback<JsonElement>() {
                           @Override
                           public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                               Log.w("aaaasss", "" + response.body().toString());
                               Toast.makeText(mContext, "You have successfully deleted the branch to favorites list", Toast.LENGTH_SHORT).show();

                           }

                           @Override
                           public void onFailure(Call<JsonElement> call, Throwable t) {
                               Log.w("EE", t.getLocalizedMessage().toString());
                           }
                       }


        );
    }

    public interface GetServerDataF {
        @GET("deleteFavorite.php")
        Call<JsonElement> getDataf(@Query("phone") String phone, @Query("branch") String branch);
    }

}
