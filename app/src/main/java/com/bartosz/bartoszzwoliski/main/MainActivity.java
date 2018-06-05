package com.bartosz.bartoszzwoliski.main;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bartosz.bartoszzwoliski.POJO.LeagueSimpleNamePOJO;
import com.bartosz.bartoszzwoliski.API.LeagueListInterface;
import com.bartosz.bartoszzwoliski.API.MyRetrofit;
import com.bartosz.bartoszzwoliski.R;
import com.bartosz.bartoszzwoliski.tabel.LeagueTable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, LeagueListAdapter.LeagueInterface,
        LeagueListInterface {
    @BindView(R.id.recycleView) RecyclerView recyclerView;
    @BindView(R.id.swipeMain) SwipeRefreshLayout swipeRefreshLayout;


    LeagueListAdapter leagueListAdapter;

    ArrayList<LeagueSimpleNamePOJO> currentLeagues = new ArrayList<>();

    MyRetrofit api;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        leagueListAdapter = new LeagueListAdapter(currentLeagues, this, this);
        setupAdapter();

        api = new MyRetrofit(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        api.getLeagueList();
    }


    @Override
    public void onRefresh() {
        api.getLeagueList();
    }


    @Override
    public void onItemClickListener(int position) {

        LeagueTable leagueTable = new LeagueTable();

        Bundle bundle = new Bundle();
        bundle.putString("leagueId", currentLeagues.get(position).getId() + "");

        leagueTable.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.main, leagueTable).addToBackStack(null).commit();
    }

    @Override
    public void onDateSet() {
        swipeRefreshLayout.setRefreshing(false);
    }


    void setupAdapter(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(leagueListAdapter);
    }


    @Override
    public void onResponse(ArrayList<LeagueSimpleNamePOJO> currentLeagues) {
        if(currentLeagues != null){
            this.currentLeagues = currentLeagues;
            leagueListAdapter.notifyListDateChanged(currentLeagues);
        }

        if(snackbar != null)
            snackbar.dismiss();

    }


    @Override
    public void onFailure(String message) {
        swipeRefreshLayout.setRefreshing(false);
        snackbar = Snackbar
                .make(getCurrentFocus(), message, Snackbar.LENGTH_INDEFINITE);

        snackbar.show();
    }


}
