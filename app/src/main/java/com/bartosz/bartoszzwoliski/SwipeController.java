package com.bartosz.bartoszzwoliski;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;

public class SwipeController extends ItemTouchHelper.SimpleCallback {
        private SwipeControllerListener listener;
        boolean swipeBack = false;

        public SwipeController(int dragDirs, int swipeDirs, SwipeControllerListener listener) {
            super(dragDirs, swipeDirs);
            this.listener = listener;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                final View foregroundView = ((LeagueTable.CurrentLeagueAdapter.CurrentLeagueHolder) viewHolder).foregound;

                getDefaultUIUtil().onSelected(foregroundView);
            }
        }

        @Override
        public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((LeagueTable.CurrentLeagueAdapter.CurrentLeagueHolder) viewHolder).foregound;
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final View foregroundView = ((LeagueTable.CurrentLeagueAdapter.CurrentLeagueHolder) viewHolder).foregound;
            getDefaultUIUtil().clearView(foregroundView);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
            final View foregroundView = ((LeagueTable.CurrentLeagueAdapter.CurrentLeagueHolder) viewHolder).foregound;

            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                    actionState, isCurrentlyActive);

            if (actionState == ACTION_STATE_SWIPE) {
                setTouchListener(recyclerView);
            }
        }


    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(RecyclerView recyclerView) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                return false;
            }
        });
    }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        }

        @Override
        public int convertToAbsoluteDirection(int flags, int layoutDirection) {

            if (swipeBack) {
                swipeBack = false;
                return 0;
            }

            return super.convertToAbsoluteDirection(flags, layoutDirection);
        }

        public interface SwipeControllerListener {
            void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
        }

}
