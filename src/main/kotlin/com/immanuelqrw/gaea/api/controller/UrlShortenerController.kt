package com.immanuelqrw.gaea.api.controller

import com.google.common.hash.Hashing
import com.immanuelqrw.gaea.api.model.Error
import com.immanuelqrw.gaea.api.model.Url
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping(value = ["/rest/url"])
class UrlShortenerController {

    @Autowired
    private val redisTemplate: RedisTemplate<String, Url>? = null

    @Value("\${redis.ttl}")
    private val ttl: Long = 0

    /**
     * Returns the original URL.
     */
    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    @ResponseBody
    fun getUrl(@PathVariable id: String?): ResponseEntity<*> {

        // get from redis
        val url: Url? = redisTemplate?.opsForValue()?.get(id)
        if (url == null) {
            val error = Error("id", id, "No such key exists")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
        }

        return ResponseEntity.ok(url)
    }

    /**
     * Returns a short URL.
     */
    @RequestMapping(method = [RequestMethod.POST])
    @ResponseBody
    fun postUrl(@RequestBody url: Url): ResponseEntity<*> {
        val validator = UrlValidator(arrayOf("http", "https"))

        // if invalid url, return error
        if (!validator.isValid(url.url)) {
            val error = Error("url", url.url, "Invalid URL")
            return ResponseEntity.badRequest().body(error)
        }
        val id: String = Hashing.murmur3_32().hashString(url.url, Charset.defaultCharset()).toString()
        url.id = id
        url.createdOn = LocalDateTime.now()

        //store in redis
        redisTemplate?.opsForValue()?.set(url.id!!, url, ttl, TimeUnit.SECONDS)
        return ResponseEntity.ok(url)
    }
}
