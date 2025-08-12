package com

import com.global.app.SingletoneScope.wiseSayingController
import com.global.rq.Rq

class App {
    fun run() {
        println("== 명언 앱 ==")

        while (true) {
            print("명령) ")
            val input = readLine()!!.trim()

            val rq = Rq(input)

            when (rq.action) {
                "종료" -> {
                    println("프로그램을 종료합니다")
                    break
                }

                "빌드" -> wiseSayingController.actionBuild(rq)
                "등록" -> wiseSayingController.actionWrite(rq)
                "목록" -> wiseSayingController.actionList(rq)
                "삭제" -> wiseSayingController.actionDelete(rq)
                "수정" -> wiseSayingController.actionModify(rq)
            }
        }

    }
}