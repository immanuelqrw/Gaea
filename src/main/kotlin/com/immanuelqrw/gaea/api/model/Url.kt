package com.immanuelqrw.gaea.api.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime

data class Url(
    var id: String? = null,

    val url: String? = null,

    @JsonSerialize(using = LocalDateTimeSerializer::class)
    var createdOn: LocalDateTime? = null

) : Response
