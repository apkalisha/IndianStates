package com.indian.states.capitals.indianstates;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<String> imagesArray;
    private ArrayList<String> namesArray;
    private ArrayList<String> linksArray;

    public CustomAdapter(Activity activity, ArrayList<String> imagesArray, ArrayList<String> namesArray,ArrayList<String> linksArray) {

        this.activity = activity;
        this.imagesArray = imagesArray;
        this.namesArray = namesArray;
        this.linksArray = linksArray;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_viewpager,container,false);
        ImageView imageView = view.findViewById(R.id.image_viewpager);
        TextView textView = view.findViewById(R.id.tv_image_name);
        ImageButton imageButton = view.findViewById(R.id.external_link_btn);
        Picasso.get()
                .load(imagesArray.get(position+1))
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(imageView);
        textView.setText(namesArray.get(position));
        container.addView(view);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(linksArray.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                if(intent.resolveActivity(activity.getPackageManager()) != null){
                    activity.startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return imagesArray.size()-1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
