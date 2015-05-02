package ru.libertyguard.libertyguard;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Лев on 31.03.2015.
 */
public class RatingFragment extends Fragment {

    RatingAdapter ratingAdapter;
    ArrayList<Rating> ratingsList = new ArrayList<>();
    MainActivity.DBHelper dbHelper;
    private GetRatingAsync getRatingAsync;

    ListView lvRatings;
    TextView tvRatingListEmpty;

    public static final String TAG = "RatingFragmentTag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rating, container, false);

        // находим список
        lvRatings = (ListView) view.findViewById(R.id.lvRatings);
        tvRatingListEmpty = (TextView) view.findViewById(R.id.tvRatingListEmpty);

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            fillData();
        }
        else{
            tvRatingListEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Проверьте соединение с интернетом", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden){

            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                if(lvRatings.getCount() > 0){

                    ratingAdapter.clearData();
                    ratingAdapter.notifyDataSetChanged();

                }

                fillData();
            }
            else{
                Toast.makeText(getActivity(),"Проверьте соединение с интернетом",Toast.LENGTH_LONG).show();
            }

        }
    }

    // генерируем данные для адаптера
    void fillData() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getRatingAsync = new GetRatingAsync("");
        getRatingAsync.execute((Void) null);
        Log.d("Рейтинг2", "ЭТАП3");
    }


    public class GetRatingAsync extends AsyncTask<Void, Void, String> {

        String ratings;
        ConnectSupport cs = new ConnectSupport();

        public GetRatingAsync(String ratings) {
            this.ratings = ratings;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                ratings = cs.getRatings();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return ratings;
        }

        @Override
        protected void onPostExecute(String ratings) {

            if(ratings.contains(";")){
                String[] parts = ratings.split(";");

                for(int q = 0; q  < parts.length-1; q++){

                    String[] rating = parts[q].split(":");

                    ratingsList.add(new Rating(Integer.parseInt(rating[0]),rating[1],Integer.parseInt(rating[2])));

                }

            }

            if(ratingsList.toArray().length == 0){
                tvRatingListEmpty.setVisibility(View.VISIBLE);

            }else{
                tvRatingListEmpty.setVisibility(View.INVISIBLE);
            }

            ratingAdapter = new RatingAdapter(getActivity(), ratingsList);

           // Log.d("Рейтинг2", "ЭТАП1");

            lvRatings.setAdapter(ratingAdapter);
           // Log.d("Рейтинг2", "ЭТАП2");

            getRatingAsync = null;

            //  showProgress(false);

        }

        @Override
        protected void onCancelled() {
            getRatingAsync = null;
            // showProgress(false);
        }
    }

}