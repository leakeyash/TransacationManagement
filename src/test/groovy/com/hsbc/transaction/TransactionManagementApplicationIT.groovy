package com.hsbc.transaction

import com.hsbc.transaction.model.TransactionRequest
import com.hsbc.transaction.model.TransactionType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionManagementApplicationIT extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    def "application context loads"() {
        expect:
        restTemplate != null
    }

    def "GET /api/transactions?page=0&size=1 should return page"() {
        when:
        ResponseEntity resp = restTemplate.getForEntity("/api/transactions?page=0&size=1", Map)
        then:
        resp.statusCode == HttpStatus.OK
        resp.body.containsKey("content")
    }
} 