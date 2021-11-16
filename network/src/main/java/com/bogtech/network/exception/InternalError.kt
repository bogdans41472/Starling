package com.bogtech.network.exception

enum class InternalError {
    // Returned to host app when there was an IOException or UnknownHostException
    NETWORK_ERROR,
}