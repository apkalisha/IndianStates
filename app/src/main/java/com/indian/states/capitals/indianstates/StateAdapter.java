package com.indian.states.capitals.indianstates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private List<String> stateNames;

    private final StateAdapterOnClickHandler stateAdapterOnClickHandler;

    StateAdapter(StateAdapterOnClickHandler stateAdapterOnClickHandler){

        this.stateAdapterOnClickHandler = stateAdapterOnClickHandler;
    }

    public interface StateAdapterOnClickHandler {
        void onItemClick(String state);
    }

    @Override
    public StateAdapter.StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idForListItem = R.layout.layout_states;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idForListItem,parent,false);
        return new StateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.StateViewHolder holder, int position) {
        String stateName = stateNames.get(position);
        holder.mTextView.setText(stateName);
    }

    @Override
    public int getItemCount() {
        if(stateNames == null) return 0;

        return stateNames.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTextView;
        public StateViewHolder(View itemView) {
            super(itemView);
            mTextView =  itemView.findViewById(R.id.tv_state_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPostion = getAdapterPosition();
            String state = stateNames.get(clickedPostion);
            stateAdapterOnClickHandler.onItemClick(state);
        }
    }

    public void setStateNames(List<String> states) {
        stateNames = states;
        notifyDataSetChanged();
    }
}
