package com.photoviewer.data.presenters.views;

import com.photoviewer.data.model.Album;

import java.util.List;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public interface AlbumsView extends BaseView {

    void onAlbumsSuccess(List<Album> albums);

}
