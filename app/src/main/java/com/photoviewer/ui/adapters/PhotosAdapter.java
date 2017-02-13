package com.photoviewer.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.photoviewer.R;
import com.photoviewer.data.model.Photo;
import com.photoviewer.ui.components.SquareView;
import com.photoviewer.utils.GlideRequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodianiuk on 10.02.2017.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.GridViewHolder> {

    private List<Photo> mPhotos = new ArrayList<>();
    private static ItemListener sItemListener;

    public PhotosAdapter(ItemListener mItemListener) {
        PhotosAdapter.sItemListener = mItemListener;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {
        holder.setImage(mPhotos.get(position).getImageUrlBig());

        holder.root.setOnClickListener(v -> sItemListener.onItemClick(holder.image,
                holder.getAdapterPosition(),
                mPhotos.get(position)));
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }



    public void update(List<Photo> photos) {
        this.mPhotos.clear();
        this.mPhotos = photos;
        notifyDataSetChanged();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        FrameLayout root;

        @BindView(R.id.image)
        SquareView image;

        @BindView(R.id.progress)
        ProgressBar progressBar;

        public GridViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setImage(String url){
            Glide.with((Fragment) sItemListener)
                    .load(url)
                    .centerCrop()
                    .listener(new GlideRequestListener(progressBar))
                    .error(android.R.drawable.stat_notify_error)
                    .into(image);
        }
    }

    public interface ItemListener {
        void onItemClick(SquareView image, int position, Photo photo);
    }
}
