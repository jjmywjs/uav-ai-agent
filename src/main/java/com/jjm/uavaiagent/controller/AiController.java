package com.jjm.uavaiagent.controller;

import com.jjm.uavaiagent.agent.UavManus;
import com.jjm.uavaiagent.app.UavApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private UavApp uavApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @GetMapping("/uav_app/chat/sync")
    public String doChatWithUavAppSync(String message, String chatId) {
        return uavApp.doChat(message, chatId);
    }

    @GetMapping(value = "/uav_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithUavAppSSE(String message, String chatId) {
        return uavApp.doChatByStream(message, chatId);
    }

    /*
    @GetMapping(value = "/uav_app/chat/sse")
    public Flux<ServerSentEvent<String>> doChatWithUavAppSSE(String message, String chatId) {
        return uavApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }
     */

    @GetMapping("/uav_app/chat/sse/emitter")
    public SseEmitter doChatWithUavAppSseEmitter(String message, String chatId) {
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter emitter = new SseEmitter(180000L); // 3分钟超时
        // 获取 Flux 数据流并直接订阅
        uavApp.doChatByStream(message, chatId)
                .subscribe(
                        // 处理每条消息
                        chunk -> {
                            try {
                                emitter.send(chunk);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        emitter::completeWithError,
                        // 处理完成
                        emitter::complete
                );
        // 返回emitter
        return emitter;
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        UavManus uavManus = new UavManus(allTools, dashscopeChatModel);
        return uavManus.runStream(message);
    }


}

