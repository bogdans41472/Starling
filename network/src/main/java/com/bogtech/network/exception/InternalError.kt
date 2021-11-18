package com.bogtech.network.exception

enum class InternalError {
    // Returned to host app when there was an IOException or UnknownHostException.
    NETWORK_ERROR,
    // Returned to host app when we receive an error from platform that we do not know.
    UNKNOWN_ERROR,
    // Returned to host app when 403 is received and token needs to be refresh.
    FORBIDDEN,
    // Returned to host app when 400 is received.
    BAD_REQUEST,
}