package com.jjm.uavimagesearchmcpserver.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    void searchImage() {
        // 测试搜索无人机图片
        String result = imageSearchTool.searchImage("drone");
        Assertions.assertNotNull(result);
    }
}
