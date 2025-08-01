package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RqTest {
    @Test
    @DisplayName("수정?id=1.getActionName()")
    void t1() {
        Rq rq = new Rq("수정?id=1");

        String actionName = rq.getActionName();

        assertThat(actionName).isEqualTo("수정");
    }

    @Test
    @DisplayName("삭제?id=1.getActionName()")
    void t2() {
        Rq rq = new Rq("삭제?id=1");

        String actionName = rq.getActionName();

        assertThat(actionName).isEqualTo("삭제");
    }

    @Test
    @DisplayName("삭제?id=1.getParamAsInt()")
    void t3() {
        Rq rq = new Rq("삭제?id=1");

        int id = rq.getParamAsInt("id", -1);

        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제?id=실패.getParamAsInt()")
    void t4() {
        Rq rq = new Rq("삭제?id=실패");

        int id = rq.getParamAsInt("id", -1);

        assertThat(id).isEqualTo(-1);
    }

    @Test
    @DisplayName("등록?이름=홍길동.getParam()")
    void t5() {
        Rq rq = new Rq("등록?이름=홍길동");

        String name = rq.getParam("이름", "");

        assertThat(name).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("등록?이름=홍길동&고향=남원.getParam()")
    void t6() {
        Rq rq = new Rq("등록?이름=홍길동&고향=남원");

        String name = rq.getParam("이름", "");
        String hometown = rq.getParam("고향", "");

        assertThat(name).isEqualTo("홍길동");
        assertThat(hometown).isEqualTo("남원");
    }

    @Test
    @DisplayName("등록?이름 =&고향 =.getParam()")
    void t7() {
        Rq rq = new Rq("등록?이름 = 홍길동 &고향 = 남원");

        String name = rq.getParam("이름", "");
        String hometown = rq.getParam("고향", "");

        assertThat(name).isEqualTo("홍길동");
        assertThat(hometown).isEqualTo("남원");
    }

    @Test
    @DisplayName("\"등록?성별=\" : rq.getParam(\"성별\", \"모름\")")
    void t8() {
        Rq rq = new Rq("등록?성별=");

        String paramValue = rq.getParam("성별", "모름");

        assertThat(paramValue).isEqualTo("모름");
    }

    @Test
    @DisplayName("\"등록?성별\" : rq.getParam(\"성별\", \"모름\")")
    void t9() {
        Rq rq = new Rq("등록?성별");

        String paramValue = rq.getParam("성별", "모름");

        assertThat(paramValue).isEqualTo("모름");
    }

    @Test
    @DisplayName("\"등록\" : rq.getParam(\"성별\", \"모름\")")
    void t10() {
        Rq rq = new Rq("등록");

        String paramValue = rq.getParam("성별", "모름");

        assertThat(paramValue).isEqualTo("모름");
    }
}
