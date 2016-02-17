#common-platform

---

这是一个类似于Appfuse,Raipd Frameworks的Java快速开发框架，它整合了Web开发常用技术以平台化的形式提供如下方案：

1. 遵循MVC分层架构设计，并对J2EE MVC分层架构中流行框架（SpringMVC/SpringData/Hibernate/Ibatis/Datanucleus）进行了封装并提供统一的编程接口，使开发人员无需关注各个框架的差异;
2. 提供了ActiveMQ/SecurityManager/Email/ScheduleTask/SolrClient/Apache poi/Apache commons/RedisClient/MongoDB/CommonUtils等常用工具类。

--- 

### **子模块**

---

| 模块名称         | 描述            |   
| ------           | --------------------------------------------- |  
| commons          | 公共资源库，系统基础架构提供常用适配器以及工具类。|
| hibernate-api    | 基于Hibernate DAO数据访问接口。|
| ibatis-api       | 基于Ibatis/Mybatis DAO数据访问接口。|
| spring-data-api  | 基于Spring Data的数据访问接口。|
| datanucleus-jdo  | 基于DataNucleus JDO的数据访问接口。|
| datanucleus-jpi  | 基于DataNucleus JPA的数据访问接口。|
| mongo-driver-api | 基于Mongo Driver的数据访问接口。|