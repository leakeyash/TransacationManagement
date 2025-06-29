package com.hsbc.transaction.exception

import com.hsbc.transaction.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class GlobalExceptionHandlerSpec extends Specification {
    def handler = new GlobalExceptionHandler()

    def "handleNotFound returns 404"() {
        when:
        ResponseEntity<String> resp = handler.handleNotFound(new TransactionNotFoundException("not found"))
        then:
        resp.statusCode == HttpStatus.NOT_FOUND
        resp.body == "not found"
    }

    def "handleDuplicate returns 409"() {
        when:
        ResponseEntity<String> resp = handler.handleDuplicate(new DuplicateTransactionException("dup"))
        then:
        resp.statusCode == HttpStatus.CONFLICT
        resp.body == "dup"
    }

    def "handleNotSupported returns 400"() {
        when:
        ResponseEntity<String> resp = handler.handleNotSupported(new TransactionNotSupported("bad"))
        then:
        resp.statusCode == HttpStatus.BAD_REQUEST
        resp.body == "bad"
    }

    def "handleTransactionExecution returns 500"() {
        when:
        ResponseEntity<String> resp = handler.handleTransactionExecution(new TransactionExecutionException("fail"))
        then:
        resp.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        resp.body == "fail"
    }

    def "handleOther returns 500"() {
        when:
        ResponseEntity<String> resp = handler.handleOther(new Exception("err"))
        then:
        resp.statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        resp.body.contains("Internal error: err")
    }
} 