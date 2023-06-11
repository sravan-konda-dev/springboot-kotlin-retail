package com.retailstore.requests

import com.retailstore.models.Bill
import com.retailstore.models.User

data class Requests(val user: User, val bill: Bill)
