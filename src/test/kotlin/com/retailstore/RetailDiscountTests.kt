package com.retailstore

import com.retailstore.models.Product
import com.retailstore.models.ProductType
import com.retailstore.models.User
import com.retailstore.models.UserType
import com.retailstore.utility.DiscountLogic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
class RetailDiscountTests {

    @Test
    fun testCalculateTotal() {
        val items = mutableListOf<Product>()
        items.add(Product(ProductType.CLOTHES, BigDecimal("50.0")))
        items.add(Product(ProductType.PLASTICS, BigDecimal("150.0")))
        items.add(Product(ProductType.CLOTHES, BigDecimal("200.0")))

        val helper = DiscountLogic()
        val total = helper.calculateTotal(items)
        assertEquals(400.00, total.toDouble(), 0.0)
    }

    @Test
    fun testCalculateTotalPerType() {
        val items = mutableListOf<Product>()
        items.add(Product(ProductType.CLOTHES, BigDecimal("100.0")))
        items.add(Product(ProductType.PLASTICS, BigDecimal("100.0")))
        items.add(Product(ProductType.CLOTHES, BigDecimal("100.0")))
        items.add(Product(ProductType.GROCERY, BigDecimal("100.0")))
        items.add(Product(ProductType.GROCERY, BigDecimal("100.0")))

        val helper = DiscountLogic()
        val total = helper.calculateTotalPerType(items, ProductType.GROCERY)
        assertEquals(200.00, total.toDouble(), 0.0)
    }

    @Test
    fun testCalculateTotal_GroceriesOnly() {
        val products = mutableListOf<Product>()
        products.add(Product(ProductType.GROCERY, BigDecimal("100.0")))
        products.add(Product(ProductType.CLOTHES, BigDecimal("100.0")))
        products.add(Product(ProductType.GROCERY, BigDecimal("100.0")))

        val helper = DiscountLogic()
        val total = helper.calculateTotalPerType(products, ProductType.GROCERY)
        assertEquals(200.00, total.toDouble(), 0.0)
    }

    @Test
    fun testCalculateDiscount_10pct() {
        val discountLogic = DiscountLogic()
        val total = discountLogic.calculateDiscount(BigDecimal("600"), BigDecimal("0.1"))
        assertEquals(540.00, total.toDouble(), 0.0)
    }

    @Test
    fun testCalculateDiscount_30pct() {
        val discountLogic = DiscountLogic()
        val total = discountLogic.calculateDiscount(BigDecimal("1000"), BigDecimal("0.3"))
        assertEquals(700.00, total.toDouble(), 0.0)
    }

    @Test
    fun testCalculateDiscount_0pct() {
        val discountLogic = DiscountLogic()
        val total = discountLogic.calculateDiscount(BigDecimal("1500"), BigDecimal("0.0"))
        assertEquals(1500.00, total.toDouble(), 0.0)
    }

    @Test
    fun testCalculateDiscount_100pct() {
        val discountLogic = DiscountLogic()
        val total = discountLogic.calculateDiscount(BigDecimal("1000"), BigDecimal("1.0"))
        assertEquals(0.0, total.toDouble(), 0.0)
    }

    @Test
    fun testCalculateDiscount_error() {
        val discountLogic = DiscountLogic()
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            discountLogic.calculateDiscount(BigDecimal("1000"), BigDecimal("2.0"))
        }
    }

    @Test
    fun testGetUserSpecificDiscount_affiliate() {
        val user = User(UserType.AFFILIATE, LocalDate.now())
        val discountLogic = DiscountLogic()
        val discount = discountLogic.getUserDiscount(user)
        assertEquals(0.1, discount.toDouble(), 0.0)
    }

    @Test
    fun testUserDiscount_employee() {
        val user = User(UserType.EMPLOYEE, LocalDate.now())
        val discountLogic = DiscountLogic()
        val discount = discountLogic.getUserDiscount(user)
        assertEquals(0.3, discount.toDouble(), 0.0)
    }

    @Test
    fun testUserDiscount_customer_old() {
        val joinedDate = LocalDate.of(2016, 2, 23)
        val user = User(UserType.CUSTOMER, joinedDate)
        val discountLogic = DiscountLogic()
        val discount = discountLogic.getUserDiscount(user)
        assertEquals(0.05, discount.toDouble(), 0.0)
    }

    @Test
    fun testUserDiscount_customer_new() {
        val user = User(UserType.CUSTOMER, LocalDate.now())
        val discountLogic = DiscountLogic()
        val discount = discountLogic.getUserDiscount(user)
        assertEquals(0.0, discount.toDouble(), 0.0)
    }

    @Test
    fun testGetUserDiscount_nullUser() {
        val discountLogic = DiscountLogic()
        val user: User? = null
        val discount = discountLogic.getUserDiscount(user)
        assertEquals(BigDecimal.ZERO, discount)
    }


    @Test
    fun testIsCustomerOver_a_year() {
        val discountLogic = DiscountLogic()
        val joinedDate = LocalDate.now().minusYears(1)
        val isEligible = discountLogic.isCustomerOver(joinedDate, 1.0)
        assertFalse(isEligible)
    }

    @Test
    fun testIfCustomerOver_2_year() {
        val discountLogic = DiscountLogic()
        val joinedDate = LocalDate.now().minusYears(3)
        val isEligible = discountLogic.isCustomerOver(joinedDate, 2.1)
        assertTrue(isEligible)
    }

    @Test
    fun testIsCustomerOver_3_years() {
        val discountLogic = DiscountLogic()
        val joinedDate = LocalDate.now().minusYears(3)
        val isEligible = discountLogic.isCustomerOver(joinedDate, 2.0)
        assertTrue(isEligible)
    }

    @Test
    fun testCalculateBillDiscount() {
        val discountLogic = DiscountLogic()
        val amount = discountLogic.calculateBillDiscount(BigDecimal("987"), BigDecimal("100"), BigDecimal("5"))
        assertEquals(BigDecimal("45"), amount)
    }

    @Test
    fun testCalculateBillDiscount_2() {
        val discountLogic = DiscountLogic()
        val amount = discountLogic.calculateBillDiscount(BigDecimal("2500"), BigDecimal("100"), BigDecimal("5"))
        assertEquals(BigDecimal("125"), amount)
    }

    @Test
    fun testCalculateBillDiscount_3() {
        val discountLogic = DiscountLogic()
        val amount = discountLogic.calculateBillDiscount(BigDecimal("532"), BigDecimal("100"), BigDecimal("5"))
        assertEquals(BigDecimal("25"), amount)
    }
}

