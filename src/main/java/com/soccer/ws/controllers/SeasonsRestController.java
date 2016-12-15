package com.soccer.ws.controllers;

import com.soccer.ws.dto.SeasonDTO;
import com.soccer.ws.service.DTOConversionHelper;
import com.soccer.ws.service.SeasonService;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by u0090265 on 08/07/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Seasons REST api", description = "Season REST operations")
public class SeasonsRestController extends AbstractRestController {

    private final SeasonService seasonService;

    private final DTOConversionHelper DTOConversionHelper;

    public SeasonsRestController(SecurityUtils securityUtils, MessageSource messageSource, SeasonService seasonService, DTOConversionHelper dtoConversionHelper) {
        super(securityUtils, messageSource);
        this.seasonService = seasonService;
        DTOConversionHelper = dtoConversionHelper;
    }

    @RequestMapping(value = "/seasons", method = RequestMethod.GET)
    @ApiOperation(value = "Get all seasons")
    public ResponseEntity<List<SeasonDTO>> getSeasons() {
        return new ResponseEntity<>(DTOConversionHelper.convertSeasons(seasonService.getSeasons()), HttpStatus.OK);
    }
}
