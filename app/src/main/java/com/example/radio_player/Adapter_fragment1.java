package com.example.radio_player;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Adapter_fragment1 extends RecyclerView.Adapter<Adapter_fragment1.ViewHolder> implements OnItemClickListener{

    private ArrayList<String> titulos;
    private ArrayList<byte[]> imgs;
    private ArrayList<String> autores;


    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(int position) {
        if (listener != null) {
            listener.onItemClick(position);
        }
    }



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView author;
        private final ImageView imageView;
        private int index;



        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            title = (TextView) view.findViewById(R.id.music_item_title);
            author = view.findViewById(R.id.music_item_band);
            imageView = view.findViewById(R.id.music_item_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

        }

        public TextView gettitle() {
            return title;
        }
        public TextView getauthor() {
            return author;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public void setindex(int i){
            this.index = i;
        }
        public int getindex(){
            return index;
        }




    }


    public Adapter_fragment1(ArrayList<String> titulos, ArrayList<byte[]> img, ArrayList<Integer> position,ArrayList<String> autores ) {
        this.titulos = titulos;
        this.imgs = img;
        this.autores = autores;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.music_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       // if (itemsFiltered.contains(titulos.get(position))) {
            viewHolder.gettitle().setText(titulos.get(position));
            viewHolder.getauthor().setText(autores.get(position));
            viewHolder.setindex(position);
            if (imgs.get(position) != null) {
                viewHolder.getImageView().setImageBitmap(BitmapFactory.decodeByteArray(imgs.get(position), 0, imgs.get(position).length));
            } else {
                viewHolder.getImageView().setImageResource(R.drawable.icons8_musical_80);
            }
       // }
    }
    @Override
    public int getItemCount() {
        return titulos.size();
    }




}