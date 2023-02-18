package com.sametcagriaktepe.afinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    String[] fruitList;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, ArrayList<String> fruitList) {
        this.context = ctx;
        this.fruitList = fruitList.toArray(new String[0]);
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return fruitList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView txtView = view.findViewById(R.id.textView);
        txtView.setText(fruitList[i]);
        txtView.setTextSize(16);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        return view;
    }
}
