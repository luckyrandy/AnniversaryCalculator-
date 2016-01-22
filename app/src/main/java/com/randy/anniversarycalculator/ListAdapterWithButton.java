package com.randy.anniversarycalculator;


// 어댑터 클래스

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class ListAdapterWithButton extends BaseAdapter {
    private ArrayList<Item> arrayItem;
    private Context context;

    public interface onButtonClickListener {
        void onButtonDelClick(Item item);
    }

    private onButtonClickListener adptCallback = null;

    public void setOnButtonClickListener( onButtonClickListener callback) {
        adptCallback = callback;
    }


    public ListAdapterWithButton(Context context, int resource, ArrayList<Item> objects) {
        //super(context, resource, objects);
        this.arrayItem = objects;
        this.context = context;
    }

    public int getCount() {
        return arrayItem.size();
    }

    public Object getItem(int position) {
        return arrayItem.get(position).getId();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View v = convertView;

        if (v == null) {
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_layout, null);
        }

        final Item item = arrayItem.get(pos);

        if (item != null) {
            TextView text_view = (TextView) v.findViewById(R.id.text_list);
            text_view.setText(arrayItem.get(pos).getsListText());

            Button btn_del = (Button) v.findViewById(R.id.btn_del);

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adptCallback != null)
                        adptCallback.onButtonDelClick(item);
                }
            });
        }

        return v;
    }

    // Adapter가 관리하는 Data List를 교체 한다.
    // 교체 후 Adapter.notifyDataSetChanged() 메서드로 변경 사실을
    // Adapter에 알려 주어 ListView에 적용 되도록 한다.
    public void setArrayList(ArrayList<Item> objects){
        this.arrayItem = objects;
    }
}

