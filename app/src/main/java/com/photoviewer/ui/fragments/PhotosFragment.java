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
import com.photoviewer.data.model.Photo;
import com.photoviewer.data.presenters.impl.PhotosPresenterImpl;
import com.photoviewer.data.presenters.views.PhotosView;
import com.photoviewer.ui.BaseActivity;
import com.photoviewer.ui.BaseFragment;
import com.photoviewer.ui.adapters.PhotosAdapter;
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

public class PhotosFragment extends BaseFragment
        implements PhotosAdapter.ItemListener, PhotosView,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String PARAM_ALBUM_TITLE = "album_title";
    private static final String PARAM_ALBUM_ID = "album_id";

    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private PhotosAdapter mAdapter;

    private PhotosPresenterImpl mPhotosPresenter;

    private GridLayoutManager mLayoutManager;
    private ItemOffsetDecoration mItemDecoration;

    private String mAlbumTitle = "";
    private String mAlbumId = "";
    private int mSaveRecyclerPosition;

    public static Fragment newInstance(String albumTitle, String albumId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_ALBUM_TITLE, albumTitle);
        bundle.putString(PARAM_ALBUM_ID, albumId);
        PhotosFragment fragment = new PhotosFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mSaveRecyclerPosition = Prefs.getInt(PrefsKeys.RECYCLER_PHOTO_POSITION, 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mAlbumTitle = getArguments().getString(PARAM_ALBUM_TITLE);
            mAlbumId = getArguments().getString(PARAM_ALBUM_ID);
        }
        updateToolbar();
        initAdapter();

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mPhotosPresenter = new PhotosPresenterImpl(this);
        mPhotosPresenter.executePhotos(mAlbumId);
    }

    private void initAdapter() {
        mAdapter = new PhotosAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mItemDecoration = new ItemOffsetDecoration(0);

        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void updateToolbar() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).unableHomeButton();
            ((BaseActivity) getActivity()).unableLogoutButton();
            ((BaseActivity) getActivity()).setToolbarTitle(getString(R.string.fragment_photos, mAlbumTitle));
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_albums;
    }

    @Override
    public void onItemClick(SquareView image, int position, Photo photo) {
        Prefs.putInt(PrefsKeys.RECYCLER_PHOTO_POSITION, position);
        ((BaseActivity) getActivity())
                .replaceFragment(R.id.fragment_container,
                        PreviewImageFragment.newInstance(photo),
                        Globals.FRAGMENT_PHOTO_PREVIEW_TAG);
    }

    @Override
    public void onError(String ex) {
        if (getActivity() != null && isVisible()) {
            Toast.makeText(getActivity(), ex, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPhotosSuccess(List<Photo> photos) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if (mAdapter != null) {
            mAdapter.update(photos);
        }
        mLayoutManager.scrollToPosition(mSaveRecyclerPosition);
    }


    @Override
    public void onRefresh() {
        if (mSwipeRefreshLayout != null && mPhotosPresenter != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mPhotosPresenter.executePhotos(mAlbumId);
        }
    }
}
