package com.example.transportcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.transportcontrol.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public  class RVAdapter extends RecyclerView.Adapter<RVAdapter.MainViewHolder> {
    LayoutInflater inflater;
    List<DataModel> modelList;

    public RVAdapter(Context context, List<DataModel> list) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    public void removeItem(int position) {
        modelList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        TextView mainText, subText, costText;

        public MainViewHolder(View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.name);
            subText =  itemView.findViewById(R.id.description);
            costText =  itemView.findViewById(R.id.price);
        }

        public void bindData(DataModel expensesList) {
            mainText.setText(expensesList.getBrand());
            //subText.setText(expensesList.getCategory());
            //costText.setText(expensesList.getCost() + "р.");
        }
    }
}
