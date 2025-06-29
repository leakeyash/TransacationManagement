package com.hsbc.transaction.model

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class PageResponseSpec extends Specification {
    def "should construct PageResponse from Page"() {
        given:
        def list = [1, 2, 3]
        def page = new PageImpl(list, PageRequest.of(1, 3), 10)
        when:
        def resp = new PageResponse(page)
        then:
        resp.content == list
        resp.totalPages == 4
        resp.totalElements == 10
        resp.number == 1
        resp.size == 3
    }
} 