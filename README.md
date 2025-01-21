# future-cloud 微服务

## 一 技术栈

- 主框架：`Spring Cloud` + `Spring Boot` + `Spring Framework`
- 持久层框架：`MyBatis-Plus`
- 数据库连接池：`Alibaba Druid`
- 服务网关：`Spring Cloud Gateway`
- 服务注册&发现和配置中心: `Alibaba Nacos`
- 服务监控：`Spring Boot Admin`
- 服务消费（调用）：`Spring Cloud OpenFeign`、`Apache Dubbo`
- 负载均衡：`Spring Cloud Loadbalancer`
- 服务熔断&降级&限流：`Alibaba Sentinel`
- 分布式事务：`Alibaba Seata`
- 权限认证框架：`Sa-Token`+`JWT`
- 代码生成器：`MyBatis-Plus-Generator`
- 缓存数据库：`Redis`
- Api文档生成工具：`Knife4j`
- 项目构建：`Maven`

## 二 环境要求

### 2.1 开发环境

| 类目 | 版本或建议                                                                                                                                                           |
| --- |-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 电脑配置 | 建议开发电脑I5及以上CPU，内存32G及以上                                                                                                                                         |
| 操作系统 | Windows 10/11，MacOS                                                                                                                                             |
| JDK | 建议使用 `1.8.0_281` 及以上版本                                                                |
| Maven | 3.6.3及以上版本                                                                                                                                                      |
| Redis | 3.2.100(Windows)/4.0.x+(Linux,Mac)                                                                                                                              |
| 数据库 | 兼容 `MySQL 5.7.x/8.0.x`(默认)、`SQLServer 2012+`、`Oracle 11g`、`PostgreSQL 12+`、`达梦数据库(DM8)`、`人大金仓数据库(KingbaseES_V8R6)`                                              |
| 前端开发 | `Node.js v16.15.0`(某些情况下可能需要安装 Python3)及以上版本;<br/>`Yarn v1.22.x` 版本;<br/> `pnpm v8.10` 及以上版本;<br/>浏览器推荐使用 `Chrome 90` 及以上版本;<br/>`Visual Studio Code`(简称VSCode) |
| Nacos | v2.2.3， 服务注册&发现和配置中心                                                                                                                                            |
