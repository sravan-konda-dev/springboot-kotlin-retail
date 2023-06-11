package com.retailstore.controller

import com.retailstore.requests.Requests
import com.retailstore.service.DiscountService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal


@RestController
class PaymentController(private val discountService: DiscountService) {

    @PostMapping("/store/payment")
    fun netPayment(@RequestBody paymentRequests: Requests): BigDecimal {
        return discountService.discountCalculation(paymentRequests.user, paymentRequests.bill)
    }
}