package com.Srijan.BirdSpecies_Classification.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Srijan.BirdSpecies_Classification.R;

public class ListAdapter extends RecyclerView.Adapter {

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return BirdData.title.length;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_item_list,parent,false);
        return new ListViewHolder(view);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        private ImageView imageView;

        public  ListViewHolder(View itemView){
            super(itemView);
            textView =(TextView) itemView.findViewById(R.id.textName);
            imageView =(ImageView) itemView.findViewById(R.id.imageview);
            itemView.setOnClickListener(this);

        }

        public void bindView(int position){
            textView.setText(BirdData.title[position]);
            imageView.setImageResource(BirdData.picturePath[position]);
        }

        public void onClick(View view){

        }
    }
}
