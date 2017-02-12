package com.photoviewer.network;


import com.photoviewer.network.responses.AlbumsResponse;
import com.photoviewer.network.responses.PhotosResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Serhii Slobodyanuk on 08.09.2016.
 */

public interface ApiService {

    @GET("photos.getAlbums")
    Observable<AlbumsResponse> getAlbums(@Query("owner_id") String ownerId,
                                         @Query("access_token") String accessToken,
                                         @Query("photo_sizes") String photoSizes,
                                         @Query("need_covers") String needCovers);

    @GET("photos.get")
    Observable<PhotosResponse> getPhotos(@Query("album_id") String album_id,
                                         @Query("access_token") String accessToken);
}
