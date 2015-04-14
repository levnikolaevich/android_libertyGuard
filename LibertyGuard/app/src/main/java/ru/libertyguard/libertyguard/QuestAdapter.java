package ru.libertyguard.libertyguard;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Quest> objects;

    QuestAdapter(Context context, ArrayList<Quest> quests) {
        ctx = context;
        objects = quests;
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
            view = lInflater.inflate(R.layout.list_quest_item, parent, false);
        }

        Quest q = getQuest(position);

        // заполняем View в пункте списка данными из квестов
        ((TextView) view.findViewById(R.id.tvQuestName)).setText(q.name);
        ((TextView) view.findViewById(R.id.tvQuestDate)).setText("Дата: " + q.startDate);
        ((TextView) view.findViewById(R.id.tvQuestTaskCount)).setText("Кол-во задач: " + q.countTask);
        ((TextView) view.findViewById(R.id.tvQuestExp)).setText("Опыт за выполнение квеста: " + q.exp);
        ((TextView) view.findViewById(R.id.tvQuestStatus)).setText("Статус: " + q.statusComplete);

        return view;
    }

    // квест по позиции
    Quest getQuest(int position) {
        return ((Quest) getItem(position));
    }

    public void clearData() {
        // clear the data
        objects.clear();
    }

}
