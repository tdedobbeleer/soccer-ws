package com.soccer.ws.controllers;

import com.google.common.base.Optional;
import com.soccer.ws.dto.AccountDTO;
import com.soccer.ws.dto.NewsDTO;
import com.soccer.ws.dto.PageDTO;
import com.soccer.ws.exceptions.CustomMethodArgumentNotValidException;
import com.soccer.ws.model.News;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.NewsService;
import com.soccer.ws.utils.SecurityUtils;
import com.soccer.ws.validators.NewsValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Created by u0090265 on 16/09/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "News REST api", description = "News REST operations")
public class NewsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(NewsRestController.class);
    private final NewsService newsService;
    private final DTOConversionHelper dtoConversionHelper;
    private final NewsValidator newsValidator;

    public NewsRestController(SecurityUtils securityUtils, MessageSource messageSource, NewsService newsService, DTOConversionHelper dtoConversionHelper, NewsValidator newsValidator) {
        super(securityUtils, messageSource);
        this.newsService = newsService;
        this.dtoConversionHelper = dtoConversionHelper;
        this.newsValidator = newsValidator;
    }

    @InitBinder("newsDTO")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(newsValidator);
    }

    @RequestMapping(value = "/news", method = RequestMethod.GET)
    @ApiOperation(value = "Get news", nickname = "getNewsPage")
    public ResponseEntity<PageDTO<NewsDTO>> getNewsPage(@RequestParam(required = false) String searchTerm, @RequestParam int page, @RequestParam(required =
            false) int size) {
        Page<News> news = newsService.getPagedNews(Optional.fromNullable(searchTerm), page, size, Optional.absent());
        return new ResponseEntity<>(dtoConversionHelper.convertNewsPage(getAccountFromSecurity(), news, isAdmin()),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/news/latest", method = RequestMethod.GET)
    @ApiOperation(value = "Get latest news", nickname = "getLatestNews")
    public ResponseEntity<PageDTO<NewsDTO>> getLatestNewsPage(@RequestParam(required = false) String searchTerm, @RequestParam int page, @RequestParam(required =
            false) int size) {
        Page<News> news = newsService.getLatestPagedNews(Optional.fromNullable(searchTerm), page, size, Optional.absent());
        return new ResponseEntity<>(dtoConversionHelper.convertNewsPage(getAccountFromSecurity(), news, isAdmin()),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/news", method = RequestMethod.POST)
    @ApiOperation(value = "Post news", nickname = "postNews")
    public ResponseEntity postNews(@Valid @RequestBody NewsDTO newsDTO, BindingResult bindingResult) throws CustomMethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new CustomMethodArgumentNotValidException(bindingResult);
        } else {
            AccountDTO account = dtoConversionHelper.convertAccount(getAccountFromSecurity(), true);
            newsDTO.setPostedBy(account);

            switch (newsDTO.getType()) {
                case POST_AND_SEND:
                    newsService.create(newsDTO);
                    newsService.sendNewsEmail(newsDTO);
                    break;
                case POST:
                    newsService.create(newsDTO);
                    break;
                case SEND:
                    newsService.sendNewsEmail(newsDTO);
                    break;
            }

        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/news", method = RequestMethod.PUT)
    @ApiOperation(value = "Update news", nickname = "updateNews")
    public ResponseEntity updateNews(@Valid @RequestBody NewsDTO newsDTO, BindingResult bindingResult) throws CustomMethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new CustomMethodArgumentNotValidException(bindingResult);
        } else {
            newsService.update(newsDTO, getAccountFromSecurity());
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/news/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get news", nickname = "getNews")
    public ResponseEntity<NewsDTO> getNews(@PathVariable UUID id) {
        News news = newsService.getNewsItem(id);
        return new ResponseEntity<>(dtoConversionHelper.convertNews(getAccountFromSecurity(), news, isAdmin()),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/news/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Delete news", nickname = "deleteNews")
    public ResponseEntity deleteNews(@PathVariable UUID id) {
        newsService.deleteNews(id, getAccountFromSecurity());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
