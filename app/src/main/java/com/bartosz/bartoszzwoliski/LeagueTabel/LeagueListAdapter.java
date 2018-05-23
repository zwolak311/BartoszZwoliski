package com.bartosz.bartoszzwoliski.LeagueTabel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bartosz.bartoszzwoliski.API.CurrentLeauge;
import com.bartosz.bartoszzwoliski.LeagueTable;
import com.bartosz.bartoszzwoliski.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeagueListAdapter extends RecyclerView.Adapter<LeagueListAdapter.LeagueListHolder> {
    ArrayList<CurrentLeauge> currentLeagues;
    LayoutInflater layoutInflater;


    public void notifyListDateChanged(ArrayList<CurrentLeauge> currentLeagues){
        this.currentLeagues = currentLeagues;
        notifyDataSetChanged();
//        swipeRefreshLayout.setRefreshing(false);
    }

    public LeagueListAdapter(ArrayList<CurrentLeauge> currentLeagues, Context context) {
        this.currentLeagues = currentLeagues;
        this.layoutInflater = LayoutInflater.from(context);
    }

    class LeagueListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.position)
        TextView textView;
        int position;


        LeagueListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        void bindView(int position){
            this.position = position;
            textView.setText(currentLeagues.get(position).getCaption());
        }


        @OnClick(R.id.leagueLayout)
        void onClick(){
            LeagueTable leagueTable = new LeagueTable();

            Bundle bundle = new Bundle();
            bundle.putString("leagueId", currentLeagues.get(position).getId() + "");

            leagueTable.setArguments(bundle);

//            getSupportFragmentManager().beginTransaction().replace(R.id.main, leagueTable).addToBackStack(null).commit();

        }

    }


    @NonNull
    @Override
    public LeagueListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeagueListHolder(layoutInflater.inflate(R.layout.league_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeagueListHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return currentLeagues.size();
    }
}