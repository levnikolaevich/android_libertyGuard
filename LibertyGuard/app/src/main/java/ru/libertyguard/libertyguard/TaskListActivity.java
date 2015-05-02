package ru.libertyguard.libertyguard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TaskListActivity extends ActionBarActivity {

    TaskAdapter taskAdapter;
    ArrayList<Task> tasksList = new ArrayList<>();
    private GetTasks getTasks;
   // String idQuestReturned;

    ListView lvTasks;
    TextView tvTaskListEmpty;
    String idQuest, nameQuest, idUEQ;

    public static class TaskInfo implements Parcelable {
        private int mid; // id задачи
        private String mName; // название задачи
        private String mDescription; // описание задачи
        private int mNeedFoto;
        private int mNeedData;

        public TaskInfo(int id, String name, String description, int NeedFoto, int NeedData) {
            mid = id;
            mName = name;
            mDescription = description;
            mNeedFoto = NeedFoto;
            mNeedData = NeedData;
        }

        public TaskInfo(Parcel in) {
            String[] dataS = new String[2];
            int[] dataI = new int[3];

            in.readStringArray(dataS);
            in.readIntArray(dataI);

            mid = dataI[0];
            mNeedFoto = dataI[1];
            mNeedData = dataI[2];
            mName = dataS[0];
            mDescription = dataS[1];

        }

        public void setTaskId(int id) {
            mid = id;
        }

        public int getTaskId() {
            return mid;
        }

        public void setTaskName(String name) {
            mName = name;
        }

        public String getTaskName() {
            return mName;
        }

        public void setTaskDescription(String description) {
            mDescription = description;
        }

        public String getTaskDescription() {
            return mDescription;
        }

        public void setTaskNeedFoto(int NeedFoto) {
            mNeedFoto = NeedFoto;
        }

        public int getTaskNeedFoto() {
            return mNeedFoto;
        }

        public void setTaskNeedData(int NeedData) {
            mNeedData = NeedData;
        }

        public int getTaskNeedData() {
            return mNeedData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(new String[] {  mName, mDescription });
            dest.writeIntArray(new int[] { mid, mNeedFoto, mNeedData });
        }

        public static final Parcelable.Creator<TaskInfo> CREATOR = new Parcelable.Creator<TaskInfo>() {

            @Override
            public TaskInfo createFromParcel(Parcel source) {
                return new TaskInfo(source);
            }

            @Override
            public TaskInfo[] newArray(int size) {
                return new TaskInfo[size];
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_layout);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Set the Action Bar to use tabs for navigation
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4190AB")));
        ab.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#3A8098")));

        Log.d("СОЗДАТЬ", "ОК");

        // находим список
        lvTasks = (ListView) findViewById(R.id.lvTasks);
        tvTaskListEmpty = (TextView) findViewById(R.id.tvTaskListEmpty);

        idQuest = getIntent().getStringExtra("idQuest");
        nameQuest = getIntent().getStringExtra("nameQuest");
        idUEQ = getIntent().getStringExtra("idUEQ");

        ab.setTitle(nameQuest);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
           fillData(idQuest);
        }
        else{
            tvTaskListEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Проверьте соединение с интернетом", Toast.LENGTH_LONG).show();
        }

        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Task value = (Task) taskAdapter.getItem(position);

                //Log.d("Время 2","результат " + checkTime(value.TimeStart, value.TimeEnd));

                if(value.isTaskActive == 1 && checkTime(value.TimeStart, value.TimeEnd)){

                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);

                intent.putExtra("TaskInfo", new TaskInfo(value.id, value.name, value.description, value.NeedFoto, value.NeedData));
                intent.putExtra("idQuest", idQuest);
                intent.putExtra("nameQuest", nameQuest);
                intent.putExtra("idUEQ", idUEQ);

                startActivity(intent);
                }
                else{
                    view.setEnabled(false);
                }

            }
        });


    }


    private boolean checkTime(String timeStart, String timeEnd) {
        DateFormat formatter = new SimpleDateFormat("kk:mm");

        String currentTime = formatter.format(System.currentTimeMillis());

        try {

            java.sql.Time tS = new java.sql.Time(formatter.parse(timeStart).getTime());
            java.sql.Time tE = new java.sql.Time(formatter.parse(timeEnd).getTime());
            java.sql.Time tC = new java.sql.Time(formatter.parse(currentTime).getTime());

           // Log.d("Время","старт " + tS + " текущее " + tC + " конец " + tE);

            return tC.after(tS) && tC.before(tE);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("УНИЧТОЖИТЬ", "ДА");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("СТОП", "");
      //  taskAdapter.clearData();
       // taskAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("СТАРТ", "");
     /*   ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("При возвращении", idQuestReturned);
            fillData(idQuestReturned);
        }*/
    }


    // генерируем данные для адаптера
    void fillData(String idQuest) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getTasks = new GetTasks(idQuest);
       // Toast.makeText(this, "id2: " + idQuest, Toast.LENGTH_LONG).show();
        getTasks.execute();

    }

    public class GetTasks extends AsyncTask<String, Void, String> {

        String tasks, idQuest;
        ConnectSupport cs = new ConnectSupport();


        public GetTasks(String idQuest) {
            this.tasks = "";
            this.idQuest = idQuest;


        }

        @Override
        protected String doInBackground(String... params) {

            try {

                //Log.d("Параметр для задач2: ", idQuest);

                tasks = cs.getTasks(idQuest, idUEQ);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            return tasks;
        }

        @Override
        protected void onPostExecute(String tasks) {

            if(tasks.contains(";")){
                String[] parts = tasks.split(";");

                for(int q = 0; q  < parts.length-1; q++){

                    String[] task = parts[q].split(":");
                    tasksList.add(new Task(Integer.parseInt(task[0]), task[1], task[2], task[3].replace(".",":"), task[4].replace(".",":"), Integer.parseInt(task[5]), Integer.parseInt(task[6]), task[7], Integer.parseInt(task[8]), Integer.parseInt(task[9]), Integer.parseInt(task[10])));

                }

            }

            if(tasksList.toArray().length == 0){
                tvTaskListEmpty.setVisibility(View.VISIBLE);
            }else{
                tvTaskListEmpty.setVisibility(View.INVISIBLE);
            }


            taskAdapter = new TaskAdapter(getBaseContext(),tasksList);

            lvTasks.setAdapter(taskAdapter);

            getTasks = null;

            //  showProgress(false);

        }

        @Override
        protected void onCancelled() {
            getTasks = null;
            // showProgress(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
