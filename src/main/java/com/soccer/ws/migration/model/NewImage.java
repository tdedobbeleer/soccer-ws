package com.soccer.ws.migration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by u0090265 on 11/8/15.
 */
@Entity
@Table(name = "image")
public class NewImage extends NewBaseClass {
    private String imageId;
    private String imageUrl;

    @Column(name = "image_id")
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Column(name = "image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
