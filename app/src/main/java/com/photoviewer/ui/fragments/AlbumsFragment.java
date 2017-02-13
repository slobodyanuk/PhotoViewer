package com.photoviewer.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.photoviewer.R;
import com.photoviewer.data.model.Album;
import com.photoviewer.data.presenters.impl.AlbumPresenterImpl;
import com.photoviewer.data.presenters.views.AlbumsView;
import com.photoviewer.ui.BaseActivity;
import com.photoviewer.ui.BaseFragment;
import com.photoviewer.ui.adapters.AlbumsAdapter;
import com.photoviewer.ui.components.ItemOffsetDecoration;
import com.photoviewer.ui.components.SquareView;
import com.photoviewer.utils.Globals;
import com.photoviewer.utils.PrefsKeys;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Serhii Slobodianiuk on 10.02.2017.
 */

public class AlbumsFragment extends BaseFragment
        implements AlbumsAdapter.ItemListener, AlbumsView,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private AlbumsAdapter mAdapter;

    private AlbumPresenterImpl mAlbumPresenter;

    private GridLayoutManager mLayoutManager;
    private ItemOffsetDecoration mItemDecoration;
    private int mSavedRecyclerPosition;

    public static Fragment newInstance() {
        return new AlbumsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mSavedRecyclerPosition = Prefs.getInt(PrefsKeys.RECYCLER_ALBUM_POSITION, 0);
        Prefs.remove(PrefsKeys.RECYCLER_PHOTO_POSITION);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAdapter();

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAlbumPresenter = new AlbumPresenterImpl(this);
        mAlbumPresenter.executeAlbums();
    }

    private void initAdapter() {
        mAdapter = new AlbumsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mItemDecoration = new ItemOffsetDecoration(0);

        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void updateToolbar() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).disableHomeButton();
            ((BaseActivity) getActivity()).unableLogoutButton();
            ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.fragment_albums));
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_albums;
    }

    @Override
    public void onItemClick(SquareView image, int position, Album album) {

        Prefs.putInt(PrefsKeys.RECYCLER_ALBUM_POSITION, position);

        ((BaseActivity) getActivity())
                .replaceFragment(R.id.fragment_container,
                        PhotosFragment.newInstance(album.getTitle(), album.getAlbumId()),
                        Globals.FRAGMENT_PHOTO_TAG);
    }

    @Override
    public void onError(String ex) {
        if (getActivity() != null && isVisible()) {
            Toast.makeText(getActivity(), ex, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAlbumsSuccess(List<Album> albums) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mAdapter != null) {
            mAdapter.update(albums);
        }
        mLayoutManager.scrollToPosition(mSavedRecyclerPosition);
    }

    @Override
    public void onRefresh() {
        if (mSwipeRefreshLayout != null && mAlbumPresenter != null){
            mSwipeRefreshLayout.setRefreshing(true);
            mAlbumPresenter.executeAlbums();
        }
    }
}
