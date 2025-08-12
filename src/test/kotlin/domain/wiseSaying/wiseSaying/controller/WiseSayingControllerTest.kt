package domain.wiseSaying.wiseSaying.controller

import TestRunner
import com.global.app.SingletoneScope
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WiseSayingControllerTest {
    @BeforeEach
    fun setUp() {
        SingletoneScope.wiseSayingRepository.clear()
    }

    @Test
    @DisplayName("명언 작성 테스트")
    fun t1(){
        val result = TestRunner.run(
            """
                등록
                나의 죽음을 적들에게 알리지 말라.
                충무공 이순신
            """
        )


        assertThat(result).contains("명언 : ")
        assertThat(result).contains("작가 : ")
        assertThat(result).contains("1번 명언이 등록되었습니다.")

    }

    @Test
    @DisplayName("명언 목록 조회 테스트")
    fun t2() {
        val result = TestRunner.run(
            """
            등록
            나의 죽음을 적들에게 알리지 말라.
            충무공 이순신
            등록
            천재는 99%의 노력과 1%의 영감이다.
            에디슨
            목록
        """
        )

        assertThat(result).contains("1 / 충무공 이순신 / 나의 죽음을 적들에게 알리지 말라.")
        assertThat(result).contains("2 / 에디슨 / 천재는 99%의 노력과 1%의 영감이다.")
    }

    @Test
    @DisplayName("명언 삭제 테스트")
    fun t3() {
        val result = TestRunner.run(
            """
            등록
            나의 죽음을 적들에게 알리지 말라.
            충무공 이순신
            등록
            천재는 99%의 노력과 1%의 영감이다.
            에디슨
            삭제?id=1
            목록
        """
        )

        assertThat(result).contains("1번 명언을 삭제하였습니다.")
        assertThat(result).doesNotContain("1 / 충무공 이순신 / 나의 죽음을 적들에게 알리지 말라.")
        assertThat(result).contains("2 / 에디슨 / 천재는 99%의 노력과 1%의 영감이다.")
    }

    @Test
    @DisplayName("명언 수정 테스트")
    fun t4() {
        val result = TestRunner.run(
            """
            등록
            나의 죽음을 적들에게 알리지 말라.
            충무공 이순신
            수정?id=1
            나의 죽음을 적들에게 알리지 말라. 그리고 적들에게 나의 삶을 알리라.
            이순신 장군
            목록
        """
        )

        assertThat(result).contains("1번 명언을 수정하였습니다.")
        assertThat(result).doesNotContain("1 / 충무공 이순신 / 나의 죽음을 적들에게 알리지 말라.")
        assertThat(result).contains("1 / 이순신 장군 / 나의 죽음을 적들에게 알리지 말라. 그리고 적들에게 나의 삶을 알리라.")
    }

    @Test
    @DisplayName("빌드 테스트")
    fun t5() {
        val result = TestRunner.run(
            """
            등록
            나의 죽음을 적들에게 알리지 말라.
            충무공 이순신
            등록
            천재는 99%의 노력과 1%의 영감이다.
            에디슨
            빌드
        """
        )

        assertThat(result)
            .contains("data.json 파일의 내용이 갱신되었습니다.");
    }

    @Test
    @DisplayName("검색 테스트")
    fun t6() {
        val result = TestRunner.run(
            """
            등록
            현재를 사랑하라.
            작자미상
            등록
            과거에 집착하지 마라.
            작자미상
            목록?keywordType=content&keyword=과거
        """
        )

        assertThat(result)
            .contains("----------------------")
            .contains("검색타입 : content")
            .contains("검색어 : 과거")

        assertThat(result)
            .doesNotContain("1 / 작자미상 / 현재를 사랑하라.")
            .contains("2 / 작자미상 / 과거에 집착하지 마라.")
    }

    @Test
    @DisplayName("목록(페이징) - page=1")
    fun t7() {
        TestRunner.makeSampleData(10)

        val result = TestRunner.run(
            """
            목록
            """
        )

        assertThat(result)
            .contains("10 / 작자미상 / 명언 10")
            .contains("6 / 작자미상 / 명언 6")
            .doesNotContain("5 / 작자미상 / 명언 5")
            .doesNotContain("1 / 작자미상 / 명언 1")
            .contains("페이지 : [1] 2")
    }

    @Test
    @DisplayName("목록(페이징) - page=2")
    fun t8() {
        TestRunner.makeSampleData(10)

        val result = TestRunner.run(
            """
            목록?page=2
            """
        )

        assertThat(result)
            .doesNotContain("10 / 작자미상 / 명언 10")
            .doesNotContain("6 / 작자미상 / 명언 6")
            .contains("5 / 작자미상 / 명언 5")
            .contains("1 / 작자미상 / 명언 1")
            .contains("페이지 : 1 [2]")
    }

    @Test
    @DisplayName("목록?page=2&keywordType=content&keyword=명언")
    fun t9() {
        TestRunner.makeSampleData(10)

        val result = TestRunner.run(
            """
            목록?page=2&keywordType=content&keyword=명언
            """.trimIndent()
        )

        assertThat(result)
            .doesNotContain("10 / 작자미상 / 명언 10")
            .doesNotContain("6 / 작자미상 / 명언 6")
            .contains("5 / 작자미상 / 명언 5")
            .contains("1 / 작자미상 / 명언 1")
            .contains("페이지 : 1 [2]")
    }

    @Test
    @DisplayName("목록?page=1&keywordType=content&keyword=1")
    fun t10() {
        TestRunner.makeSampleData(10)

        val result = TestRunner.run(
            """
            목록?page=1&keywordType=content&keyword=1
            """.trimIndent()
        )

        assertThat(result)
            .contains("10 / 작자미상 / 명언 10")
            .doesNotContain("9 / 작자미상 / 명언 9")
            .doesNotContain("2 / 작자미상 / 명언 2")
            .contains("1 / 작자미상 / 명언 1")
            .contains("페이지 : [1]")
    }
}