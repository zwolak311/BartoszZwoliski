package com.bartosz.bartoszzwoliski.tabel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bartosz.bartoszzwoliski.API.Caller;
import com.bartosz.bartoszzwoliski.API.LeagueTablePOJO;
import com.bartosz.bartoszzwoliski.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueTable extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipeController.SwipeControllerListener, CurrentLeagueAdapter.TableLeagueInterface{
    @BindView(R.id.recycleView2) RecyclerView recyclerView;
    @BindView(R.id.swipeTable) SwipeRefreshLayout swipeRefreshLayout;

    String leagueId;
    LeagueTablePOJO currentLeagues = new LeagueTablePOJO();
    CurrentLeagueAdapter adapter;
    Caller api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leauge_table, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        api = new Caller();
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        assert getArguments() != null;
        leagueId = getArguments().getString("leagueId");
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setupAdapter(currentLeagues);
        getLeagueTable();
    }

    @Override
    public void onRefresh() {
        getLeagueTable();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sorting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.positionItem:

                Collections.sort(currentLeagues.getStanding(), new Comparator<LeagueTablePOJO.Standing>() {
                    @Override
                    public int compare(LeagueTablePOJO.Standing o1, LeagueTablePOJO.Standing o2) {
                        Integer i1 = o1.getPosition();
                        Integer i2 = o2.getPosition();

                        return i1.compareTo(i2);
                    }
                });

                break;
            case R.id.nameItem:

                Collections.sort(currentLeagues.getStanding(), new Comparator<LeagueTablePOJO.Standing>() {
                    @Override
                    public int compare(LeagueTablePOJO.Standing o1, LeagueTablePOJO.Standing o2) {
                        return o1.getTeamName().compareTo(o2.getTeamName());
                    }
                });

                break;
            case R.id.lossesItem:


                Collections.sort(currentLeagues.getStanding(), new Comparator<LeagueTablePOJO.Standing>() {
                    @Override
                    public int compare(LeagueTablePOJO.Standing o1, LeagueTablePOJO.Standing o2) {

                        Integer i1 = o1.getLosses();
                        Integer i2 = o2.getLosses();

                        return i2.compareTo(i1);
                    }
                });

                break;

        }

        adapter.notifyDataSetChanged();
        return true;
    }

    void getLeagueTable(){

        Call<LeagueTablePOJO> call = api.getApi().getLeagueTable(leagueId);

        call.clone().enqueue(new Callback<LeagueTablePOJO>() {
            @Override
            public void onResponse(@NonNull Call<LeagueTablePOJO> call, @NonNull Response<LeagueTablePOJO> response) {

                if(response.body() != null) {
                    currentLeagues = response.body();
                    adapter.notifyListDataChanged(currentLeagues);
                }

            }

            @Override
            public void onFailure(@NonNull Call<LeagueTablePOJO> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }



    void setupAdapter(LeagueTablePOJO currentLeagues){

        ItemTouchHelper.SimpleCallback simpleCallback = new SwipeController(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);


        adapter = new CurrentLeagueAdapter(getActivity(), currentLeagues, this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(getActivity()),
                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onDateSet() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
