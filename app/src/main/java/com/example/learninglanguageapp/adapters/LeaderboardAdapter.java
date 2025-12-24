package com.example.learninglanguageapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Entities.TopUserEntity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter
        extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final Context context;
    private List<TopUserEntity> list = new ArrayList<>();

    public LeaderboardAdapter(Context context) {
        this.context = context;
    }

    public void submitList(List<TopUserEntity> users) {
        list = users != null ? users : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        TopUserEntity user = list.get(position);

        holder.tvName.setText(user.getFullName());
        holder.tvXP.setText(user.getTotalExperience() + " XP");

        Glide.with(context)
                .load(user.getAvatar())
                .placeholder(R.drawable.defaultavt)
                .into(holder.imgAvatar);

        int rank = user.getRank();
        holder.imgMedal.setVisibility(View.GONE);

        if (rank <= 8) {
            holder.imgMedal.setVisibility(View.VISIBLE);
            holder.imgMedal.setImageResource(getMedal(rank));
        }
    }

    private int getMedal(int rank) {
        switch (rank) {
            case 1: return R.drawable.ic_medal_gold;
            case 2: return R.drawable.ic_medal_silver;
            case 3: return R.drawable.ic_medal_bronze;
            case 4: return R.drawable.ic_medal_blue;
            case 5: return R.drawable.ic_medal_top5;
            case 6: return R.drawable.ic_medal_top6;
            case 7: return R.drawable.ic_medal_top7;
            case 8: return R.drawable.ic_medal_top8;
            default: return 0;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgMedal;
        CircleImageView imgAvatar;
        TextView tvName, tvXP;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMedal = itemView.findViewById(R.id.imgMedal);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvXP = itemView.findViewById(R.id.tvXP);
        }
    }
}
