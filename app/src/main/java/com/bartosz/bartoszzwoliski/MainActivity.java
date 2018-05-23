package com.bartosz.bartoszzwoliski;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bartosz.bartoszzwoliski.API.Caller;
import com.bartosz.bartoszzwoliski.API.CurrentLeauge;
import com.bartosz.bartoszzwoliski.tabel.LeagueTable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, LeagueListAdapter.LeagueInterface {
    @BindView(R.id.recycleView) RecyclerView recyclerView;
    @BindView(R.id.swipeMain) SwipeRefreshLayout swipeRefreshLayout;


    Caller api;
    LeagueListAdapter leagueListAdapter;
    ArrayList<CurrentLeauge> currentLeagues = new ArrayList<>();
    Call<ArrayList<CurrentLeauge>> leagueListPOJOCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        leagueListAdapter = new LeagueListAdapter(currentLeagues, this, this);
        setupAdapter();
        api = new Caller();
        leagueListPOJOCall = api.getApi().getLeagueList();

    }


    @Override
    protected void onResume() {
        super.onResume();

        getLeagueList();
    }


    @Override
    public void onRefresh() {
        getLeagueList();
    }


    @Override
    public void onItemClickListner(int position) {

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

    void getLeagueList(){

        leagueListPOJOCall.clone().enqueue(new Callback<ArrayList<CurrentLeauge>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<CurrentLeauge>> call, @NonNull Response<ArrayList<CurrentLeauge>> response) {

                if(response.body() != null) {
                    currentLeagues = response.body();
                    leagueListAdapter.notifyListDateChanged(currentLeagues);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<CurrentLeauge>> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    void setupAdapter(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(leagueListAdapter);
    }








}
