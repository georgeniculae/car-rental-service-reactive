package com.carrental.audit.consumer;

import com.carrental.audit.service.AuditService;
import com.carrental.document.dto.AuditLogInfoDto;
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
public class ExpenseAuditLogInfoConsumerMessage {

    @Value("${auditConsumer.isMessageAckEnabled:false}")
    private boolean isMessageAckEnabled;

    private final AuditService auditService;

    @Bean
    public Function<Flux<Message<AuditLogInfoDto>>, Mono<Void>> expenseAuditLogInfoConsumer() {
        return messageFlux -> messageFlux.concatMap(this::processMessage)
                .then();
    }

    private Mono<AuditLogInfoDto> processMessage(Message<AuditLogInfoDto> message) {
        return auditService.saveAuditLogInfo(message.getPayload())
                .doOnNext(auditLogInfoDto -> {
                    log.info("Audit log saved: {}", auditLogInfoDto);

                    if (isMessageAckEnabled) {
                        this.sendMessageAcknowledgement(message.getHeaders());
                    }
                })
                .onErrorResume(e -> {
                    log.error("Exception during processing saved audit log message: {}", e.getMessage(), e);

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
