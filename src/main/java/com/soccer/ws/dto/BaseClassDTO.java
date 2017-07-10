package com.soccer.ws.dto;

/**
 * Created by u0090265 on 10/06/16.
 */
public class BaseClassDTO {
    private Long id;

    public BaseClassDTO() {

    }

    public BaseClassDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
