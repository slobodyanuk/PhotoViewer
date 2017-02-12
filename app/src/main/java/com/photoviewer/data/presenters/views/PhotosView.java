package com.photoviewer.data.presenters.views;

import com.photoviewer.data.model.Photo;

import java.util.List;

/**
 * Created by Serhii Slobodianiuk on 11.02.2017.
 */

public interface PhotosView extends BaseView {

    void onPhotosSuccess(List<Photo> photos);

}
