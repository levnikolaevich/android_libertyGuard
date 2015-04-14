package ru.libertyguard.libertyguard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Лев on 31.03.2015.
 */
public class RatingFragment extends Fragment {

    public static final String TAG = "RatingFragmentTag";
    Button btLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rating, container, false);


        return view;
    }



}
