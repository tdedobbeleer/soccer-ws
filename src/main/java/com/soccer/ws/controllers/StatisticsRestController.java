package com.soccer.ws.controllers;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.AccountStatisticDTO;
import com.soccer.ws.dto.ByteResponseDTO;
import com.soccer.ws.service.CSVService;
import com.soccer.ws.service.CacheAdapter;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by u0090265 on 16/09/16.
 */
@RestController
@Api(value = "Statictics REST api", description = "Statictics REST operations")
public class StatisticsRestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsRestController.class);
    private final CacheAdapter cacheAdapter;
    private final CSVService csvService;

    public StatisticsRestController(SecurityUtils securityUtils, MessageSource messageSource, CacheAdapter cacheAdapter, CSVService csvService) {
        super(securityUtils, messageSource);
        this.cacheAdapter = cacheAdapter;
        this.csvService = csvService;
    }

    @RequestMapping(value = "/statistics/season/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get statictics", nickname = "getStatictics")
    public ResponseEntity<List<AccountStatisticDTO>> getStatisticsForSeason(@PathVariable UUID id) {
        return new ResponseEntity<>(cacheAdapter.getStatisticsForSeason(id, isLoggedIn()),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/statistics/season/{id}/export")
    @ApiOperation(value = "Export statistics for season", nickname = "exportStatistics")
    public ByteResponseDTO getExportStatistic(@PathVariable UUID id, HttpServletResponse response) throws IOException {
        List<List<String>> csvData = Lists.<List<String>>newArrayList(Lists.newArrayList("Name", "Goals", "Assists", "Matches played", "Motm"));
        cacheAdapter.getStatisticsForSeason(id, isLoggedIn()).forEach(s -> {
            csvData.add(Lists.newArrayList(s.getAccount().getName(), Integer.toString(s.getGoals()), Integer.toString(s.getAssists()), Integer.toString(s.getPlayed()), Integer.toString(s.getMotm())));
        });

        return new ByteResponseDTO(csvService.write(csvData).getBytes());
    }
}
