package ru.libertyguard.libertyguard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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

        // заполняем View в пункте списка данными из квестов
        ((TextView) view.findViewById(R.id.tvTasksName)).setText(t.name);
        ((TextView) view.findViewById(R.id.tvTaskStartTime)).setText("Время начало: " + t.TimeStart);
        ((TextView) view.findViewById(R.id.tvTaskStartEnd)).setText("Время окончания: " + t.TimeEnd);
        ((TextView) view.findViewById(R.id.tvTasksExp)).setText("Опыт за задачу: " + t.exp);
        ((TextView) view.findViewById(R.id.tvTasksStatus)).setText("Статус: " + t.statusComplete);

        return view;
    }

    // квест по позиции
    Task getTask(int position) {
        return ((Task) getItem(position));
    }

    public void clearData() {
        // clear the data
        objects.clear();
    }

}
