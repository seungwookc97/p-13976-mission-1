package com.domain.wiseSaying.wiseSaying.repository

import com.domain.wiseSaying.wiseSaying.entity.WiseSaying
import com.standard.dto.Page

interface WiseSayingRepository {
    fun save(wiseSaying: WiseSaying) : WiseSaying
    fun isEmpty(): Boolean
    fun findAll(): List<WiseSaying>
    fun findById(id: Int): WiseSaying?
    fun delete(wiseSaying: WiseSaying)
    fun clear()
    fun findByKeywordPaged(keywordType: String, keyword: String, count: Int, pageNo: Int) : Page<WiseSaying>
    fun findAllPaged(count: Int, pageNo: Int): Page<WiseSaying>
    fun findByAuthorLike(authorLike: String): List<WiseSaying>
    fun findByAuthorContent(contentLike: String): List<WiseSaying>
    fun build()
}