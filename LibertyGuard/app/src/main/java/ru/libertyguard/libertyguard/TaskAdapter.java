package ru.libertyguard.libertyguard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Лев on 14.04.2015.
 * Адаптер задач
 */
public class TaskAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Task> objects;

    TaskAdapter(Context context, ArrayList<Task> tasks) {
        ctx = context;
        objects = tasks;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_tasks_item, parent, false);
        }

        Task t = getTask(position);

        TextView tvTasksName = (TextView) view.findViewById(R.id.tvTasksName);
        TextView tvTaskStartTime = (TextView) view.findViewById(R.id.tvTaskStartTime);
        TextView tvTaskStartEnd = (TextView) view.findViewById(R.id.tvTaskStartEnd);
        TextView tvTasksExp = (TextView) view.findViewById(R.id.tvTasksExp);
        TextView tvTasksStatus = (TextView) view.findViewById(R.id.tvTasksStatus);

        // заполняем View в пункте списка данными из квестов
        tvTasksName.setText(t.name);
        tvTaskStartTime.setText("Время начало: " + t.TimeStart);
        tvTaskStartEnd.setText("Время окончания: " + t.TimeEnd);
        tvTasksExp.setText("Опыт за задачу: " + t.exp);
        tvTasksStatus.setText("Статус: " + t.moderationStatusName);


        if(t.isTaskActive == 1 && checkTime(t.TimeStart,t.TimeEnd)){

            view.setEnabled(true);
            tvTasksName.setEnabled(true);
            tvTaskStartTime.setEnabled(true);
            tvTaskStartEnd.setEnabled(true);
            tvTasksExp.setEnabled(true);
            tvTasksStatus.setEnabled(true);

        }
        else{
            view.setEnabled(false);
            tvTasksName.setEnabled(false);
            tvTaskStartTime.setEnabled(false);
            tvTaskStartEnd.setEnabled(false);
            tvTasksExp.setEnabled(false);
            tvTasksStatus.setEnabled(false);
        }

        return view;
    }

    // квест по позиции
    Task getTask(int position) {
        return ((Task) getItem(position));
    }

    private boolean checkTime(String timeStart, String timeEnd) {
        DateFormat formatter = new SimpleDateFormat("kk:mm");
        String currentTime = formatter.format(System.currentTimeMillis());

        try {

            java.sql.Time tS = new java.sql.Time(formatter.parse(timeStart).getTime());
            java.sql.Time tE = new java.sql.Time(formatter.parse(timeEnd).getTime());
            java.sql.Time tC = new java.sql.Time(formatter.parse(currentTime).getTime());

            //Log.d("Время","старт " + tS + " текущее " + tC + " конец " + tE);

            return tC.after(tS)&&tC.before(tE);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;

    }

    public void clearData() {
        // clear the data
        objects.clear();
    }

}
