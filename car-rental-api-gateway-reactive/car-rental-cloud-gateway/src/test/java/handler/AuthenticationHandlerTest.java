package handler;

import com.carrental.cloudgateway.handler.AuthenticationHandler;
import com.carrental.document.dto.AuthenticationRequest;
import com.carrental.dto.AuthenticationResponse;
import com.carrental.lib.security.jwt.JwtAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import util.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationHandlerTest {

    @InjectMocks
    private AuthenticationHandler authenticationHandler;

    @Mock
    private JwtAuthenticationService jwtAuthenticationService;

    @Test
    void authenticateUserTest_success() {
        AuthenticationRequest authenticationRequest =
                TestUtils.getResourceAsJson("/data/AuthenticationRequest.json", AuthenticationRequest.class);

        ServerRequest serverRequest = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .body(Mono.just(authenticationRequest));

        String token = "token";
        AuthenticationResponse authenticationResponse = new AuthenticationResponse().token(token);

        when(jwtAuthenticationService.authenticateUser(any(AuthenticationRequest.class)))
                .thenReturn(Mono.just(authenticationResponse));

        StepVerifier.create(authenticationHandler.authenticateUser(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void authenticateUserTest_noResultReturned() {
        AuthenticationRequest authenticationRequest =
                TestUtils.getResourceAsJson("/data/AuthenticationRequest.json", AuthenticationRequest.class);

        ServerRequest serverRequest = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .body(Mono.just(authenticationRequest));

        when(jwtAuthenticationService.authenticateUser(any(AuthenticationRequest.class))).thenReturn(Mono.empty());

        StepVerifier.create(authenticationHandler.authenticateUser(serverRequest))
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is4xxClientError())
                .verifyComplete();
    }

}
