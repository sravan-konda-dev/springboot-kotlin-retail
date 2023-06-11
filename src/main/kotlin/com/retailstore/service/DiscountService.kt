package com.retailstore.service

import com.retailstore.models.Bill
import com.retailstore.models.User
import java.math.BigDecimal

interface DiscountService {

    fun discountCalculation(user: User, bill: Bill): BigDecimal
}