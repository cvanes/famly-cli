package com.cvanes.famly

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType

fun newHttpClient() = HttpClient(CIO) {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.NONE
    }

    install(JsonFeature) {
        serializer = JacksonSerializer {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            dateFormat = StdDateFormat().withColonInTimeZone(true)
            findAndRegisterModules()
        }
    }

    defaultRequest {
        contentType(ContentType.Application.Json)
        accept(ContentType.Any)
    }

    engine {
        maxConnectionsCount = 50
        endpoint {
            maxConnectionsPerRoute = 10
            keepAliveTime = 5000
            connectTimeout = 5000
            socketTimeout = 30000
        }
    }
}