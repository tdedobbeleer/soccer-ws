package com.soccer.ws.controllers;

import com.google.common.base.Optional;
import com.soccer.ws.dto.NewsDTO;
import com.soccer.ws.dto.PageDTO;
import com.soccer.ws.model.News;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by u0090265 on 16/09/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "News REST api", description = "News REST operations")
public class NewsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(NewsRestController.class);
    private final NewsService newsService;
    private final DTOConversionHelper dtoConversionHelper;

    public NewsRestController(NewsService newsService, DTOConversionHelper dtoConversionHelper) {
        this.newsService = newsService;
        this.dtoConversionHelper = dtoConversionHelper;
    }

    @RequestMapping(value = "/news", method = RequestMethod.GET)
    @ApiOperation(value = "Get news", nickname = "getNewsPage")
    public ResponseEntity<PageDTO<NewsDTO>> getNewsPage(@RequestParam int page, @RequestParam(required =
            false) int size) {
        Page<News> news = newsService.getPagedNews(page, size, Optional.absent());
        return new ResponseEntity<>(dtoConversionHelper.convertNewsPage(getAccountFromSecurity(), news, isAdmin()),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/news/search/{term}", method = RequestMethod.GET)
    @ApiOperation(value = "Search news", nickname = "searchNewsPage")
    public ResponseEntity<PageDTO<NewsDTO>> searchNewsPage(@PathVariable String term, @RequestParam int page,
                                                           @RequestParam(required =
                                                                   false) int size) {
        Page<News> news = newsService.getSearch(term, page, size, Optional.absent());
        return new ResponseEntity<>(dtoConversionHelper.convertNewsPage(getAccountFromSecurity(), news, isAdmin()),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/news/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get news", nickname = "getNews")
    public ResponseEntity<NewsDTO> getNews(@PathVariable Long id) {
        News news = newsService.getNewsItem(id);
        return new ResponseEntity<>(dtoConversionHelper.convertNews(getAccountFromSecurity(), news, isAdmin()),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/news/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete news", nickname = "deleteNews")
    public ResponseEntity deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id, getAccountFromSecurity());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
