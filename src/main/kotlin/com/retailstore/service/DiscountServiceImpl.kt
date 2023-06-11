package com.retailstore.service

import com.retailstore.models.Bill
import com.retailstore.models.ProductType
import com.retailstore.models.User
import com.retailstore.utility.DiscountLogic
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DiscountServiceImpl : DiscountService {

    override fun discountCalculation(user: User, bill: Bill): BigDecimal {
        val discountLogic = DiscountLogic()

        val totalAmount = discountLogic.calculateTotal(bill.products)
        val groceryAmount = discountLogic.calculateTotalPerType(bill.products, ProductType.GROCERY)
        val nonGroceryAmount = totalAmount.subtract(groceryAmount)
        val userDiscount = discountLogic.getUserDiscount(user)
        val billsDiscount =
                discountLogic.calculateBillDiscount(totalAmount, BigDecimal(100), BigDecimal(5))

        var netPayableAmount = nonGroceryAmount
        if (nonGroceryAmount > BigDecimal.ZERO) {
            netPayableAmount = discountLogic.calculateDiscount(nonGroceryAmount, userDiscount)
        }
        netPayableAmount = netPayableAmount.add(groceryAmount).subtract(billsDiscount)

        return netPayableAmount
    }
}