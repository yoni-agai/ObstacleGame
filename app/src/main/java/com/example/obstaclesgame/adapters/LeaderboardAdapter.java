package com.example.obstaclesgame.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obstaclesgame.HighScore;
import com.example.obstaclesgame.R;
import com.example.obstaclesgame.interfaces.ILeaderboardListener;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private final ArrayList<HighScore> highScores;
    private ILeaderboardListener listener;

    public LeaderboardAdapter(ArrayList<HighScore> highScores, ILeaderboardListener listener) {
        this.highScores = highScores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeaderboardAdapter.LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_leaderboard_item, parent, false);
        LeaderboardAdapter.LeaderboardViewHolder vh = new LeaderboardAdapter.LeaderboardViewHolder(view);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = highScores.get(vh.getAdapterPosition()).getLatitude();
                double longitude = highScores.get(vh.getAdapterPosition()).getLongitude();
                listener.onLeaderboardItemClicked(latitude, longitude);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.LeaderboardViewHolder holder, int position) {
        HighScore highScore = highScores.get(position);
        holder.title.setText(String.valueOf(highScore.getScore()));

    }

    @Override
    public int getItemCount() {
        return highScores.size();
    }

    class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.li_leaderboard_title);
        }
    }
}

