package me.yablochniuk.carouseltest.search.model.pxrest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vitalii Yablochniuk on 3/31/16
 */
public class PhotosInfo {
    @SerializedName("photos") private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public class Photo {
        @SerializedName("name") String name;
        @SerializedName("image_url") private String imageUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
