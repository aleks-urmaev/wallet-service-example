package com.aurmaev.wallet.controllers;

import com.aurmaev.wallet.controllers.dto.PaymentDto;
import com.aurmaev.wallet.converters.PaymentConverter;
import com.aurmaev.wallet.repositories.entities.PaymentEntity;
import com.aurmaev.wallet.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Payment", description = "Payment management API")
@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentConverter paymentConverter;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentConverter paymentConverter) {
        this.paymentService = paymentService;
        this.paymentConverter = paymentConverter;
    }

    @Operation(summary = "Create payment", description = "Create a new payment or transfer operation in the system")
    @PostMapping
    public PaymentDto createPayment(@RequestBody PaymentDto paymentDto) {
        PaymentEntity paymentEntity = paymentService.createPayment(paymentDto);
        return paymentConverter.convert(paymentEntity);
    }
}
