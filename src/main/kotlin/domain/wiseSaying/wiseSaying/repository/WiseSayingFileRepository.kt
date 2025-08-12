package com.domain.wiseSaying.wiseSaying.repository

import com.domain.wiseSaying.wiseSaying.entity.WiseSaying
import com.global.app.AppConfig
import com.standard.dto.Page
import com.standard.util.json.JsonUtil
import java.nio.file.Path

class WiseSayingFileRepository: WiseSayingRepository {

    val tableDirPath: Path
        get() {
            return AppConfig.dbDirPath.resolve("wiseSaying")
        }

    override fun save(wiseSaying: WiseSaying): WiseSaying {
        if (wiseSaying.isNew()) wiseSaying.id = genNextId()

        saveOnDisk(wiseSaying)

        return wiseSaying
    }

    override fun build() {
        mkTableDirsIfNotExists()

        val mapList = findAll()
            .map(WiseSaying::map)

        JsonUtil.toString(mapList)
            .let {
                tableDirPath
                    .resolve("data.json")
                    .toFile()
                    .writeText(it)
            }
    }

    override fun isEmpty(): Boolean {
        return tableDirPath.toFile()
            .listFiles()
            ?.filter { it.name != "data.json" }
            ?.none { it.name.endsWith(".json") }
            ?: true
    }

    override fun clear() {
        tableDirPath.toFile().deleteRecursively()
    }

    override fun findAll(): List<WiseSaying> {
        return tableDirPath.toFile()
            .listFiles()
            ?.filter { it.name != "data.json" }
            ?.filter { it.name.endsWith(".json") }
            ?.map { it.readText() }
            ?.map(WiseSaying.Companion::fromJsonStr)
            ?.sortedByDescending { it.id }
            .orEmpty()
    }

    override fun findById(id: Int): WiseSaying? {
        return tableDirPath
            .resolve("$id.json")
            .toFile()
            .takeIf { it.exists() }
            ?.readText()
            ?.let(WiseSaying.Companion::fromJsonStr)
    }

    override fun delete(wiseSaying: WiseSaying) {
        tableDirPath
            .resolve("${wiseSaying.id}.json")
            .toFile()
            .takeIf { it.exists() }
            ?.delete()
    }

    override fun findByAuthorLike(authorLike: String): List<WiseSaying> {
        val keyword = authorLike.replace("%", "")

        if (keyword.isBlank()) return findAll()

        return findAll().filter { author ->
            when {
                authorLike.startsWith("%") && authorLike.endsWith("%") -> author.author.contains(keyword)
                authorLike.startsWith("%") -> author.author.endsWith(keyword)
                authorLike.endsWith("%") -> author.author.startsWith(keyword)
                else -> author.author == keyword
            }
        }
    }

    override fun findByAuthorContent(contentLike: String): List<WiseSaying> {
        val keyword = contentLike.replace("%", "")

        if (keyword.isBlank()) return findAll()

        return findAll().filter { content ->
            when {
                contentLike.startsWith("%") && contentLike.endsWith("%") -> content.content.contains(keyword)
                contentLike.startsWith("%") -> content.content.endsWith(keyword)
                contentLike.endsWith("%") -> content.content.startsWith(keyword)
                else -> content.content == keyword
            }
        }
    }

    override fun findAllPaged(count: Int, pageNo: Int): Page<WiseSaying> {
        val wiseSayings = findAll()

        val content = wiseSayings
            .drop((pageNo - 1) * count)
            .take(count)

        return Page(wiseSayings.size, count, pageNo, "", "", content)
    }

    override fun findByKeywordPaged(keywordType: String, keyword: String, count: Int, pageNo: Int): Page<WiseSaying> {
        val wiseSayings = when (keywordType) {
            "author" -> findByAuthorLike("%$keyword%")
            "content" -> findByAuthorContent("%$keyword%")
            else -> emptyList()
        }

        val content = wiseSayings
            .drop((pageNo - 1) * count)
            .take(count)

        return Page(wiseSayings.size, count, pageNo, keywordType, keyword, content)
    }

    internal fun saveLastId(lastId: Int) {
        mkTableDirsIfNotExists()

        tableDirPath.resolve("lastId.txt")
            .toFile()
            .writeText(lastId.toString())
    }

    internal fun loadLastId(): Int {
        return try {
            tableDirPath.resolve("lastId.txt")
                .toFile()
                .readText()
                .toInt()
        } catch (e: Exception) {
            0
        }
    }
    private fun saveOnDisk(wiseSaying: WiseSaying) {
        mkTableDirsIfNotExists()

        val wiseSayingFile = tableDirPath.resolve("${wiseSaying.id}.json")
        wiseSayingFile.toFile().writeText(wiseSaying.jsonStr)
    }


    private fun mkTableDirsIfNotExists() {
        tableDirPath.toFile().run {
            if (!exists()) {
                mkdirs()
            }
        }
    }
    private fun genNextId(): Int {
        return (loadLastId() + 1).also {
            saveLastId(it)
        }
    }
}