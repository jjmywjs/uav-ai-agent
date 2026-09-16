# 低空物流多智能体系统

项目基于 Spring AI + LangGraph4j，是一套面向城市低空物流（无人机货运）运营的多智能体协同系统，业务包括需求理解、方案推荐、订单确认、空域申报、调度执行、物流跟踪、异常处置等。

## 系统架构

低空智运生态架构包括交互层（货主服务与交易 Agent、运行协同与播报 Agent、飞行影像智能切片 Agent）、资产层（数据中台、多模态素材中心、绩效与安全考核系统）、模型层、运行时层、共享底座，以及确定性安全面（横切全部层级）。
<br/>
<img width="1189" height="666" alt="image" src="https://github.com/user-attachments/assets/966591a1-fa6d-4c49-884b-8df29a83b6d0" />
<br/>
- **Supervisor 路由层**：意图分类、槽位抽取、复杂度评估（L0-L3）、风险预检、路由决策。
- **Orchestrator 编排层**：任务分解为 DAG、状态机推进、检查点与恢复、人机闸口调度、Saga 补偿。
- **Agent 执行层**：7 个领域 Agent 承担具体推理与工具编排。

| Agent | 职责 |
|---|---|
| Intake | 需求理解、槽位抽取、多模态交叉核验 |
| Solution | 方案生成、报价、时效评估 |
| Order | 订单核验、闸口一签署 |
| Airspace | 空域申报、批复回收、有效期管理 |
| Dispatch | 运力匹配、放飞检查单、起飞确认 |
| Tracking | 遥测订阅、在途监控、ETA 预测 |
| Exception | 异常识别、预案生成、升级处置 |
- **核心架构决策（硬约束）**：Agent 之间不直接通信、空域数据按「分钟级 + 缓存」设计、L3 场景不允许纯自动放行。
- **三阶段人机协作**
   - 闸口一 · 硬闸口：货主签署方案与报价
   - 闸口二 · 软闸口：证据齐备即可放行，人可介入
   - 闸口三 · 硬升级：异常处置需值班长选定预案并承担决策责任
- 可供调用的工具：气象查询、空域状态、空域报备、运力匹配、订单创建、遥测订阅、合规校验、知识检索等。
- 三层护栏：① 超时重试  →  ② 结果过滤  →  ③ 异常降级

## 场景展示

- **货主**：通过语音、文字、图片等方式说明需求，系统提供优选方案和快速报价，保障货物安全可追溯。
<img width="1919" height="919" alt="image" src="https://github.com/user-attachments/assets/776e3658-2b1e-448d-8433-c69d87395a96" />
<br/>
<br/>

- **OCC 运行值班长**：掌握全局态势，可对异常进行快速定位，并获得一定的处置决策支持。
<img width="1919" height="919" alt="image" src="https://github.com/user-attachments/assets/f7387b4d-6149-42ce-b685-653e5b79e1c6" />
<br/>
<br/>

- **机巢/驿站运营**：装载检查单，进行放飞条件确认。
<img width="1919" height="919" alt="image" src="https://github.com/user-attachments/assets/89e1e863-8cc8-4095-b50c-bff318c622d1" />
<br/>
<br/>
<img width="1919" height="919" alt="image" src="https://github.com/user-attachments/assets/db281337-aa46-4bd1-83c3-9960f9a4d8a2" />
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
