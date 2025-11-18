package com.example.learninglanguageapp.adapters;


import android.annotation.SuppressLint;
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
import com.example.learninglanguageapp.models.UIModel.LeaderBoardItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private Context context;
    private List<LeaderBoardItem> leaderboardList;

    public LeaderboardAdapter(Context context, List<LeaderBoardItem> leaderboardList) {
        this.context = context;
        this.leaderboardList = leaderboardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderBoardItem item = leaderboardList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvXP.setText(item.getXp() + " XP");

        // Load avatar
        int avatarResId = context.getResources().getIdentifier(
                item.getAvatarUrl(),
                "drawable",
                context.getPackageName()
        );
        holder.imgAvatar.setImageResource(avatarResId != 0 ? avatarResId : R.drawable.defaultavt);

        int rank = item.getRank();

        // 🔥 Reset để tránh recycle lỗi
        holder.imgMedal.setVisibility(View.GONE);
        holder.tvRank.setVisibility(View.GONE);

        if (rank <= 8) {
            // Top 5 hiển thị hình
            holder.imgMedal.setVisibility(View.VISIBLE);

            switch (rank) {
                case 1:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_gold);
                    break;
                case 2:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_silver);
                    break;
                case 3:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_bronze);
                    break;
                case 4:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_blue);
                    break;
                case 5:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_top5);
                    break;
                case 6:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_top6);
                    break;
                case 7:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_top7);
                    break;
                case 8:
                    holder.imgMedal.setImageResource(R.drawable.ic_medal_top8);
                    break;
            }

        }
    }


    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public void updateList(List<LeaderBoardItem> newList) {
        this.leaderboardList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMedal;
        TextView tvRank;
        CircleImageView imgAvatar;
        TextView tvName;
        TextView tvXP;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMedal = itemView.findViewById(R.id.imgMedal);
            tvRank = itemView.findViewById(R.id.tvRank);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvXP = itemView.findViewById(R.id.tvXP);
        }
    }
}