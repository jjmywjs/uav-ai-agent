# 航空货运无人机 AI 智能体 SkyBot

项目基于 Spring Boot 3 + Java 21 + Spring AI，用于与航空货运无人机 AI 智能体系统进行交互。

## 项目描述

SkyBot 是一个集成多个 AI 智能体的应用平台，包括：
- **航空货运无人机智能专家**：针对无人机应用优化，可完成精准报价与运力匹配、无人机运行法规讲解、异常情况应急方案生成等任务，支持多轮对话、对话记忆持久化、RAG 知识库检索、工具调用、MCP 服务调用。
- **ReAct 模式自主规划智能体应用**：可以根据用户需求，自主推理和行动，直到完成目标。
- 可供调用的工具：联网搜索、网页抓取、资源下载、PDF 生成、文件操作、终端操作等。
<img width="1919" height="917" alt="图片1" src="https://github.com/user-attachments/assets/d2c45161-c43e-48ce-a1c3-400862d23629" />

<img width="1919" height="919" alt="图片2" src="https://github.com/user-attachments/assets/2baf5bde-33a3-40d9-ad27-9d84b02eec2f" />

## 技术栈

- ✅ Java 21 + Spring Boot 3 框架
- ✅ Spring AI + LangChain4j
- ✅ RAG 知识库
- ✅ PGvector 向量数据库
- ✅ Tool Calling 工具调用
- ✅ MCP 模型上下文协议
- ✅ ReAct Agent 智能体构建
- ✅ Serverless 计算服务
- ✅ AI 大模型开发平台百炼
- ✅ Ollama 大模型部署
- ✅ SSE 异步推送
- ✅ 第三方接口：如 SearchAPI / Pexels API
- ✅ 工具库如：Kryo 高性能序列化 + Jsoup 网页抓取 + iText PDF 生成 + Knife4j 接口文档
- ✅ GitHub Copilot AI 生成前端（Vue 3、Vue Router 4、Axios、Vite、UUID）
