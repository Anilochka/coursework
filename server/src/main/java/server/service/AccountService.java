package server.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.repository.RefreshTokenRepository;
import server.repository.UserRepository;
import server.entity.RefreshToken;
import server.entity.RegisterRequest;
import server.entity.User;
import server.security.jwt.JwtTokenProvider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepo;
    private final UserRepository userRepo;

    @Autowired
    public AccountService(@Qualifier("customUserDetailService") UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider, RefreshTokenRepository refreshTokenRepo, UserRepository userRepo) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
    }

    public Map<String, Object> loginUser(String username, String password, HttpServletResponse response) {
        try {
            User user = (User) userDetailsService.loadUserByUsername(username);

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            String jwtToken = jwtTokenProvider.createToken(username, user.getRoles());
            String refreshToken = createRefreshToken(user);

            Map<String, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("roles", user.getRoles());
            model.put("jwtToken", jwtToken);

            Cookie cookie = new Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/api/auth/");
            response.addCookie(cookie);

            return model;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public Optional<String> refreshToken(String refreshToken) {
        return refreshTokenRepo.findByToken(refreshToken)
                .map(userRefreshToken ->
                        jwtTokenProvider.createToken(
                                userRefreshToken.getUser().getUsername(),
                                userRefreshToken.getUser().getRoles()
                        )
                );
    }

    public void logoutUser(String refreshToken) {
        refreshTokenRepo.findByToken(refreshToken).ifPresent(refreshTokenRepo::delete);
    }

    public boolean isUnique(String username) {
        return userRepo.findUserByUsername(username).isEmpty();
    }

    public void registerUser(@Valid RegisterRequest request) {
        if (!isUnique(request.getUsername())) {
            throw new IllegalStateException("Username must be unique");
        }

        final List<String> roles = List.of("ROLE_USER", "ROLE_ADMIN");

        if (!roles.contains(request.getRole())) {
            throw new IllegalStateException("Role must be allowed");
        }

        userRepo.save(new User(request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                List.of(request.getRole())));
    }

    private String createRefreshToken(User user) {
        String token = RandomStringUtils.randomAlphanumeric(128);
        refreshTokenRepo.save(new RefreshToken(token, user));
        return token;
    }
}

