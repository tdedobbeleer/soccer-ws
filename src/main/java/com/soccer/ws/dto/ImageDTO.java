package com.soccer.ws.dto;

/**
 * Created by u0090265 on 05.08.17.
 */
public class ImageDTO {
    private String id;
    private String url;

    public ImageDTO() {
    }

    public ImageDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
