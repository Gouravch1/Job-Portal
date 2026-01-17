package com.jobportal.controllers;

import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.entity.Users;
import com.jobportal.entity.UsersType;
import com.jobportal.services.UsersService;
import com.jobportal.services.UsersTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsersService usersService;
    private final UsersTypeService usersTypeService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UsersService usersService, UsersTypeService usersTypeService,
            AuthenticationManager authenticationManager) {
        this.usersService = usersService;
        this.usersTypeService = usersTypeService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            // Check if email already exists
            Optional<Users> existingUser = usersService.getUserByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new AuthResponse(false, "Email already registered", null, 0, null));
            }

            // Create new user
            Users newUser = new Users();
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword());

            // Fetch user type from database (default to Job Seeker if not specified)
            int userTypeId = request.getUserTypeId() > 0 ? request.getUserTypeId() : 2;
            UsersType userType = usersTypeService.getAll().stream()
                    .filter(ut -> ut.getUserTypeId() == userTypeId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Invalid user type"));
            newUser.setUserTypeId(userType);

            // Save user
            Users savedUser = usersService.addNew(newUser);

            // Authenticate the user automatically
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Return success response
            String userTypeName = savedUser.getUserTypeId().getUserTypeId() == 1 ? "Recruiter" : "Job Seeker";
            return ResponseEntity.ok(new AuthResponse(
                    true,
                    "Registration successful",
                    savedUser.getEmail(),
                    savedUser.getUserId(),
                    userTypeName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "Registration failed: " + e.getMessage(), null, 0, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get user details
            Users user = usersService.findByEmail(request.getEmail());
            String userTypeName = user.getUserTypeId().getUserTypeId() == 1 ? "Recruiter" : "Job Seeker";

            // Return success response
            return ResponseEntity.ok(new AuthResponse(
                    true,
                    "Login successful",
                    user.getEmail(),
                    user.getUserId(),
                    userTypeName));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Invalid email or password", null, 0, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "Login failed: " + e.getMessage(), null, 0, null));
        }
    }
}
