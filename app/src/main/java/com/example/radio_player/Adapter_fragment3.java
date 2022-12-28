package com.example.radio_player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_fragment3 extends RecyclerView.Adapter<Adapter_fragment3.ViewHolder> implements OnItemClickListener {

    private ArrayList<String> nomes;
    private ArrayList<byte[]> imgs;

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
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.radio_name);
            imageView = view.findViewById(R.id.radio_img);
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

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
    }


    public Adapter_fragment3(ArrayList<String> s, ArrayList<byte[]> i) {
        this.nomes = s;
        this.imgs = i;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.radio_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(nomes.get(position));
        if (imgs.get(position) != null) {
            viewHolder.getImageView().setImageBitmap(BitmapFactory.decodeByteArray(imgs.get(position), 0, imgs.get(position).length));
        }
        else{
            viewHolder.getImageView().setImageBitmap(Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888));
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return nomes.size();
    }
}