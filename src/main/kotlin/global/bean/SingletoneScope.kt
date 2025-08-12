package com.global.app

import com.domain.wiseSaying.wiseSaying.controller.WiseSayingController
import com.domain.wiseSaying.wiseSaying.repository.WiseSayingFileRepository
import com.domain.wiseSaying.wiseSaying.repository.WiseSayingRepository
import com.domain.wiseSaying.wiseSaying.service.WiseSayingService

object SingletoneScope {
    val wiseSayingController by lazy { WiseSayingController() }
    val wiseSayingService by lazy { WiseSayingService() }
    val wiseSayingRepository: WiseSayingRepository by lazy { WiseSayingFileRepository() }

}