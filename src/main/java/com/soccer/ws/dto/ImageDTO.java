package com.soccer.ws.dto;

import lombok.*;

/**
 * Created by u0090265 on 05.08.17.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ImageDTO {
    private String id;
    private String url;

    public ImageDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
