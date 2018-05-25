package com.bartosz.bartoszzwoliski.tabel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bartosz.bartoszzwoliski.POJO.LeagueTablePOJO;
import com.bartosz.bartoszzwoliski.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentLeagueAdapter extends RecyclerView.Adapter<CurrentLeagueAdapter.CurrentLeagueHolder> {
    private LayoutInflater inflater;
    private LeagueTablePOJO arrayList;
    private TableLeagueInterface tableLeagueInterface;
    private Activity activity;


    public void notifyListDataChanged(LeagueTablePOJO arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
        tableLeagueInterface.onDateSet();
    }

    CurrentLeagueAdapter(Activity activity, LeagueTablePOJO arrayList, TableLeagueInterface tableLeagueInterface) {
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(activity);
        this.tableLeagueInterface = tableLeagueInterface;
        this.activity = activity;
    }

    class CurrentLeagueHolder extends RecyclerView.ViewHolder {
        LeagueTablePOJO.Standing standing;

        @BindView(R.id.foreground) public ConstraintLayout foreground;


        @BindView(R.id.position)
        TextView position;
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


        @SuppressLint("SetTextI18n")
        void bindView(int position){
            standing = arrayList.getStanding().get(position);


            this.position.setText(standing.getPosition() + "");
            this.name.setText(arrayList.getStanding().get(position).getTeamName());

            this.im.setText(standing.getPlayedGames());
            this.wm.setText(standing.getWins());
            this.rm.setText(standing.getDraws());
            this.pm.setText(standing.getLosses() + "");
            this.pkt.setText(standing.getPoints());


            String url = standing.getCrestURI();

            SvgLoader.pluck()
                    .with(activity)
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load(url, imageView);
        }
    }


    @NonNull
    @Override
    public CurrentLeagueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrentLeagueHolder(inflater.inflate(R.layout.league_ltable_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentLeagueHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.getStanding().size();
    }



    public interface TableLeagueInterface{
        void onDateSet();
    }
}
