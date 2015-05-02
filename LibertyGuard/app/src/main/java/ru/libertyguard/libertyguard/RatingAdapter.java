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
 * Адаптер для рейтинга
 */
public class RatingAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Rating> objects;

    RatingAdapter(Context context, ArrayList<Rating> ratings) {
        ctx = context;
        objects = ratings;
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
            view = lInflater.inflate(R.layout.list_rating_item, parent, false);
        }

        Rating r = getRating(position);

        TextView tvRatingPlace = (TextView) view.findViewById(R.id.tvRatingPlace);
        TextView tvRatingExp = (TextView) view.findViewById(R.id.tvRatingExp);
        TextView tvRatingName = (TextView) view.findViewById(R.id.tvRatingName);

        // заполняем View в пункте списка данными из рейтинга
        tvRatingPlace.setText("" + (position+1));
        tvRatingExp.setText("" + r.exp);
        tvRatingName.setText(r.name);

        return view;
    }

    // квест по позиции
    Rating getRating(int position) {
        return ((Rating) getItem(position));
    }


    public void clearData() {
        // clear the data
        objects.clear();
    }

}
