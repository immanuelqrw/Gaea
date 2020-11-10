package com.immanuelqrw.gaea.api.model

data class Error(

    var field: String? = null,

    var value: String? = null,

    var message: String? = null

) : Response
