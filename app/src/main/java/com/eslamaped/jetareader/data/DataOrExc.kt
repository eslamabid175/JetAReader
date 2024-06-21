package com.eslamaped.jetareader.data

data class DataOrExc<T, Boolean, E : Exception? > (
    var data: T? = null,
    var loading: Boolean? = null,
    var e: E? = null
)

