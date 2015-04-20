package ru.libertyguard.libertyguard;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Лев on 31.03.2015.
 */
public class QuestFragment extends Fragment {

    QuestAdapter questAdapter;
    ArrayList<Quest> questsList = new ArrayList<>();
    MainActivity.DBHelper dbHelper;
    private GetQuestTask getQuestTask;

    ListView lvMain;
    TextView tvQuestListEmpty;

    public static final String TAG = "QuestFragmentTag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.quest, container, false);

        // находим список
        lvMain = (ListView) view.findViewById(R.id.lvQuests);
        tvQuestListEmpty = (TextView) view.findViewById(R.id.tvQuestListEmpty);

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            fillData();
        }
        else{
            tvQuestListEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"Проверьте соединение с интернетом",Toast.LENGTH_LONG).show();
        }



        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("СПИСОК", "itemClick: position = " + position + ", id = "
                        + id);

                Intent intent = new Intent(getActivity(), TaskListActivity.class);
                intent.putExtra("id", id );
                startActivity(intent);

            }
        });

        /*
        lvMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("СПИСОК", "itemSelect: position = " + position + ", id = "
                        + id);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("СПИСОК", "itemSelect: nothing");
            }
        });

        lvMain.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d("СПИСОК", "scrollState = " + scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                Log.d("СПИСОК", "scroll: firstVisibleItem = " + firstVisibleItem
                        + ", visibleItemCount" + visibleItemCount
                        + ", totalItemCount" + totalItemCount);
            }
        });*/


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
                if(lvMain.getCount() > 0){
                    // QuestAdapter questAdapter2 = (QuestAdapter)lvMain.getAdapter();
                    questAdapter.clearData();
                    questAdapter.notifyDataSetChanged();
                    //  lvMain.setAdapter(null);
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

       /* if(questAdapter.getCount() > 0){
           // QuestAdapter questAdapter2 = (QuestAdapter)lvMain.getAdapter();
            questAdapter.clearData();
            questAdapter.notifyDataSetChanged();
        }*/

        getQuestTask = new GetQuestTask("");
        getQuestTask.execute((Void) null);

        /*for (int i = 1; i <= 3; i++) {
            quests.add(new Quest(i, "Квест" + i,
                    100*i, i+".04.2015", i*2,i*3,"100%"));
        }*/

        // подключаемся к БД
        //SQLiteDatabase db = dbHelper.getWritableDatabase();


    }


    public class GetQuestTask extends AsyncTask<Void, Void, String> {

        String quests;
        ConnectSupport cs = new ConnectSupport();

        public GetQuestTask(String quests) {
            this.quests = quests;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {


                quests = cs.getQuests(getActivity().getSharedPreferences("libertyguardfile", getActivity().MODE_PRIVATE).getString("saved_login",""));


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            return quests;
        }

        @Override
        protected void onPostExecute(String quests) {

            if(quests.contains(";")){
                String[] parts = quests.split(";");

                for(int q = 0; q  < parts.length-1; q++){
                    Log.d("Квест"+q, parts[q]);
                    String[] quest = parts[q].split(":");

                    for(int r =0; r < quest.length; r++){
                        Log.d("Квест",quest[r]);
                    }

                    questsList.add(new Quest(Integer.parseInt(quest[0]),quest[1],Integer.parseInt(quest[2]),quest[3], Integer.parseInt(quest[4]),Integer.parseInt(quest[5]),Integer.parseInt(quest[6]),quest[7].trim()));

                }

            }


            if(questsList.toArray().length == 0){
                tvQuestListEmpty.setVisibility(View.VISIBLE);

            }else{
                tvQuestListEmpty.setVisibility(View.INVISIBLE);
            }


            questAdapter = new QuestAdapter(getActivity(), questsList);

            lvMain.setAdapter(questAdapter);

            getQuestTask = null;

          //  showProgress(false);

        }

        @Override
        protected void onCancelled() {
            getQuestTask = null;
           // showProgress(false);
        }
    }




}