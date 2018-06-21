package com.indian.states.capitals.indianstates;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {
    private List<String> stateNames;
    private Integer VISIBLE = 0;
    private Context mContext;
    // private final OnFavClickListener mOnFavClickListener;


    private final StateAdapterOnClickHandler stateAdapterOnClickHandler;

    StateAdapter(StateAdapterOnClickHandler stateAdapterOnClickHandler) {

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
        View view = inflater.inflate(idForListItem, parent, false);

        return new StateViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final StateAdapter.StateViewHolder holder, int position) {
        final String stateName = stateNames.get(position);
        holder.mTextView.setText(stateName);
        if (VISIBLE == 1) {
            holder.mImageView.setVisibility(View.VISIBLE);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // mOnFavClickListener.OnFavclicked(stateName);
                    holder.mImageView.setImageResource(R.drawable.ic_bookmar_fav2);

                }
            });
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

    public void setBookmarkVisibilty(Integer visibilty, Context context) {
        VISIBLE = visibilty;
        mContext = context;
    }
}
