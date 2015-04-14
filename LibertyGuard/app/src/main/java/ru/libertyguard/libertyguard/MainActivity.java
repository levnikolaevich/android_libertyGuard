package ru.libertyguard.libertyguard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;




import com.crashlytics.android.Crashlytics;

import java.io.UnsupportedEncodingException;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ProfileFragment profileFragment;
    private QuestFragment questFragment;
    private RatingFragment ratingFragment;

    private android.support.v4.app.FragmentManager manager;

    DBHelper dbHelper;

   // private FragmentTransaction fragmentTransaction;

   // private Boolean checkLogin;

    SharedPreferences sPref;
    //DBHelper dbHelper;
   // final String SAVED_LOGIN = "saved_login";
    //final String SAVED_PASS = "saved_pass";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.main);

        // LoginActivity.


        ratingFragment = new RatingFragment();
        questFragment = new QuestFragment();
        profileFragment = new ProfileFragment();

        manager = getSupportFragmentManager();


        // Set the Action Bar to use tabs for navigation
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4190AB")));
        ab.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#3A8098")));

       // ab.setDisplayHomeAsUpEnabled(true);
       // ab.NavigationMode = ActionBarNavigationMode.Tabs;

        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        ab.setTitle("Страж свободы");


        // Add three tabs to the Action Bar for display
        ab.addTab(ab.newTab().setText("Квесты").setTabListener(this));
        ab.addTab(ab.newTab().setText("Рейтинг").setTabListener(this));
        ab.addTab(ab.newTab().setText("Профиль").setTabListener(this));

        dbHelper = new DBHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ConnectSupport cs = new ConnectSupport();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        sPref = getSharedPreferences("libertyguardfile", MODE_PRIVATE);
        final String mEmail = sPref.getString("saved_login","");
        final String mPassword = sPref.getString("saved_pass","");

        if (networkInfo != null && networkInfo.isConnected() && networkInfo.getTypeName().equals("WIFI")) {
        //Log.d("Контакт","Есть проверка");
            try {
                if(!cs.getAuth(mEmail, mPassword,"asdgargarHGKYU234898yhjbHBGF7856576783")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        else{
            if (!mEmail.contains("@")){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sPref = getSharedPreferences("libertyguardfile", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("isProfile", "0");
        ed.apply();


    }

    /*Табы*/
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // This is called when a tab is selected.

        fragmentTransaction = manager.beginTransaction();

        int nTabSelected = tab.getPosition();

        switch (nTabSelected) {
            case 0:
                //fragmentTransaction = getChildFragmentManager().beginTransaction();
                if(manager.findFragmentByTag(QuestFragment.TAG)== null){
                    fragmentTransaction.add(R.id.container, questFragment, QuestFragment.TAG);
                }
                else{
                    fragmentTransaction.show(questFragment);
                }

                break;
            case 1:

                if(manager.findFragmentByTag(RatingFragment.TAG)== null){
                    fragmentTransaction.add(R.id.container, ratingFragment, RatingFragment.TAG);
                }
                else{
                    fragmentTransaction.show(ratingFragment);

                }
                break;
            case 2:
                //fragmentTransaction = manager.beginTransaction();
                if(manager.findFragmentByTag(ProfileFragment.TAG)== null){
                    fragmentTransaction.add(R.id.container, profileFragment, ProfileFragment.TAG);
                }
                else{

                    fragmentTransaction.show(profileFragment);
                }
                break;
        }

        fragmentTransaction.commit();
    }

    // Implemented from ActionBar.TabListener
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // This is called when a previously selected tab is unselected.

        fragmentTransaction = manager.beginTransaction();

        int nTabSelected = tab.getPosition();

        switch (nTabSelected) {
            case 0:

                fragmentTransaction.hide(questFragment);

                break;
            case 1:


                    fragmentTransaction.hide(ratingFragment);


                break;
            case 2:


                fragmentTransaction.hide(profileFragment);

                break;
        }
        fragmentTransaction.commit();

    }

    // Implemented from ActionBar.TabListener
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // This is called when a previously selected tab is selected again.



    }

    /*Меню*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_settings:
                break;

            case R.id.menu_finish:

                sPref = getSharedPreferences("libertyguardfile", MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString("saved_login", "");
                ed.putString("saved_pass", "");
                ed.apply();

                finish();
                break;
        }

        return true;
    }

    /*БАЗА*/
    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "libertyguardDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table quest ("
                    + "id integer,"
                    + "name text,"
                    + "exp integer,"
                    + "startDate text,"
                    + "countTask integer,"
                    + "idUEQ integer,"
                    + "status text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
