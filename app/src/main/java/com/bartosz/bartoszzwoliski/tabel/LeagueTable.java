package com.bartosz.bartoszzwoliski.tabel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import com.bartosz.bartoszzwoliski.API.LeagueTableInterface;
import com.bartosz.bartoszzwoliski.POJO.LeagueTablePOJO;
import com.bartosz.bartoszzwoliski.API.MyRetrofit;
import com.bartosz.bartoszzwoliski.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeagueTable extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, SwipeController.SwipeControllerListener,
        CurrentLeagueAdapter.TableLeagueInterface, LeagueTableInterface {

    @BindView(R.id.recycleView2) RecyclerView recyclerView;
    @BindView(R.id.swipeTable) SwipeRefreshLayout swipeRefreshLayout;

    String leagueId;
    LeagueTablePOJO leagueTable = new LeagueTablePOJO();
    CurrentLeagueAdapter adapter;
    MyRetrofit api;
    View view;
    Snackbar snackbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leauge_table, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        api = new MyRetrofit(this);

        assert getArguments() != null;
        leagueId = getArguments().getString("leagueId");

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setupAdapter(leagueTable);
        api.getLeagueTable(leagueId);
    }

    @Override
    public void onRefresh() {
        api.getLeagueTable(leagueId);
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
                Collections.sort(leagueTable.getStanding(), new Comparator<LeagueTablePOJO.Standing>() {
                    @Override
                    public int compare(LeagueTablePOJO.Standing o1, LeagueTablePOJO.Standing o2) {
                        Integer i1 = o1.getPosition();
                        Integer i2 = o2.getPosition();
                        return i1.compareTo(i2);
                    }
                });
                break;
            case R.id.nameItem:
                Collections.sort(leagueTable.getStanding(), new Comparator<LeagueTablePOJO.Standing>() {
                    @Override
                    public int compare(LeagueTablePOJO.Standing o1, LeagueTablePOJO.Standing o2) {
                        return o1.getTeamName().compareTo(o2.getTeamName());
                    }
                });
                break;
            case R.id.lossesItem:
                Collections.sort(leagueTable.getStanding(), new Comparator<LeagueTablePOJO.Standing>() {
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



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) { }



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


    @Override
    public void onResponse(LeagueTablePOJO leagueTable) {
        this.leagueTable = leagueTable;

        if(snackbar != null)
            snackbar.dismiss();

        adapter.notifyListDataChanged(leagueTable);
    }

    @Override
    public void onFailure(String message) {

        swipeRefreshLayout.setRefreshing(false);
        snackbar = Snackbar
                .make(this.getView(), message, Snackbar.LENGTH_INDEFINITE);

        snackbar.show();

    }
}
