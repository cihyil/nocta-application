package com.nocta.user.management.service;


import com.nocta.internalization.service.MessageService;
import com.nocta.user.management.payload.response.LoginResponse;
import com.nocta.user.management.models.ERole;
import com.nocta.user.management.models.Role;
import com.nocta.user.management.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nocta.user.management.payload.request.LoginRequest;
import com.nocta.user.management.payload.request.SignupRequest;
import com.nocta.user.management.payload.response.MessageResponse;
import com.nocta.user.management.payload.response.UserResponse;
import com.nocta.user.management.repository.RoleRepository;
import com.nocta.user.management.repository.UserRepository;
import com.nocta.user.management.security.jwt.JwtUtils;
import com.nocta.user.management.security.services.UserDetailsImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private MessageService messageService;


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return new LoginResponse(true, getMessage("login.success"),
                new UserResponse(userDetails.getUsername(), userDetails.getEmail(), jwt));
    }

    @Override
    public MessageResponse register(SignupRequest signUpRequest) {

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

        return new MessageResponse(true,getMessage("success.register"));

    }

    private String getMessage(String key) {
        return messageService.getMessage("", key);
    }
}
