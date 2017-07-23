package com.soccer.ws.controllers;

import com.soccer.ws.dto.ErrorDetailDTO;
import com.soccer.ws.dto.ValidationErrorDetailDTO;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by u0090265 on 08/07/16.
 */
@org.springframework.web.bind.annotation.RestController
@Api(value = "Errors REST api", description = "Error REST operations")
public class ErrorRestController extends AbstractRestController {

    @Autowired
    public ErrorRestController(SecurityUtils securityUtils, MessageSource messageSource) {
        super(securityUtils, messageSource);
    }

    @RequestMapping(value = "/error/500", method = RequestMethod.GET)
    @ApiOperation(value = "Get example 500 error", nickname = "get500Error")
    public ResponseEntity<ErrorDetailDTO> GetError() {
        return new ResponseEntity<>(new ErrorDetailDTO(), HttpStatus.OK);
    }

    @RequestMapping(value = "/error/400", method = RequestMethod.GET)
    @ApiOperation(value = "Get example 400 error", nickname = "get400Error")
    public ResponseEntity<ValidationErrorDetailDTO> getValidationError() {
        return new ResponseEntity<>(new ValidationErrorDetailDTO(), HttpStatus.OK);
    }
}
