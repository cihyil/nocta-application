package com.nocta.login.service.controllers;

import com.nocta.login.service.models.ERole;
import com.nocta.login.service.models.Role;
import com.nocta.login.service.models.User;
import com.nocta.login.service.payload.request.LoginRequest;
import com.nocta.login.service.payload.request.SignupRequest;
import com.nocta.login.service.payload.response.LoginResponse;
import com.nocta.login.service.payload.response.MessageResponse;
import com.nocta.login.service.repository.RoleRepository;
import com.nocta.login.service.repository.UserRepository;
import com.nocta.login.service.security.jwt.JwtUtils;
import com.nocta.login.service.security.services.UserDetailsImpl;
import com.nocta.internalization.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  private MessageService messageService;



  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());


    return ResponseEntity
            .ok(new LoginResponse(true, getMessage("login.success"), new com.nocta.login.service.payload.response.User(userDetails.getUsername(), userDetails.getEmail(), jwt)));

  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new RuntimeException(getMessage("exist.username"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new RuntimeException(getMessage("exist.email"));
    }


    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException(getMessage("exist.email")));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException(getMessage("role.notfound")));
          roles.add(adminRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException(getMessage("role.notfound")));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse(true,getMessage("success.register")));
  }

  private String getMessage(String key) {
    return messageService.getMessage("", key);
  }
}
