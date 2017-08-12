package com.soccer.ws.controllers;

import com.google.common.collect.Lists;
import com.soccer.ws.dto.AuthenticationRequestDTO;
import com.soccer.ws.dto.AuthenticationResponseDTO;
import com.soccer.ws.persistence.UserDetailsAdapter;
import com.soccer.ws.security.TokenUtils;
import com.soccer.ws.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Authentication endpoint", description = "Endpoint for logging in")
public class AuthenticationController extends AbstractRestController {

  private final Logger logger = Logger.getLogger(this.getClass());
  private final AuthenticationManager authenticationManager;
  private final TokenUtils tokenUtils;
  private final UserDetailsService userDetailsService;
  @Value("${jwt.token.header}")
  private String tokenHeader;

  public AuthenticationController(MessageSource messageSource, AuthenticationManager authenticationManager, TokenUtils tokenUtils, UserDetailsService userDetailsService, SecurityUtils securityUtils) {
    super(securityUtils, messageSource);
    this.authenticationManager = authenticationManager;
    this.tokenUtils = tokenUtils;
    this.userDetailsService = userDetailsService;
  }

  @RequestMapping(value = "/auth", method = RequestMethod.POST)
  @ResponseBody
  @ApiOperation(value = "authenticate", nickname = "authenticate")
  @ApiResponses(value = {
          @ApiResponse(code = 401, message = "Access denied")})
  public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequestDTO authenticationRequestDTO, Device device, HttpServletRequest request) throws AuthenticationException {
    // Perform the authentication
    Authentication authentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    authenticationRequestDTO.getUsername(),
                    authenticationRequestDTO.getPassword()
            )
    );
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Reload password post-authentication so we can generate token
    UserDetailsAdapter user = (UserDetailsAdapter) this.userDetailsService.loadUserByUsername(authenticationRequestDTO.getUsername());
      String token = this.tokenUtils.generateToken(user, device, request.getHeader("User-Agent"), authenticationRequestDTO.isRememberMe());

    // Return the token
    return ResponseEntity.ok(new AuthenticationResponseDTO(token, user.getId(), user.getFirstName(), user.getLastName(), getAuthorities(user)));

  }

  @RequestMapping(value = "/auth/full", method = RequestMethod.GET)
  @ResponseBody
  @ApiOperation(value = "Is fully authenticated", nickname = "isFullyAuthenticated")
  public ResponseEntity isFullyAuthenticated() {
    return getAccountFromSecurity() == null ? new ResponseEntity<>(HttpStatus.UNAUTHORIZED) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

    private List<String> getAuthorities(UserDetailsAdapter userDetailsAdapter) {
        return Lists.newArrayList(userDetailsAdapter.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

    }
}
