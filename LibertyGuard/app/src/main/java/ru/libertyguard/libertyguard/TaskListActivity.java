package ru.libertyguard.libertyguard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class TaskListActivity extends ActionBarActivity {

    TaskAdapter taskAdapter;
    ArrayList<Task> tasksList = new ArrayList<>();
    private GetTasks getTasks;

    ListView lvTasks;
    TextView tvTaskListEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_layout);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // находим список
        lvTasks = (ListView) findViewById(R.id.lvTasks);
        tvTaskListEmpty = (TextView) findViewById(R.id.tvTaskListEmpty);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Toast.makeText(this, "id: " + id, Toast.LENGTH_LONG).show();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
           // fillData();
        }
        else{
            tvTaskListEmpty.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Проверьте соединение с интернетом", Toast.LENGTH_LONG).show();
        }


    }



    // генерируем данные для адаптера
    void fillData() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getTasks = new GetTasks("");
        getTasks.execute((Void) null);

    }

    public class GetTasks extends AsyncTask<Void, Void, String> {

        String tasks;
        ConnectSupport cs = new ConnectSupport();

        public GetTasks(String quests) {
            this.tasks = quests;
        }

        @Override
        protected String doInBackground(Void... params) {

            try {


                tasks = cs.getQuests(getSharedPreferences("libertyguardfile", MODE_PRIVATE).getString("saved_login",""));


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            return tasks;
        }

        @Override
        protected void onPostExecute(String quests) {

            if(tasks.contains(";")){
                String[] parts = tasks.split(";");

                for(int q = 0; q  < parts.length-1; q++){
                    Log.d("Задача" + q, parts[q]);
                    String[] task = parts[q].split(":");

                    for(int r =0; r < task.length; r++){
                        Log.d("Задача",task[r]);
                    }

                    tasksList.add(new Task(Integer.parseInt(task[0]),task[1],task[2],task[3],task[4],Integer.parseInt(task[5]),Integer.parseInt(task[6]),task[7],Integer.parseInt(task[8]),Integer.parseInt(task[9]),Integer.parseInt(task[10])));

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
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
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
