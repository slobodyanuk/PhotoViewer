package com.photoviewer.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.photoviewer.R;
import com.photoviewer.data.model.Album;
import com.photoviewer.ui.components.SquareView;
import com.photoviewer.utils.GlideRequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Serhii Slobodianiuk on 10.02.2017.
 */

public class AlbumsAdapter  extends RecyclerView.Adapter<AlbumsAdapter.GridViewHolder> {

    private List<Album> mAlbums = new ArrayList<>();
    private static ItemListener sItemListener;

    public AlbumsAdapter(ItemListener mItemListener) {
        AlbumsAdapter.sItemListener = mItemListener;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridViewHolder holder, int position) {
        holder.title.setText(mAlbums.get(position).getTitle());
        holder.setImage(mAlbums.get(position).getAlbumImageUrl());

        holder.root.setOnClickListener(v -> sItemListener.onItemClick(holder.image,
                holder.getAdapterPosition(),
                mAlbums.get(position)));
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public void update(List<Album> albums) {
        this.mAlbums.clear();
        this.mAlbums = albums;
        notifyDataSetChanged();
    }

    static class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        FrameLayout root;

        @BindView(R.id.image)
        SquareView image;

        @BindView(R.id.title)
        TextView title;

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
        void onItemClick(SquareView image, int position, Album album);
    }
}
