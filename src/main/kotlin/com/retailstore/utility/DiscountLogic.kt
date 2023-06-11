package com.retailstore.utility

import com.retailstore.models.Product
import com.retailstore.models.ProductType
import com.retailstore.models.User
import com.retailstore.models.UserType
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate



class DiscountLogic {

    private companion object {
        const val YEARS_FOR_DISCOUNT = 2.0
        const val EMPLOYEE_DISCOUNT = 0.30
        const val AFFILIATE_DISCOUNT = 0.10
        const val CUSTOMER_DISCOUNT = 0.05
    }

    fun calculateTotal(products: List<Product>?): BigDecimal {
        return products?.sumOf { it.price } ?: BigDecimal.ZERO
    }

    fun calculateTotalPerType(products: List<Product>?, type: ProductType): BigDecimal {
        return products?.filter { it.type == type }?.sumOf { it.price } ?: BigDecimal.ZERO
    }

    fun getUserDiscount(user: User?): BigDecimal {
        return user?.let {
            when (it.type) {
                UserType.EMPLOYEE -> BigDecimal(EMPLOYEE_DISCOUNT).setScale(3, RoundingMode.HALF_EVEN)
                UserType.AFFILIATE -> BigDecimal(AFFILIATE_DISCOUNT).setScale(3, RoundingMode.HALF_EVEN)
                UserType.CUSTOMER ->
                    if (isCustomerOver(user.joinedDate, YEARS_FOR_DISCOUNT)) {
                        BigDecimal(CUSTOMER_DISCOUNT).setScale(2, RoundingMode.HALF_EVEN)
                    } else {
                        BigDecimal.ZERO
                    }
            }
        } ?: BigDecimal.ZERO
    }




     fun isCustomerOver(joinedDate: LocalDate, years: Double): Boolean {
        val currentDate = LocalDate.now()
        return joinedDate.plusYears(years.toLong()).isBefore(currentDate)
    }

    fun calculateBillDiscount(totalAmount: BigDecimal, amount: BigDecimal, discountAmount: BigDecimal): BigDecimal {
        val discAmount = totalAmount.divide(amount).toInt()
        return discountAmount.multiply(BigDecimal(discAmount))
    }

    fun calculateDiscount(amount: BigDecimal, discount: BigDecimal): BigDecimal {
        if (discount > BigDecimal.ONE) {
            throw IllegalArgumentException("Discount cannot exceed 100%")
        }
        return amount.subtract(amount.multiply(discount))
    }
}



