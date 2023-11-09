package com.carrental.expense.router;

import com.carrental.dto.RevenueDto;
import com.carrental.expense.handler.RevenueHandler;
import com.carrental.expense.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RevenueRouter.class)
@WebFluxTest
public class RevenueRouterTest {

    private static final String PATH = "/revenues";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private RevenueHandler revenueHandler;

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void findAllRevenuesTest_success() {
        RevenueDto revenueDto =
                TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(revenueDto));

        when(revenueHandler.findAllRevenues(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<RevenueDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(RevenueDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(revenueDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void findAllRevenuesTest_unauthorized() {
        RevenueDto revenueDto =
                TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(revenueDto));

        when(revenueHandler.findAllRevenues(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void getTotalAmountTest_success() {
        RevenueDto revenueDto =
                TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(revenueDto));

        when(revenueHandler.getTotalAmount(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<RevenueDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/total")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(RevenueDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(revenueDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void getTotalAmountTest_unauthorized() {
        RevenueDto revenueDto =
                TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(revenueDto));

        when(revenueHandler.getTotalAmount(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/total")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void findRevenuesByDateTest_success() {
        RevenueDto revenueDto =
                TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(revenueDto));

        when(revenueHandler.findRevenuesByDate(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<RevenueDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/{date}", "2023-09-25")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(RevenueDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(revenueDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void findRevenuesByDateTest_unauthorized() {
        RevenueDto revenueDto =
                TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(List.of(revenueDto));

        when(revenueHandler.findRevenuesByDate(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/{date}", "2023-09-25")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

}
