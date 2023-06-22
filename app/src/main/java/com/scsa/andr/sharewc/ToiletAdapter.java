package com.scsa.andr.sharewc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scsa.andr.sharewc.Toilet;

import java.util.List;

public class ToiletAdapter extends ArrayAdapter<Toilet> {
    private Context context;
    private List<Toilet> toiletList;

    public ToiletAdapter(Context context, List<Toilet> toiletList) {
        super(context, 0, toiletList);
        this.context = context;
        this.toiletList = toiletList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.item_toilet, parent, false);
        }

        Toilet currentToilet = toiletList.get(position);

        TextView nameTextView = listItemView.findViewById(R.id.nameTextView);
        TextView addressTextView = listItemView.findViewById(R.id.addressTextView);
        TextView validTextView = listItemView.findViewById(R.id.validTextView);

        nameTextView.setText(currentToilet.getName());
        addressTextView.setText(currentToilet.getAddress());
        validTextView.setText(String.valueOf(currentToilet.getId()));

        return listItemView;
    }
}
