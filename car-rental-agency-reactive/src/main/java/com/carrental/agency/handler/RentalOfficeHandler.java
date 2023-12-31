package com.carrental.agency.handler;

import com.carrental.agency.service.RentalOfficeService;
import com.carrental.dto.RentalOfficeDto;
import com.carrental.lib.util.ServerRequestUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RentalOfficeHandler {

    private static final String ID = "id";
    private static final String NAME = "name";
    private final RentalOfficeService rentalOfficeService;

    public Mono<ServerResponse> findAllRentalOffices(ServerRequest serverRequest) {
        return rentalOfficeService.findAllRentalOffices()
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(rentalOfficeDtoList -> ServerResponse.ok().bodyValue(rentalOfficeDtoList))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findRentalOfficeById(ServerRequest serverRequest) {
        return rentalOfficeService.findRentalOfficeById(ServerRequestUtil.getPathVariable(serverRequest, ID))
                .flatMap(rentalOfficeDto -> ServerResponse.ok().bodyValue(rentalOfficeDto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findRentalOfficesByNameInsensitiveCase(ServerRequest serverRequest) {
        return rentalOfficeService.findRentalOfficesByNameInsensitiveCase(ServerRequestUtil.getPathVariable(serverRequest, NAME))
                .collectList()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(rentalOfficeDtoList -> ServerResponse.ok().bodyValue(rentalOfficeDtoList))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> countRentalOffices(ServerRequest serverRequest) {
        return rentalOfficeService.countRentalOffices()
                .flatMap(numberOfRentalOffices -> ServerResponse.ok().bodyValue(numberOfRentalOffices));
    }

    public Mono<ServerResponse> saveRentalOffice(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RentalOfficeDto.class)
                .flatMap(rentalOfficeService::saveRentalOffice)
                .flatMap(rentalOfficeDto -> ServerResponse.ok().bodyValue(rentalOfficeDto));
    }

    public Mono<ServerResponse> updateRentalOffice(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RentalOfficeDto.class)
                .flatMap(rentalOfficeDto ->
                        rentalOfficeService.updateRentalOffice(ServerRequestUtil.getPathVariable(serverRequest, ID), rentalOfficeDto))
                .flatMap(rentalOfficeDto -> ServerResponse.ok().bodyValue(rentalOfficeDto));
    }

    public Mono<ServerResponse> deleteRentalOfficeById(ServerRequest serverRequest) {
        return rentalOfficeService.deleteRentalOfficeById(ServerRequestUtil.getPathVariable(serverRequest, ID))
                .then(ServerResponse.noContent().build());
    }

}
