package ru.libertyguard.libertyguard;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

/**
 * Created by Лев on 31.03.2015.
 */
public class ProfileFragment extends Fragment {

    SharedPreferences sPref;
    public static final String TAG = "ProfileFragmentTag";

    private MainInfoTask mainInfoTask;
    TextView tvName, tvLevel;
    ImageView ivAva;

    private View mProgressView;
    private View mProfileFormView;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);

        tvName = (TextView) view.findViewById(R.id.tvName);
        tvLevel = (TextView) view.findViewById(R.id.tvLevel);
        ivAva = (ImageView) view.findViewById(R.id.ivAva);

        mProfileFormView = view.findViewById(R.id.svProfile);
        mProgressView = view.findViewById(R.id.profile_progress);
        //mProgressView.setVisibility(0);



        getContentProfile();


        return view;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden)
            if(!getActivity().getSharedPreferences("libertyguardfile", getActivity().MODE_PRIVATE).getString("isProfile","").contains("1"))
                getContentProfile();

    }

    void getContentProfile(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sPref = getActivity().getSharedPreferences("libertyguardfile", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
                showProgress(true);
                mainInfoTask = new MainInfoTask("");
                mainInfoTask.execute((Void) null);
                ed.putString("isProfile", "1");
        }
        else{
                tvName.setText("Нет связи с сервером");
                tvLevel.setText("Проверьте соединение с интернетом");
                ed.putString("isProfile", "0");
        }
        ed.apply();
   }

    public class MainInfoTask extends AsyncTask<Void, Void, String> {

        String maininfo;
        ConnectSupport cs = new ConnectSupport();

        public MainInfoTask(String maininfo) {
            this.maininfo = maininfo;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {


             maininfo = cs.getMainInfo(getActivity().getSharedPreferences("libertyguardfile", getActivity().MODE_PRIVATE).getString("saved_login",""));


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            return maininfo;
        }

        @Override
        protected void onPostExecute(String mInfo) {

            if(mInfo.contains(";")){
                String[] parts = mInfo.split(";");
                Log.d("Задача: ","http://libertyguard.ru/home/GetImage?id="+parts[5]);
                ivAva.setImageBitmap(cs.getImageBitmap("http://libertyguard.ru/home/GetImage?id=" + parts[5]));

                tvName.setText(parts[2] + "\n" + parts[1] + " " + parts[0]);
                tvLevel.setText("Очков до нового уровня: " + parts[4]);
            }

            mainInfoTask = null;

            showProgress(false);

        }

        @Override
        protected void onCancelled() {
            mainInfoTask = null;
            showProgress(false);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mProfileFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProfileFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




}
