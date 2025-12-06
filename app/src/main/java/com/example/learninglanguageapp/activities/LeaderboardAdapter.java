package com.example.learninglanguageapp.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Response.UserResponse;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<UserResponse> list = new ArrayList<>();

    public void submitList(List<UserResponse> newList) {
        this.list = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserResponse user = list.get(position);
        int rank = position + 1;

        holder.tvName.setText(user.getFullName());
        holder.tvXP.setText(user.getTotalExperience() + " XP");

        // Top 3: huy chương
        if (rank == 1) {
            holder.imgMedal.setImageResource(R.drawable.ic_medal_gold);
            holder.imgMedal.setVisibility(View.VISIBLE);
            holder.tvRank.setVisibility(View.GONE);
        } else if (rank == 2) {
            holder.imgMedal.setImageResource(R.drawable.ic_medal_silver);
            holder.imgMedal.setVisibility(View.VISIBLE);
            holder.tvRank.setVisibility(View.GONE);
        } else if (rank == 3) {
            holder.imgMedal.setImageResource(R.drawable.ic_medal_bronze);
            holder.imgMedal.setVisibility(View.VISIBLE);
            holder.tvRank.setVisibility(View.GONE);
        } else {
            holder.imgMedal.setVisibility(View.GONE);
            holder.tvRank.setVisibility(View.VISIBLE);
            holder.tvRank.setText(String.valueOf(rank));

            // Top 5: nền tím đậm, còn lại xám
            if (rank <= 5) {
                holder.tvRank.setBackgroundResource(R.drawable.bg_rank_top5); // màu tím
            } else {
                holder.tvRank.setBackgroundResource(R.drawable.bg_rank_circle); // xám
            }
        }

        // Avatar
        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(avatarUrl)
                    .placeholder(R.drawable.defaultavt)
                    .error(R.drawable.defaultavt)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.defaultavt);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        ImageView imgMedal;
        TextView tvRank, tvName, tvXP;

        ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgMedal = itemView.findViewById(R.id.imgMedal);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvName = itemView.findViewById(R.id.tvName);
            tvXP = itemView.findViewById(R.id.tvXP);
        }
    }
}