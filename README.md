# 低空物流多智能体系统

项目基于 Spring AI + LangGraph4j，是一套面向城市低空物流（无人机货运）的多智能体协同系统。

## 项目描述

SkyBot 是一个集成多个 AI 智能体的应用平台，包括：

- **航空货运无人机智能专家**：
- **领域 Agent**：可以根据用户需求，自主推理和行动，直到完成目标。
- 可供调用的工具：气象查询、空域状态、空域报备、运力匹配、订单创建、遥测订阅、合规校验、知识检索等。
<br/>
<img width="1189" height="666" alt="image" src="https://github.com/user-attachments/assets/966591a1-fa6d-4c49-884b-8df29a83b6d0" />
<br/>

## 技术栈

- ✅ Java 21 + Spring Boot 3 + Spring AI 框架
- ✅ 编排引擎：LangGraph4j + Temporal
- ✅ MCP 传输：stdio（边缘）+ Streamable HTTP（云侧）
- ✅ 模型层：模型网关 + 快慢双模型 + 规则引擎
- ✅ 检索：PGvector（向量） + Elasticsearch（BM25） + Neo4j（GraphRAG）
- ✅ 状态与消息：PostgreSQL + Redis + Kafka
- ✅ 可观测：OpenTelemetry + Langfuse
- ✅ 部署：K8s（云侧） + 机巢边缘节点（轻量推理）
- ✅ 前端：货主端 Web / IM 机器人；OCC 调度大屏 + 审核工作台
- ✅ 工具库如：Kryo 高性能序列化 + Jsoup 网页抓取 + iText PDF 生成 + Knife4j 接口文档
