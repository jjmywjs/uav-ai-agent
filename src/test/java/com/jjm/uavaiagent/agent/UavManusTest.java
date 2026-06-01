package com.jjm.uavaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UavManusTest {

    @Resource
    private UavManus uavManus;

    @Test
    void run() {
        String userPrompt = """  
                我要从上海送一个 45 公斤的医疗设备到杭州，里面含有锂电池（120Wh），
                请制定一份详细的货运方案，计算费用和时间，
                并以 PDF 格式输出""";
        String answer = uavManus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}
