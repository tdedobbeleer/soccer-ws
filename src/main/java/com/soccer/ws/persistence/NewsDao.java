package com.soccer.ws.persistence;

import com.soccer.ws.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;

/**
 * User: Tom De Dobbeleer
 * Date: 12/20/13
 * Time: 9:14 AM
 * Remarks: none
 */
public interface NewsDao extends PagingAndSortingRepository<News, java.util.UUID>, JpaSpecificationExecutor<News> {
    @Query("select n from News n where n.header like ?1 OR n.content like ?1 order by n.postDate desc")
    Page<News> findByHeaderOrContent(String term, Pageable pageable);

    @Query("select n from News n where n.header like ?1 OR n.content like ?1 and n.postDate > ?2 order by n.postDate desc")
    Page<News> findByHeaderOrContentPostDateAfter(String term, Date date, Pageable pageable);

    Page<News> findByPostDateAfterOrderByPostDateDesc(Pageable page, Date date);
}
