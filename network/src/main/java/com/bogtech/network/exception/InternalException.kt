package com.bogtech.network.exception

class InternalException(
    val errorEnum: InternalError,
    val developerMessage: String,
): Exception()