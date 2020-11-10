package com.immanuelqrw.gaea.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.immanuelqrw.gaea.api.model.Url
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig {

    @Autowired
    var mapper: ObjectMapper? = null

    @Autowired
    var connectionFactory: RedisConnectionFactory? = null

    @Bean
    fun redisTemplate(): RedisTemplate<String, Url> {
        val redisTemplate: RedisTemplate<String, Url> = RedisTemplate()
        val valueSerializer = Jackson2JsonRedisSerializer(Url::class.java)
        valueSerializer.setObjectMapper(mapper)
        redisTemplate.connectionFactory = connectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = valueSerializer
        return redisTemplate
    }

}
