package com.retailstore.models

import java.time.LocalDate

data class User(val type: UserType, val joinedDate: LocalDate)
