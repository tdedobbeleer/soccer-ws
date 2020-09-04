package com.soccer.ws.controllers;

import com.soccer.ws.dto.CommentDTO;
import com.soccer.ws.model.Comment;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.NewsService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.SanitizeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Created by u0090265 on 12/12/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Comment REST api", description = "Comment REST operations")
public class CommentsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(NewsRestController.class);
    private final NewsService newsService;
    private final DTOConversionHelper dtoConversionHelper;

    @Autowired
    public CommentsRestController(SecurityUtils securityUtils, MessageSource messageSource, NewsService newsService, DTOConversionHelper dtoConversionHelper) {
        super(securityUtils, messageSource);
        this.newsService = newsService;
        this.dtoConversionHelper = dtoConversionHelper;
    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/news/{id}/comment", method = RequestMethod.POST)
    @ApiOperation(value = "Post news", nickname = "postComment")
    public ResponseEntity<CommentDTO> postComment(@PathVariable UUID id, @RequestBody CommentDTO commentDTO) {
        Comment c = newsService.addNewsComment(id, SanitizeUtils.sanitizeHtml(commentDTO.getContent()), getAccountFromSecurity());
        return new ResponseEntity<>(dtoConversionHelper.convertComment(getAccountFromSecurity(), c, isAdmin(), true), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/news/{id}/comment", method = RequestMethod.PUT)
    @ApiOperation(value = "Get news", nickname = "editComment")
    public ResponseEntity editComment(@PathVariable UUID id, @RequestBody CommentDTO commentDTO) {
        Comment c = newsService.changeNewsComment(commentDTO.getId(), id, SanitizeUtils.sanitizeHtml(commentDTO.getContent()),
                getAccountFromSecurity());
        return new ResponseEntity<>(dtoConversionHelper.convertComment(getAccountFromSecurity(), c, isAdmin(), true), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/news/{id}/comment/{commentId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Get news", nickname = "deleteComment")
    public ResponseEntity deleteComment(@PathVariable UUID id, @PathVariable UUID commentId) {
        newsService.deleteNewsComment(commentId, id, getAccountFromSecurity());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
