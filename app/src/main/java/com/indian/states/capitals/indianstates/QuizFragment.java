package com.indian.states.capitals.indianstates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class QuizFragment extends Fragment{

    View quizFragment;

    //Extracting data from firebase
    RecyclerView listQuiz;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Quiz,QuizViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference dbref;


    public static QuizFragment newInstance() {

        return new QuizFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=FirebaseDatabase.getInstance();
        dbref=db.getReference("quiz");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        quizFragment = inflater.inflate(R.layout.fragment_quiz,container,false);
//        listQuiz=(RecyclerView)quizFragment.findViewById(R.id.id_view_quiz);
//        listQuiz.setHasFixedSize(true);
//        layoutManager=new LinearLayoutManager(container.getContext());
//        listQuiz.setLayoutManager(layoutManager);
//        loadCategories();
        
        return quizFragment;
    }

    private void loadCategories() {
        adapter=new FirebaseRecyclerAdapter<Quiz, QuizViewHolder>(
                Quiz.class,
                R.layout.quiz,
                QuizViewHolder.class,
                dbref
        ) {
            @Override
            protected void populateViewHolder(QuizViewHolder viewHolder, final Quiz model, int position) {

                Picasso.get()
                        .load(model.getImage())
                        .into(viewHolder.quiz_img);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(),String.format("%s|%s",adapter.getRef(position).getKey(),model.getType()),Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listQuiz.setAdapter(adapter);
    }
}
