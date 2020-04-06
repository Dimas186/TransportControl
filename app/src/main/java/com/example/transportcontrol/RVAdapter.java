package com.example.transportcontrol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.transportcontrol.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public  class RVAdapter extends RecyclerView.Adapter<RVAdapter.MainViewHolder> {
    LayoutInflater inflater;
    List<DataModel> modelList;
    Context mContext;

    public RVAdapter(Context context, List<DataModel> list) {
        mContext = context;
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

        TextView mainText;
        ImageView ivPhoto;

        public MainViewHolder(View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.name);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }

        public void bindData(DataModel dataModel) {
            mainText.setText(dataModel.getBrand());
            Glide.with(mContext) //Takes the context
                    .asBitmap()  //Tells glide that it is a bitmap
                    .load(dataModel.getPhoto())
                    .apply(new RequestOptions()
                            .override(375, 250)
                            .centerCrop()
                            .placeholder(R.drawable.progress_animation))
                    .into(ivPhoto);
        }
    }
}
