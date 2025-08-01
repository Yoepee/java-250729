package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RqTest {
    @Test
    @DisplayName("수정?id=1.getActionName()")
    void t1() {
        Rq rq = new Rq("수정?id=1");

        String actionName = rq.getActionName();

        assertEquals("수정", actionName);
    }

    @Test
    @DisplayName("삭제?id=1.getActionName()")
    void t2() {
        Rq rq = new Rq("삭제?id=1");

        String actionName = rq.getActionName();

        assertEquals("삭제", actionName);
    }

    @Test
    @DisplayName("삭제?id=1.getParamAsInt()")
    void t3() {
        Rq rq = new Rq("삭제?id=1");

        int id = rq.getParamAsInt("id", -1);

        assertEquals(1, id);
    }

    @Test
    @DisplayName("삭제?id=실패.getParamAsInt()")
    void t4() {
        Rq rq = new Rq("삭제?id=실패");

        int id = rq.getParamAsInt("id", -1);

        assertEquals(-1, id);
    }

    @Test
    @DisplayName("등록?이름=홍길동.getParam()")
    void t5() {
        Rq rq = new Rq("등록?이름=홍길동");

        String name = rq.getParam("이름", "");

        assertEquals("홍길동", name);
    }

    @Test
    @DisplayName("등록?이름=홍길동&고향=남원.getParam()")
    void t6() {
        Rq rq = new Rq("등록?이름=홍길동&고향=남원");

        String name = rq.getParam("이름", "");
        String hometown = rq.getParam("고향", "");

        assertEquals("홍길동", name);
        assertEquals("남원", hometown);
    }

    @Test
    @DisplayName("등록?이름 =&고향 =.getParam()")
    void t7() {
        Rq rq = new Rq("등록?이름 = 홍길동 &고향 = 남원");

        String name = rq.getParam("이름", "");
        String hometown = rq.getParam("고향", "");

        assertEquals("홍길동", name);
        assertEquals("남원", hometown);
    }

    @Test
    @DisplayName("\"등록?성별=\" : rq.getParam(\"성별\", \"모름\")")
    void t8() {
        Rq rq = new Rq("등록?성별=");

        String paramValue = rq.getParam("성별", "모름");

        assertEquals("모름", paramValue);
    }

    @Test
    @DisplayName("\"등록?성별\" : rq.getParam(\"성별\", \"모름\")")
    void t9() {
        Rq rq = new Rq("등록?성별");

        String paramValue = rq.getParam("성별", "모름");

        assertEquals("모름", paramValue);
    }

    @Test
    @DisplayName("\"등록\" : rq.getParam(\"성별\", \"모름\")")
    void t10() {
        Rq rq = new Rq("등록");

        String paramValue = rq.getParam("성별", "모름");

        assertEquals("모름", paramValue);
    }
}
