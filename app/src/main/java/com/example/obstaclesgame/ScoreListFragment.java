package com.example.obstaclesgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obstaclesgame.adapters.LeaderboardAdapter;
import com.example.obstaclesgame.interfaces.ILeaderboardListener;

import java.util.ArrayList;


public class ScoreListFragment extends Fragment implements ILeaderboardListener {

    private RecyclerView leaderboardRv;
    private LeaderboardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<HighScore> highScores = SharedPreferencesManager.getLeaderBoards(requireContext());

        adapter = new LeaderboardAdapter(highScores, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        leaderboardRv = view.findViewById(R.id.list);
        leaderboardRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        leaderboardRv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onLeaderboardItemClicked(double latitude, double longitude) {
        ((LeaderboardActivity)requireActivity()).onLeaderboardItemClicked(latitude , longitude);
    }
}