package com.carrental.expense.service;

import com.carrental.document.model.Invoice;
import com.carrental.document.model.Revenue;
import com.carrental.dto.RevenueDto;
import com.carrental.expense.mapper.RevenueMapper;
import com.carrental.expense.mapper.RevenueMapperImpl;
import com.carrental.expense.model.Outbox;
import com.carrental.expense.repository.InvoiceRepository;
import com.carrental.expense.repository.RevenueRepository;
import com.carrental.expense.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RevenueServiceTest {

    @InjectMocks
    private RevenueService revenueService;

    @Mock
    private RevenueRepository revenueRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private OutboxService outboxService;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Spy
    private RevenueMapper revenueMapper = new RevenueMapperImpl();

    @Test
    void findAllRevenuesTest_success() {
        Revenue revenue = TestUtils.getResourceAsJson("/data/Revenue.json", Revenue.class);
        RevenueDto revenueDto = TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        when(revenueRepository.findAll()).thenReturn(Flux.just(revenue));

        StepVerifier.create(revenueService.findAllRevenues())
                .expectNext(revenueDto)
                .verifyComplete();

        verify(revenueMapper, times(1)).mapEntityToDto(any(Revenue.class));
    }

    @Test
    void findAllRevenuesTest_errorOnFindingAllRevenues() {
        when(revenueRepository.findAll()).thenReturn(Flux.error(new Throwable()));

        StepVerifier.create(revenueService.findAllRevenues())
                .expectError()
                .verify();
    }

    @Test
    void getTotalAmountTest_success() {
        when(revenueRepository.getTotalAmount()).thenReturn(Mono.just(50D));

        StepVerifier.create(revenueService.getTotalAmount())
                .expectNext(50D)
                .verifyComplete();
    }

    @Test
    void getTotalAmountTest_errorOnGettingTotalAmount() {
        when(revenueRepository.getTotalAmount()).thenReturn(Mono.error(new Throwable()));

        StepVerifier.create(revenueService.getTotalAmount())
                .expectError()
                .verify();
    }

    @Test
    void saveInvoiceAndRevenueTransactionalTest_success() {
        Invoice invoice = TestUtils.getResourceAsJson("/data/Invoice.json", Invoice.class);
        Outbox outbox = TestUtils.getResourceAsJson("/data/Outbox.json", Outbox.class);
        Revenue revenue = TestUtils.getResourceAsJson("/data/Revenue.json", Revenue.class);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(Mono.just(invoice));
        when(outboxService.saveOutbox(any(Invoice.class), any(Outbox.Operation.class))).thenReturn(Mono.just(outbox));
        when(revenueRepository.save(any(Revenue.class))).thenReturn(Mono.just(revenue));

        StepVerifier.create(revenueService.saveInvoiceRevenueAndOutboxTransactional(invoice))
                .assertNext(actualInvoice -> assertThat(invoice).usingRecursiveComparison().isEqualTo(actualInvoice))
                .verifyComplete();
    }

    @Test
    void saveInvoiceAndRevenueTransactionalTest_errorOnSavingRevenue() {
        Invoice invoice = TestUtils.getResourceAsJson("/data/Invoice.json", Invoice.class);
        Outbox outbox = TestUtils.getResourceAsJson("/data/Outbox.json", Outbox.class);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(Mono.just(invoice));
        when(outboxService.saveOutbox(any(Invoice.class), any(Outbox.Operation.class))).thenReturn(Mono.just(outbox));
        when(revenueRepository.save(any(Revenue.class))).thenReturn(Mono.error(new Throwable()));

        StepVerifier.create(revenueService.saveInvoiceRevenueAndOutboxTransactional(invoice))
                .expectError()
                .verify();
    }

    @Test
    void findRevenueByDateTest_success() {
        Revenue revenue = TestUtils.getResourceAsJson("/data/Revenue.json", Revenue.class);
        RevenueDto revenueDto = TestUtils.getResourceAsJson("/data/RevenueDto.json", RevenueDto.class);

        when(reactiveMongoTemplate.find(any(Query.class), eq(Revenue.class))).thenReturn(Flux.just(revenue));

        StepVerifier.create(revenueService.findRevenuesByDate("2023-09-25"))
                .expectNext(revenueDto)
                .verifyComplete();

        verify(revenueMapper, times(1)).mapEntityToDto(any(Revenue.class));
    }

    @Test
    void findRevenueByDateTest_errorOnFindingByDateOfRevenue() {
        when(reactiveMongoTemplate.find(any(Query.class), eq(Revenue.class))).thenReturn(Flux.error(new Throwable()));

        StepVerifier.create(revenueService.findRevenuesByDate("2023-09-25"))
                .expectError()
                .verify();
    }

}
