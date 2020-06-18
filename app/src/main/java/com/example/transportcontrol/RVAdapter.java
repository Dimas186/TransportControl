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
    private LayoutInflater inflater;
    private List<DataModel> modelList;
    private Context mContext;

    RVAdapter(Context context, List<DataModel> list) {
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

    void removeItem(int position) {
        modelList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public List<DataModel> getModelList() {
        return modelList;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        TextView mainText, tvPlateNumber, tvInventoryNumber, tvGarageNumber;
        ImageView ivPhoto;

        MainViewHolder(View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.name);
            tvPlateNumber = itemView.findViewById(R.id.tvPlateNumber);
            tvInventoryNumber = itemView.findViewById(R.id.tvInventoryNumber);
            tvGarageNumber = itemView.findViewById(R.id.tvGarageNumber);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }

        void bindData(DataModel dataModel) {
            mainText.setText("Модель: " + dataModel.getBrand() + " " + dataModel.getVehicleType());
            tvPlateNumber.setText("Гос. №: " + dataModel.getPlateNumber());
            tvInventoryNumber.setText("Инвентарный №: " + dataModel.getInventoryNumber());
            tvGarageNumber.setText("Гаражный №: " + dataModel.getGarageNumber());
            Glide.with(mContext) //Takes the context
                    .asBitmap()  //Tells glide that it is a bitmap
                    .load((dataModel.getPhoto() != null) ? dataModel.getPhoto() : R.drawable.no_image)
                    .apply(new RequestOptions()
                            .override(400, 250)
                            .centerCrop()
                            .placeholder(R.drawable.progress_animation))
                    .into(ivPhoto);
        }
    }
}
