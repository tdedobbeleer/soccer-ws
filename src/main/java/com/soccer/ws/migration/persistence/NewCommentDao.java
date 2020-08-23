package com.soccer.ws.migration.persistence;

import com.soccer.ws.migration.model.NewComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by u0090265 on 10/13/14.
 */
public interface NewCommentDao extends PagingAndSortingRepository<NewComment, Long>, JpaSpecificationExecutor<NewComment> {
}
