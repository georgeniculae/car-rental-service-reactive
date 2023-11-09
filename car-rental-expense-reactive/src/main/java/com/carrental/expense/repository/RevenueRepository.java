package com.carrental.expense.repository;

import com.carrental.document.model.Revenue;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RevenueRepository extends ReactiveMongoRepository<Revenue, ObjectId> {

    @Aggregation(pipeline = {"{$group: { _id: '', totalAmount: {$sum: $amountFromBooking}}}"})
    Mono<Double> getTotalAmount();

}
