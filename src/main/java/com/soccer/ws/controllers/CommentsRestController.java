package com.soccer.ws.controllers;

import com.soccer.ws.dto.CommentDTO;
import com.soccer.ws.service.NewsService;
import com.soccer.ws.validators.SanitizeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by u0090265 on 12/12/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Comment REST api", description = "Comment REST operations")
public class CommentsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(NewsRestController.class);
    private final NewsService newsService;

    @Autowired
    public CommentsRestController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/news/{id}/comment", method = RequestMethod.POST)
    @ApiOperation(value = "Get news", nickname = "postComment")
    public ResponseEntity postComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        newsService.addNewsComment(id, SanitizeUtils.SanitizeHtml(commentDTO.getContent()), getAccountFromSecurity());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/news/{id}/comment", method = RequestMethod.PUT)
    @ApiOperation(value = "Get news", nickname = "editComment")
    public ResponseEntity editComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        newsService.changeNewsComment(commentDTO.getId(), id, SanitizeUtils.SanitizeHtml(commentDTO.getContent()),
                getAccountFromSecurity());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/news/{id}/comment/{commentId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Get news", nickname = "deleteComment")
    public ResponseEntity deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        newsService.deleteNewsComment(commentId, id, getAccountFromSecurity());
        return new ResponseEntity(HttpStatus.OK);
    }
}
