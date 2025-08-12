package com.domain.wiseSaying.wiseSaying.service

import com.domain.wiseSaying.wiseSaying.entity.WiseSaying
import com.global.app.SingletoneScope
import com.standard.dto.Page

class WiseSayingService {
    private val wiseSayingRepository = SingletoneScope.wiseSayingRepository;

    fun write(content: String, author: String): WiseSaying {
        return wiseSayingRepository.save(WiseSaying(content = content, author = author))
    }

    fun findAll(): List<WiseSaying> {
        return wiseSayingRepository.findAll()
    }

    fun findById(id: Int): WiseSaying? {
        return wiseSayingRepository.findById(id)
    }

    fun delete(wiseSaying: WiseSaying) {
        return wiseSayingRepository.delete(wiseSaying)
    }

    fun isEmpty(): Boolean {
        return wiseSayingRepository.isEmpty()
    }

    fun findByKeyword(keywordType: String, keyword: String): List<WiseSaying> {
        return when (keywordType) {
            "author" -> wiseSayingRepository.findByAuthorLike("%$keyword%")
            else -> wiseSayingRepository.findByAuthorContent("%$keyword%")
        }
    }

    fun modify(wiseSaying: WiseSaying, content: String, author: String) {
        wiseSaying.modify(content, author)

        wiseSayingRepository.save(wiseSaying)
    }

    fun build() {
        wiseSayingRepository.build()
    }


    fun findByKeywordPaged(keywordType: String, keyword: String, count: Int, pageNo: Int): Page<WiseSaying> {
        return wiseSayingRepository.findByKeywordPaged(keywordType, keyword, count, pageNo)
    }

    fun findAllPaged(count: Int, pageNo: Int): Page<WiseSaying> {
        return wiseSayingRepository.findAllPaged(count, pageNo)

    }


}