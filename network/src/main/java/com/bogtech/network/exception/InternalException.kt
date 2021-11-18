package com.bogtech.network.exception

/**
 * Class responsible for holding errorEnum, developerMessage and throwable.
 */
class InternalException(
    val errorEnum: InternalError,
    val developerMessage: String,
    val throwable: Throwable? = null,
): Exception()