package com.vishnu.lms.auth;

import com.vishnu.lms.exception.BadRequestException;
import com.vishnu.lms.security.JwtService;
import com.vishnu.lms.user.Role;
import com.vishnu.lms.user.User;
import com.vishnu.lms.user.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = (User) authentication.getPrincipal();
        return new LoginResponse(jwtService.generateToken(user), user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("Email is already registered");
        }
        User user = new User(request.name(), request.email(), passwordEncoder.encode(request.password()), Role.STUDENT);
        userRepository.save(user);
        return new LoginResponse(jwtService.generateToken(user), user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    public record RegisterRequest(@NotBlank String name, @NotBlank @Email String email,
                                  @NotBlank @Size(min = 6, max = 100) String password) {}
    public record LoginResponse(String token, Long userId, String name, String email, Role role) {}
}
