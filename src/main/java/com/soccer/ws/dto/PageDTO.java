package com.soccer.ws.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by u0090265 on 21/07/16.
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PageDTO<T> {
    private List<T> list;
    private int totalPages;
    private int totalSize;
    private boolean hasNext;
    private boolean hasPrevious;
    private int currentPage;

    public PageDTO(List<T> list, int totalPages, boolean hasNext, boolean hasPrevious, int currentPage) {
        this.currentPage = currentPage;
        Assert.notNull(list, "List cannot be null");
        this.list = list;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "list=" + list +
                ", totalPages=" + totalPages +
                ", totalSize=" + totalSize +
                ", hasNext=" + hasNext +
                ", hasPrevious=" + hasPrevious +
                ", currentPage=" + currentPage +
                '}';
    }
}
