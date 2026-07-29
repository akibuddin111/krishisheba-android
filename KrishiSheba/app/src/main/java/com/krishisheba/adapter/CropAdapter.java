package com.krishisheba.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krishisheba.R;
import com.krishisheba.activities.CropDetailActivity;
import com.krishisheba.models.Crop;

import java.util.List;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.ViewHolder> {

    private Context context;
    private List<Crop> cropList;

    public CropAdapter(Context context, List<Crop> cropList) {
        this.context = context;
        this.cropList = cropList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_crop, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Crop crop = cropList.get(position);

        holder.tvCropName.setText(crop.getName());
        holder.tvCropArt.setText(getCropArt(crop.getName()));

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, CropDetailActivity.class);

            intent.putExtra("name", crop.getName());
            intent.putExtra("season", crop.getSeason());
            intent.putExtra("soil", crop.getSoil());
            intent.putExtra("description", crop.getDescription());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cropList != null ? cropList.size() : 0;
    }

    //   Used for search updates
    public void updateList(List<Crop> newList) {
        this.cropList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCropName;
        TextView tvCropArt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCropName = itemView.findViewById(R.id.tvCropName);
            tvCropArt = itemView.findViewById(R.id.tvCropArt);
        }
    }

    private String getCropArt(String cropName) {
        if ("Rice".equalsIgnoreCase(cropName)) {
            return "🌾";
        }
        if ("Maize".equalsIgnoreCase(cropName)) {
            return "🌽";
        }
        if ("Potato".equalsIgnoreCase(cropName)) {
            return "🥔";
        }
        if ("Wheat".equalsIgnoreCase(cropName)) {
            return "🌾";
        }
        return "🌱";
    }
}
