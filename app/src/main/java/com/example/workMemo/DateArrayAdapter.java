package com.example.workMemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DateArrayAdapter extends ArrayAdapter<Record> {

    private int mResource;
    private List<Record> mItems;
    private LayoutInflater mInflater;

    static class ViewHolder{
        TextView date;
        TextView work;
        TextView memo;
    }
    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param resource レイアウトファイルのリソースID
     * @param items リストビューの要素
     */
    public DateArrayAdapter(Context context, int resource, ArrayList<Record> items){
        super(context, resource, items);

        mResource = resource;
        mItems = items; //ArrayList -> List
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public DateArrayAdapter(Context context, int resource){
        super(context, resource);

        mResource = resource;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(List<Record> mItems) {
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Record getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Record record;
        //リストビューに表示する要素を取得
        record = getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(mResource, null);
            holder = new ViewHolder();
            holder.date = convertView.findViewById(R.id.textViewDate_DateLayout);
            holder.memo = convertView.findViewById(R.id.textViewMemo_DateLayout);
            holder.work = convertView.findViewById(R.id.textViewWork_DateLayout);
            convertView.setTag(holder);

            if (record.getMemo().equals("")){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                holder.memo.setLayoutParams(params);
            }else{
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.memo.setLayoutParams(params);
            }
        } else {
            holder = (ViewHolder)convertView.getTag();
            if (record.getMemo().equals("")){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                holder.memo.setLayoutParams(params);
            }else{
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                holder.memo.setLayoutParams(params);
            }
        }

        //date
        holder.date.setText(record.getDateString());

        //work
        holder.work.setText(record.getWork());

        //memo
        holder.memo.setText(record.getMemo());

        return convertView; //may not be null
    }
}
