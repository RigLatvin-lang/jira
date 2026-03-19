package org.aur.jira.usecase;

import org.aur.jira.config.JwtUtil;
import org.aur.jira.domain.entity.User;
import org.aur.jira.dto.AuthResponse;
import org.aur.jira.dto.LoginRequest;
import org.aur.jira.dto.RegisterRequest;
import org.aur.jira.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthUseCase Unit Tests")
class AuthUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthUseCase authUseCase;

    private User user;
    private String rawPassword;
    private String encodedPassword;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        rawPassword = "password123";
        encodedPassword = "$2a$10$hashedpassword";
        jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";

        user = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@example.com")
                .password(encodedPassword)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should register new user successfully")
    void shouldRegisterNewUserSuccessfully() {
        // Given
        RegisterRequest request = new RegisterRequest("newuser", "newuser@test.com", rawPassword);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@test.com")).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken("newuser")).thenReturn(jwtToken);

        // When
        AuthResponse response = authUseCase.register(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(jwtToken);
        assertThat(response.username()).isEqualTo("newuser");

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("newuser@test.com");
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(User.class));
        verify(jwtUtil).generateToken(anyString());
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameExists() {
        // Given
        RegisterRequest request = new RegisterRequest("existinguser", "new@test.com", rawPassword);
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authUseCase.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username already taken");

        verify(userRepository).existsByUsername("existinguser");
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        RegisterRequest request = new RegisterRequest("newuser", "existing@test.com", rawPassword);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> authUseCase.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already taken");

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("existing@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void shouldLoginSuccessfullyWithValidCredentials() {
        // Given
        LoginRequest request = new LoginRequest("testuser", rawPassword);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn(jwtToken);

        // When
        AuthResponse response = authUseCase.login(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(jwtToken);
        assertThat(response.username()).isEqualTo("testuser");

        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
        verify(jwtUtil).generateToken("testuser");
    }

    @Test
    @DisplayName("Should throw exception when user not found during login")
    void shouldThrowExceptionWhenUserNotFoundDuringLogin() {
        // Given
        LoginRequest request = new LoginRequest("nonexistent", rawPassword);
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> authUseCase.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");

        verify(userRepository).findByUsername("nonexistent");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should throw exception when password is incorrect")
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        // Given
        LoginRequest request = new LoginRequest("testuser", "wrongpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", encodedPassword)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> authUseCase.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid credentials");

        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("wrongpassword", encodedPassword);
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should encode password during registration")
    void shouldEncodePasswordDuringRegistration() {
        // Given
        RegisterRequest request = new RegisterRequest("newuser", "new@test.com", "plaintext123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordEncoder.encode("plaintext123")).thenReturn("$2a$10$encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString())).thenReturn(jwtToken);

        // When
        authUseCase.register(request);

        // Then
        verify(passwordEncoder).encode("plaintext123");
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword().equals("$2a$10$encoded")
        ));
    }

    @Test
    @DisplayName("Should generate JWT token after successful registration")
    void shouldGenerateJwtTokenAfterSuccessfulRegistration() {
        // Given
        RegisterRequest request = new RegisterRequest("newuser", "new@test.com", rawPassword);
        String expectedToken = "jwt.token.here";

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtil.generateToken(anyString())).thenReturn(expectedToken);

        // When
        AuthResponse response = authUseCase.register(request);

        // Then
        assertThat(response.token()).isEqualTo(expectedToken);
        verify(jwtUtil).generateToken(anyString());
    }

    @Test
    @DisplayName("Should generate JWT token after successful login")
    void shouldGenerateJwtTokenAfterSuccessfulLogin() {
        // Given
        LoginRequest request = new LoginRequest("testuser", rawPassword);
        String expectedToken = "jwt.token.login";

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn(expectedToken);

        // When
        AuthResponse response = authUseCase.login(request);

        // Then
        assertThat(response.token()).isEqualTo(expectedToken);
        verify(jwtUtil).generateToken("testuser");
    }
}
