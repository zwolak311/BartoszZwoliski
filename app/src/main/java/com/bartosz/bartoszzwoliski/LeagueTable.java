package com.bartosz.bartoszzwoliski;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bartosz.bartoszzwoliski.API.Caller;
import com.bartosz.bartoszzwoliski.API.LeagueTablePOJO;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueTable extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipeController.SwipeControllerListener{
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
                        return o2.getPoints().compareTo(o1.getPoints());
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
                        return o1.getLosses().compareTo(o2.getLosses());
                    }
                });

                break;

        }

        adapter.notifyDataSetChanged();
        return true;
    }




    void getLeagueTable(){

        Call<LeagueTablePOJO> call = api.getApi().getLeagueTable(leagueId);

        call.enqueue(new Callback<LeagueTablePOJO>() {
            @Override
            public void onResponse(@NonNull Call<LeagueTablePOJO> call, @NonNull Response<LeagueTablePOJO> response) {

                if(response.body().getStanding() != null) {
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


        adapter = new CurrentLeagueAdapter(getActivity(), currentLeagues);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(getActivity()),
                DividerItemDecoration.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }





    public class CurrentLeagueAdapter extends RecyclerView.Adapter<CurrentLeagueAdapter.CurrentLeagueHolder> {
        LayoutInflater inflater;
        LeagueTablePOJO arrayList;


        public void notifyListDataChanged(LeagueTablePOJO arrayList){
            this.arrayList = arrayList;
            notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }

        CurrentLeagueAdapter(Context context, LeagueTablePOJO arrayList) {
            this.arrayList = arrayList;
            this.inflater = LayoutInflater.from(context);
        }

        class CurrentLeagueHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.foreground) public ConstraintLayout foregound;


            @BindView(R.id.position) TextView position;
            @BindView(R.id.name) TextView name;

            @BindView(R.id.im) TextView im;
            @BindView(R.id.wm) TextView wm;
            @BindView(R.id.rm) TextView rm;
            @BindView(R.id.pm) TextView pm;
            @BindView(R.id.pkt) TextView pkt;


            @BindView(R.id.imageView)ImageView imageView;


            CurrentLeagueHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }


            void bindView(int position){
                Log.d("myRest", arrayList.getStanding().get(position).getCrestURI());

                this.position.setText(arrayList.getStanding().get(position).getPosition());
                this.name.setText(arrayList.getStanding().get(position).getTeamName());

                this.im.setText(arrayList.getStanding().get(position).getPlayedGames());
                this.wm.setText(arrayList.getStanding().get(position).getWins());
                this.rm.setText(arrayList.getStanding().get(position).getDraws());
                this.pm.setText(arrayList.getStanding().get(position).getLosses());
                this.pkt.setText(arrayList.getStanding().get(position).getPoints());


                String url = arrayList.getStanding().get(getAdapterPosition()).getCrestURI();

                SvgLoader.pluck()
                        .with(getActivity())
                        .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                        .load(url, imageView);
            }


            @OnTouch({R.id.foreground})
            boolean onTouch(){

                String url = arrayList.getStanding().get(getAdapterPosition()).getCrestURI();

                SvgLoader.pluck()
                        .with(getActivity())
                        .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                        .load(url, imageView);

                return true;
            }
        }


        @NonNull
        @Override
        public CurrentLeagueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CurrentLeagueHolder(inflater.inflate(R.layout.league_list_item2, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CurrentLeagueHolder holder, int position) {
            holder.bindView(position);
        }

        @Override
        public int getItemCount() {
            return arrayList.getStanding().size();
        }
    }


}
