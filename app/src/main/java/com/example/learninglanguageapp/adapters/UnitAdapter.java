package com.example.learninglanguageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Unit;

import java.util.List;

public class UnitAdapter extends BaseAdapter {

    private Context context;
    private List<Unit> unitList;

    public UnitAdapter(Context context, List<Unit> unitList) {
        this.context = context;
        this.unitList = unitList;
    }

    @Override
    public int getCount() {
        return unitList.size();
    }

    @Override
    public Object getItem(int position) {
        return unitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return unitList.get(position).getUnitId();
    }

    static class ViewHolder {
        ImageView imgUnit;
        TextView tvUnitName, tvUnitDesc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_unit, parent, false);

            holder = new ViewHolder();
            holder.imgUnit = convertView.findViewById(R.id.imgUnit);
            holder.tvUnitName = convertView.findViewById(R.id.tvUnitName);
            holder.tvUnitDesc = convertView.findViewById(R.id.tvUnitDesc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Unit unit = unitList.get(position);

        // Set text
        holder.tvUnitName.setText("Unit " + unit.getOrderIndex());
        holder.tvUnitDesc.setText(unit.getUnitName());

        // Set image theo index (bạn tự thay các ảnh vào drawable)
        switch (unit.getOrderIndex()) {
            case 1:
                holder.imgUnit.setImageResource(R.drawable.ic_animal);
                break;
            case 2:
                holder.imgUnit.setImageResource(R.drawable.ic_family);
                break;
            case 3:
                holder.imgUnit.setImageResource(R.drawable.ic_food);
                break;
            case 4:
                holder.imgUnit.setImageResource(R.drawable.ic_weather);
                break;
            case 5:
                holder.imgUnit.setImageResource(R.drawable.ic_dailyactivity);
                break;
        }
        return convertView;
    }
}
