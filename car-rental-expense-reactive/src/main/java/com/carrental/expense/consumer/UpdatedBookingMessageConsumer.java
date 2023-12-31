package com.carrental.expense.consumer;

import com.carrental.dto.BookingDto;
import com.carrental.dto.InvoiceDto;
import com.carrental.expense.service.InvoiceService;
import com.carrental.lib.exceptionhandling.CarRentalResponseStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UpdatedBookingMessageConsumer {

    @Value("${bookingConsumer.isMessageAckEnabled:false}")
    private boolean isMessageAckEnabled;

    private final InvoiceService invoiceService;

    @Bean
    public Function<Flux<Message<BookingDto>>, Mono<Void>> updatedBookingConsumer() {
        return messageFlux -> messageFlux.concatMap(this::processMessage)
                .then();
    }

    private Mono<InvoiceDto> processMessage(Message<BookingDto> message) {
        return invoiceService.updateInvoiceAfterBookingUpdate(message.getPayload())
                .doOnNext(userDto -> {
                    log.info("Invoice updated: {}", userDto);

                    if (isMessageAckEnabled) {
                        this.sendMessageAcknowledgement(message.getHeaders());
                    }
                })
                .onErrorResume(e -> {
                    log.error("Exception during processing updated booking message: {}", e.getMessage(), e);

                    return Mono.empty();
                });
    }

    private void sendMessageAcknowledgement(MessageHeaders messageHeaders) {
        Optional.ofNullable(messageHeaders.get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class))
                .orElseThrow(
                        () -> new CarRentalResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "There is no Kafka acknowledgement in message headers"
                        )
                )
                .acknowledge();
    }

}
