package com.indian.states.capitals.indianstates;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView quiz_img;
    private ItemClickListener itemClickListener;
    public QuizViewHolder(View itemView) {
        super(itemView);
        quiz_img=(ImageView)itemView.findViewById(R.id.quiz_img_id);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {

        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
