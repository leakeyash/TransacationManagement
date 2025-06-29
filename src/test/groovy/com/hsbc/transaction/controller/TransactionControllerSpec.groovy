package com.hsbc.transaction.controller

import com.hsbc.transaction.model.PageResponse
import com.hsbc.transaction.model.TransactionRequest
import com.hsbc.transaction.model.TransactionResponse
import com.hsbc.transaction.service.TransactionService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class TransactionControllerSpec extends Specification {
    def service = Mock(TransactionService)
    def controller = new TransactionController(service)

    def "create returns 201 with body"() {
        given:
        def req = Mock(TransactionRequest)
        def resp = Mock(TransactionResponse) { transactionId() >> "tid" }
        service.create(req) >> resp
        when:
        def result = controller.create(req)
        then:
        result.statusCode.value() == 201
        result.body == resp
        result.headers.getLocation().toString().endsWith("/api/transactions/tid")
    }

    def "update returns response"() {
        given:
        def req = Mock(TransactionRequest)
        def resp = Mock(TransactionResponse)
        service.update("tid", req) >> resp
        when:
        def result = controller.update("tid", req)
        then:
        result == resp
    }

    def "delete returns no content"() {
        when:
        def result = controller.delete("tid")
        then:
        result.statusCode.value() == 204
    }

    def "getById returns response"() {
        def resp = Mock(TransactionResponse)
        service.getById("tid") >> resp
        when:
        def result = controller.getById("tid")
        then:
        result == resp
    }

    def "getAllPaged returns page response"() {
        def resp = Mock(PageResponse)
        service.getAllPaged(_) >> resp
        when:
        def result = controller.getAllPaged(0, 10)
        then:
        result == resp
    }
} 