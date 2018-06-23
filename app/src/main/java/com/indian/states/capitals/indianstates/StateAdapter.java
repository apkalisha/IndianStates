package com.indian.states.capitals.indianstates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private List<String> stateNames;
    private Integer visibilityImg;//to set the bookmark visibility

    private final StateAdapterOnClickHandler stateAdapterOnClickHandler;

    StateAdapter(StateAdapterOnClickHandler stateAdapterOnClickHandler, Integer visible) {

        this.stateAdapterOnClickHandler = stateAdapterOnClickHandler;
        this.visibilityImg = visible;

    }

    public interface StateAdapterOnClickHandler {
        void onItemClick(String state);
        void OnFavClick(String state, Integer pos);
    }


    @Override
    public StateAdapter.StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForListItem = R.layout.layout_states;
        if (visibilityImg == 1) {
            idForListItem = R.layout.layout_bookmark_states;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idForListItem, parent, false);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.StateViewHolder holder, int position) {
        String stateName = stateNames.get(position);
        holder.mTextView.setText(stateName);


    }

    @Override
    public int getItemCount() {
        if (stateNames == null) return 0;

        return stateNames.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextView;

        public StateViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_state_name);
            if (visibilityImg == 0) {
                itemView.setOnClickListener(this);
            } else if (visibilityImg == 1) {
                ImageView mImageView;
                LinearLayout mLinearLayout;
                mImageView = (ImageView) itemView.findViewById(R.id.bookmark_fav);
                mLinearLayout = (LinearLayout)itemView.findViewById(R.id.tv_state_linear);
                mLinearLayout.setOnClickListener(this);
                mImageView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPostion = getAdapterPosition();
            String state = stateNames.get(clickedPostion);
            if (visibilityImg == 0){
                stateAdapterOnClickHandler.onItemClick(state);
            }else {
                switch (v.getId()) {
                    case R.id.tv_state_linear:
                        stateAdapterOnClickHandler.onItemClick(state);
                        break;
                    case R.id.bookmark_fav:
                        stateAdapterOnClickHandler.OnFavClick(state, clickedPostion);
                        break;
                }
            }
        }

    }

    public void setStateNames(List<String> states) {
        stateNames = states;
        notifyDataSetChanged();

    }

}
