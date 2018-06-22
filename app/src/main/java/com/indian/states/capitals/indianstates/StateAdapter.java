package com.indian.states.capitals.indianstates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private List<String> stateNames;
    private Integer visibilityImg = 0;

    private final StateAdapterOnClickHandler stateAdapterOnClickHandler;
    private final OnFavClickListener onFavClickListener;

   /* StateAdapter(StateAdapterOnClickHandler stateAdapterOnClickHandler) {

        this.stateAdapterOnClickHandler = stateAdapterOnClickHandler;
        OnFavClickListener = ;
    }*/

    public interface StateAdapterOnClickHandler {
        void onItemClick(String state);
    }

    StateAdapter(StateAdapterOnClickHandler stateAdapterOnClickHandler, OnFavClickListener monFavClickListener) {

        this.stateAdapterOnClickHandler = stateAdapterOnClickHandler;
        this.onFavClickListener = monFavClickListener;
    }

    public interface OnFavClickListener {
         void OnFavclicked(String state);
    }


    @Override
    public StateAdapter.StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForListItem = R.layout.layout_states;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idForListItem, parent, false);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.StateViewHolder holder, int position) {
        String stateName = stateNames.get(position);
        holder.mTextView.setText(stateName);

        if (visibilityImg == 1) {
            holder.mImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (stateNames == null) return 0;

        return stateNames.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextView;
        public final ImageView mImageView;

        public StateViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_state_name);
            mImageView = (ImageView) itemView.findViewById(R.id.bookmark_fav);
            mTextView.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPostion = getAdapterPosition();
            String state = stateNames.get(clickedPostion);
            if (visibilityImg != 1) {
                stateAdapterOnClickHandler.onItemClick(state);
            } else if(visibilityImg == 1){


                switch (v.getId()) {
                    case R.id.tv_state_name:
                        stateAdapterOnClickHandler.onItemClick(state);
                        break;
                    case R.id.bookmark_fav:
                        onFavClickListener.OnFavclicked(state);
                        break;
                }
            }
        }
    }

    public void setStateNames(List<String> states) {
        stateNames = states;
        notifyDataSetChanged();
    }

    public void setBookmarkVisibility(Integer x) {
        this.visibilityImg = x;
    }
}
