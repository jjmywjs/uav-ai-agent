package com.jjm.uavaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class UavAppTest {

    @Resource
    private UavApp uavApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是客户甲";
        String answer = uavApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "请问中航电测（西安）的无人机货运投送系统有哪些配套的无人机产品";
        answer = uavApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我要问的公司叫什么来着？刚跟你说过，帮我回忆一下";
        answer = uavApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我要从上海送一个 45 公斤的医疗设备到杭州，里面含有锂电池（120Wh），怎么办？";
        String answer =  uavApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("推荐几个吨级标准载荷的货运无人机平台");

        // 测试网页抓取：产品分析
        testMessage("公司有大型货物需要使用无人机运输，看看中航电测（西安）（zemic.com.cn）有哪些适配的货运无人机？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张货运无人机图片为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的货运档案为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘无人机货运计划’PDF，包含费用、流程和注意事项");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = uavApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }


    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        // 测试地图 MCP
        //String message = "我的货物在西安雁塔区，请帮我找到 10 公里内合适的货运地点";
        //String answer =  uavApp.doChatWithMcp(message, chatId);
        // 测试图片搜索 MCP
        String message = "帮我搜索一些货运无人机的图片";
        String answer =  uavApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }


}
