package com.carrental.agency.router;

import com.carrental.agency.handler.EmployeeHandler;
import com.carrental.agency.util.TestUtils;
import com.carrental.dto.EmployeeDto;
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
@ContextConfiguration(classes = EmployeeRouter.class)
@WebFluxTest
class EmployeeRouterTest {

    private static final String PATH = "/employees";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeHandler employeeHandler;

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void findAllEmployeesTest_success() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);
        List<EmployeeDto> employeeDtoList = List.of(employeeDto);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDtoList);

        when(employeeHandler.findAllEmployees(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<EmployeeDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(EmployeeDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void findAllEmployeesTest_forbidden() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);
        List<EmployeeDto> employeeDtoList = List.of(employeeDto);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDtoList);

        when(employeeHandler.findAllEmployees(any(ServerRequest.class))).thenReturn(serverResponse);

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
    void findEmployeeByIdTest_success() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.findEmployeeById(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<EmployeeDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(EmployeeDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void findEmployeeByIdTest_unauthorized() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.findEmployeeById(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void findEmployeesByBranchIdTest_success() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.findEmployeesByBranchId(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<EmployeeDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/branch/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(EmployeeDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void findEmployeesByBranchIdTest_unauthorized() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.findEmployeesByBranchId(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/branch/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void countEmployeesTest_success() {
        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(5);

        when(employeeHandler.countEmployees(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<Long> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void countEmployeesTest_unauthorized() {
        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(5);

        when(employeeHandler.countEmployees(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri(PATH + "/count")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void saveEmployeeTest_success() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.saveEmployee(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<EmployeeDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(EmployeeDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void saveEmployeeTest_unauthorized() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.saveEmployee(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithAnonymousUser
    void saveEmployeeTest_forbidden() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.saveEmployee(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void updateEmployeeTest_success() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.updateEmployee(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<EmployeeDto> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .put()
                .uri(PATH + "/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(EmployeeDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(employeeDto)
                .verifyComplete();
    }

    @Test
    @WithAnonymousUser
    void updateEmployeeTest_unauthorized() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.updateEmployee(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .put()
                .uri(PATH + "/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @WithAnonymousUser
    void updateEmployeeTest_forbidden() {
        EmployeeDto employeeDto = TestUtils.getResourceAsJson("/data/EmployeeDto.json", EmployeeDto.class);

        Mono<ServerResponse> serverResponse = ServerResponse.ok().bodyValue(employeeDto);

        when(employeeHandler.updateEmployee(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.put()
                .uri(PATH + "/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @WithMockUser(value = "admin", username = "admin", password = "admin", roles = "ADMIN")
    void deleteEmployeeByIdTest_success() {
        Mono<ServerResponse> serverResponse = ServerResponse.noContent().build();

        when(employeeHandler.deleteEmployeeById(any(ServerRequest.class))).thenReturn(serverResponse);

        Flux<Void> responseBody = webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .delete()
                .uri(PATH + "/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNoContent()
                .returnResult(Void.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteEmployeeByIdTest_forbidden() {
        Mono<ServerResponse> serverResponse = ServerResponse.noContent().build();

        when(employeeHandler.deleteEmployeeById(any(ServerRequest.class))).thenReturn(serverResponse);

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .delete()
                .uri(PATH + "/{id}", "64f361caf291ae086e179547")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

}
