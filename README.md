# Emily

## 1. 编码规范

> 参照[Google Java编码规范](https://segmentfault.com/a/1190000002761014)
> 参照[阿里巴巴Java开发手册](https://yq.aliyun.com/attachment/download/?id=1170)

Emily使用Checkstyle工具对编码风格进行检测, 不符合规范的代码会被终止构建过程:

```java
    if (aClass.isAssignableFrom(eClass)) {
        if (timeUp())                              // if语句跨行时需要添加花括号
            sendMail(ctx, throwable, group, title);
        break;
    }
```

```bash
[INFO] --- maven-checkstyle-plugin:2.17:check (verify-style) @ emily-core ---
[INFO] Starting audit...
[ERROR] D:\Source\emily\emily-core\src\main\java\com\alice&emily\emily\aop\AlertIfAspect.java:45: 'if' 结构必须使用大括号 '{}'。 [NeedBraces]
Audit done.
```

## 2. 开发环境

> * JDK 1.8+
> * Maven 3.3.9+

## 3. 框架选型

### LOG

* [log4j2](https://logging.apache.org/log4j/2.x/)
* [slf4J](https://www.slf4j.org/)

### JSON

* [Jackson](https://github.com/FasterXML/jackson/)

### HTTP

* RestTemplate
* [OkHttp](http://square.github.io/okhttp/)
* [Retrofit](http://square.github.io/retrofit/)
* [HttpRequest](https://github.com/kevinsawicki/http-request)

### WEB

* [Undertow](http://undertow.io/)

    Undertow 是一个采用 Java 开发的灵活的高性能 Web 服务器，提供包括阻塞和基于
    NIO 的非堵塞机制。Undertow 是红帽公司的开源产品，是 Wildfly 默认的 Web 服务器。

* [Resteasy](http://resteasy.jboss.org/)

    JAX-RS标准实现，用在WildFly应用服务器中, 其表现优于Jersey，查看其[性能评测](http://h2ex.com/371?utm_source=tuicool&utm_medium=referral)

### Template

* [Beetl](http://ibeetl.com/)
* [Velocity](http://velocity.apache.org/)
* [Thymeleaf](http://www.thymeleaf.org/)

### ORM

* [Hibernate JPA](http://hibernate.org/)
* [DbUtils](http://commons.apache.org/proper/commons-dbutils/)
* JdbcTemplate

### JTA

* [Narayana](http://www.narayana.io/)

    JBoss Transactions, 兼容 JTA 1.2 规范, 集成在WildFly中

### Cache

* [Infinispan](http://infinispan.org/)

    JBoss 开源数据网格平台， 支持本地和分布式两种模式

* [Caffeine](https://github.com/ben-manes/caffeine)

* Guava

### RPC

* [gRPC](http://www.grpc.io/)

### SSO

* [Keycloak](http://www.keycloak.org/)

## 4. 快速入手

### 建立新项目

* **Maven Archetype**

```bash
mvn archetype:generate
    -DgroupId=com.alice&emily                         # group
    -DartifactId=test                               # artifact
    -Dpackage=com.alice&emily.test                    # package
    -DarchetypeGroupId=com.alice.emily
    -DarchetypeArtifactId=emily-archetype
    -DarchetypeVersion=2.0-SNAPSHOT
```

```bash
mvn archetype:generate -DgroupId=com.alice&emily -DartifactId=test -Dpackage=com.alice&emily.test -DarchetypeGroupId=com.alice.emily -DarchetypeArtifactId=emily-archetype -DarchetypeVersion=2.0-SNAPSHOT
```

* 手动建立
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>emily-parent</artifactId>
        <groupId>com.alice.emily</groupId>
        <version>2.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.alice&emily.test</groupId>
    <artifactId>test-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <wrapper.daemon.id>${project.artifactId}</wrapper.daemon.id>
        <wrapper.main.class>com.alice&emily.test.EmilyApplication</wrapper.main.class>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-rest</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-jwt</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-metric</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>provided</scope>
            <optional>true</optional>
        </dependency>

        <!-- 内嵌数据库，正式使用时请删除 -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
        </dependency>
    </dependencies>

</project>
```

### Core

```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-core</artifactId>
        </dependency>
```
注： 引入其他模块会自动引入本模块

* **Template**

默认支持Beetl模板引擎，引入Web支持时，会自动配置BeetlSpringViewResolver
```bash
├─src
│  ├─main
│  │  └─resources
│  │      ├─META-INF
│  │      └─template    // beetl 模板文件
```

* **Mail**
```yml
emily:
  mail:
    enabled: true        # 是否启用
    server:              # 服务器配置
      ssl: true
      host: smtp.alice&emily.com
      username: nagrand@ipalmap.com
      password: ******
    groups:              # 邮件分组
      nagrand:
        from: nagrand@ipalmap.com
        to: [pin.liu@alice&emily.com,sifan.pan@alice&emily.com,guanyu.yue@alice&emily.com]
      pop:
        from: nagrand@ipalmap.com
        to: [kai.zhang@alice&emily.com,yang.qiu@alice&emily.com]
```
可使用**com.alice.emily.mail.MailSender**发送邮件

* **@AlertIf**

异常监控，注解在方法上，如果方法执行过程中抛出异常，会触发邮件通知。 group是邮件组
```java
    @AlertIf(value = Exception.class, group = "nagrand", title = "Sorry, just for fun!")
    public void exceptionMethod() throws Exception {
        throw new Exception();
    }
```

* **@Audit**

方法执行时间计量，在日志中输出方法执行耗费时间
```java
    @Audit("INFO")
    void longRunningTask(String mission) {
        System.out.println("Long Task " + mission + " begin...");
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        System.out.println("Long Task " + mission + " complete...");
    }
```

* **Jackson**

Emily提供JSONUtils工具类，其使用Spring管理的ObjectMapper序列化和反序列话Object
```bash
spring.jackson.default-property-inclusion=non_null
spring.jackson.deserialization.accept-float-as-int=false
spring.jackson.deserialization.fail-on-unknown-properties=false
spring.jackson.serialization.indent_output=true
spring.jackson.mapper.use-static-typing=false
```

* **Security**

Emily提供对SpringSecurity的扩展支持：
```bash
# 白名单 提供GET请求的无权限访问
security.white-list.patterns[0]=/v2/api-docs
security.white-list.patterns[1]=/swagger-resources/**

# 允许Option请求的无权限访问
security.permit-options=true

# 静态资源权限设置
security.static-resource.patterns=/*.html,/favicon.ico,/**/*.html,/**/*.css,/**/*.js
security.static-resource.authenticated=false
```

### Rest API
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-rest</artifactId>
        </dependency>
```
REST应用示例:
```java
@Component
@ApplicationPath("/")
public class RestApplication extends Application {
}
```

REST资源示例：
```java
@Component
@Path("/")
@Produces("text/plain")
public class TestResource {

    @Autowired
    private TestService service;

    @GET
    public String test() {
        System.out.println("TestService: " + service.hi());
        return "1";
    }

    @POST
    @Consumes("application/json")
    public void post(@QueryParam("name") String name, Person person) {
        System.out.println("request param: name=" + name);
        System.out.println("request body: " + person);
    }
}
```

* **CORS**
```bash
emily.resteasy.cors=true            # 启用CORS Filter
```

> 对于Http Option请求， emily默认提供了HttpOptionMethodFilter过滤器

```bash
spring.mvc.dispatch-options-request=false   # 关闭SpringMVC分发Options请求
```

* **Http Cache**
```bash
emily.resteasy.http-cache=true      # 启用Http Cache
```

* **File Upload**
```java
@Log4j2
@Component
@Path("fileUpload")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.TEXT_PLAIN)
public class FileUploadResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartFileParam("file") MultipartFile file) throws IOException {  // 可以使用SpringMVC对Multipart的支持
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        log.info("Received upload file with MediaType {}:\n {}", file.getContentType(), content);
        return Response.ok(content).header("Content-Type", file.getContentType()).build();
    }

}
```

### JWT
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-jwt</artifactId>
        </dependency>
```

```bash
emily.jwt.secret=5f2f8973-15df-45bf-be2d-dc1bcc587f0b    # 生成token使用的密钥
emily.jwt.auth-path=/auth                                # 认证 token URL
emily.jwt.refresh-path=/refresh                          # 刷新 token URL
emily.jwt.expiration=604800                              # token时效
emily.jwt.header=Authorization                           # 携带token信息的消息头

# 默认可以使用基于内存的用户凭证管理
emily.jwt.users[0].username=user
emily.jwt.users[0].password=password
emily.jwt.users[0].roles=USER,ADMIN
emily.jwt.users[0].enabled=true
```

> 可以提供自定义的 **PasswordEncoder** 和 **UserDetailService**

* 获取当前用户信息
```java
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getProtectedGreeting(@AuthenticationPrincipal UserDetails principal) {
        System.out.println("Principal: " + principal);
        return Response.ok().entity("Greetings from test protected method!").build();
    }
```

### JPA
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-jpa</artifactId>
        </dependency>
```

* **hibernate** 推荐配置(避免@Table注解name命名失效）:
```yml
spring:
  jpa:
    hibernate:
      naming:
        implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

* **druid** 支持
```yml
emily:
  druid:
    stat:
      enable: true          # 启动统计支持
      username: admin       # 用户名
      password: druid       # 密码
```

* **多数据源**
数据源Properties配置：
```bash
datasource.ocean.url=jdbc:postgresql://10.0.23.25:5432/ocean?currentSchema=ocean               # ocean
datasource.ocean.username=postgres
datasource.ocean.password=postgres
datasource.ocean.driver-class-name=org.postgresql.Driver
datasource.pop.url=jdbc:mysql://10.0.23.9/platform?autoReconnect=true&failOverReadOnly=false   # pop
datasource.pop.username=root
datasource.pop.password=
datasource.pop.driver-class-name=com.mysql.jdbc.Driver
datasource.nagrand.url=jdbc:postgresql://10.0.23.25:5432/nagrand?currentSchema=road_net        # nagrand
datasource.nagrand.username=postgres
datasource.nagrand.password=postgres
datasource.nagrand.driver-class-name=org.postgresql.Driver
```

数据源JavaConfig：
```java
@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    @ConfigurationProperties("datasource.ocean")
    public DataSourceProperties oceanDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("datasource.nagrand")
    public DataSourceProperties nagrandDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("datasource.pop")
    public DataSourceProperties popDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @Qualifier("oceanDataSource")
    public DataSource oceanDataSource() {
        return oceanDataSourceProperties().initializeDataSourceBuilder().type(DruidDataSource.class).build();
    }

    @Bean
    @Qualifier("nagrandDataSource")
    public DataSource nagrandDataSource() {
        return nagrandDataSourceProperties().initializeDataSourceBuilder().type(DruidDataSource.class).build();
    }

    @Bean
    @Qualifier("popDataSource")
    public DataSource popDataSource() {
        return popDataSourceProperties().initializeDataSourceBuilder().type(DruidDataSource.class).build();
    }
}
```

### JTA
```xml
<dependency>
    <groupId>com.alice.emily</groupId>
    <artifactId>emily-jta</artifactId>
</dependency>
```

```yml
spring:
  transaction:
    rollback-on-commit-failure: true
  jta:
    enabled: true
    transaction-manager-id: 1           # jta使用的事务管理器ID
    log-dir: ./target/tx-object-store
    narayana:
      transaction-manager-id: 1         # 与上述配置对应
      default-timeout: 60
      one-phase-commit: true
      periodic-recovery-period: 120
      recovery-backoff-period: 10
      recovery-db-user:                 # recovery manager 数据库用户名
      recovery-db-pass:                 # recovery manager 数据库用户密码
```

### Cache
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-cache</artifactId>
        </dependency>
```

* **支持JCache标准**:
```java
public class JCacheTestDemo {

    @CacheResult(cacheName = "test")
    public String longTimeSearch(String input) {
        System.out.println("Start calculate for " + input);
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        return "Result: " + input;
    }

    @CacheRemove(cacheName = "test")
    public void deleteResult(String input) {
        System.out.println("Delete result for " + input);
    }

}
```

* **直接使用Guava**:
```java
    private Cache<String, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
```

* **直接使用Infinispan**：
```java
    @InfinispanCache("test") Cache<String, String> cache;
```

### RPC
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-grpc</artifactId>
        </dependency>
```

* 服务器端： 支持自动部署RPC服务， 可配置端口号，设置拦截器（包括全局）
* 客户端：发现并编译处于指定目录的proto文件， 可直接在IOC环境中使用
```yml
emily:
  grpc:
    clients:
      pop:     # 客户端分组，支持多个分组同时启用
        host: 10.0.23.23
        port: 38628
    server:
      port: 50000
```

```bash
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─alice&emily
│  │  │          └─nagrand
│  │  │              └─grpc
│  │  │                  └─impl
│  │  └─resources
│  │      ├─META-INF
│  │      └─protos      // proto 文件位置
```

```java
    @GRpcClient("pop")
    SceneRpcServiceGrpc.SceneRpcServiceBlockingStub stub1;
```

### GEO
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-geo</artifactId>
        </dependency>
```

基于H2gis封装大量常用Geo函数：
```Java
public void testTransform() {
    Geometry geometry = GeoFunctions.geomFromText("POINT(38758575.2894 74764918.5831)", GeoCRSCode.WGS84_PSEUDO_MERCATOR);
    Geometry transform = GeoFunctions.transform(geometry, GeoCRSCode.WGS84);
    System.out.println(GeoFunctions.asText(transform));
}
```

### CMD
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-cmd</artifactId>
        </dependency>
```

命令方法基于注解发现：
```java
@Commands(category = "greet", mode = DiscoverMode.ANNOTATED_METHOD)
public class GreetCommands {

    @Command
    private String hello(String cmd) {
        return Thread.currentThread().getName() + ": " + cmd;
    }

    @Command(category = "greeting")
    public String say(String word) {
        return "said " + word;
    }

    @Command
    public String cmd(CmdObject cmdObject) {
        return "cmdObject: " + cmdObject;
    }

    @Override
    public String stop(String msg) {
        return "Stop: " + msg;
    }
}
```
基于CMD模块实现的Console应用：
```bash
[Nagrand] >>> help
         0. admin     addAlias             ( index, alias )
         1. admin     createIndex          ( index )
         2. admin     deleteIndex          ( indexes )
         3. admin     listAlias            ( indexes )
         4. admin     listIndex
         5. admin     refresh              ( indexes )
         6. admin     removeAlias          ( index, alias )
         7. admin     swapAlias            ( alias, oldIndex, newIndex )
         8. emily  help                 ( category )
         9. emily  quit
[Nagrand] >>>

```

> 推荐日志输出格式：
```bash
logging.pattern.console=%clr{[%5p] %m%n%xwEx}
```

### Camel
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-camel</artifactId>
        </dependency>
```
Apache Camel是一个基于规则路由和处理的引擎，提供企业集成模式的Java对象的实现，
通过应用程序接口 或称为陈述式的Java领域特定语言(DSL)来配置路由和处理的规则。
其核心的思想就是从一个from源头得到数据,通过processor处理,再发到一个to目的的

* JMS用法：
```xml
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-sjms</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-artemis</artifactId>
        </dependency>
```

```java
@Component
public class CamelJmsConsumers {   // jms消费者

    @Consume(uri = "sjms:topic:upload?connectionFactory=#jmsConnectionFactory")
    public void onTopic1(String msg) {
        System.out.println("Received from topic: " + msg);
    }

    @Consume(uri = "sjms:queue:download?connectionFactory=#jmsConnectionFactory")
    public void onQueue2(String msg) {
        System.out.println("Received from queue: " + msg);
    }
}
```

```java
    @EndpointInject(uri = "sjms:topic:upload?connectionFactory=#jmsConnectionFactory")
    private ProducerTemplate jmsTopicProducer;

    @EndpointInject(uri = "sjms:queue:download?connectionFactory=#jmsConnectionFactory")
    private ProducerTemplate jmsQueueProducer;

    @Test
    public void testTopic() {      // jms生产者
        jmsTopicProducer.sendBody("B");
        jmsQueueProducer.sendBody("F");
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    }
```

```bash
2017-02-10 18:02:51.913  INFO 4684 --- [           main] o.a.c.s.SpringCamelContext               : Apache Camel 2.18.1 (CamelContext: camel-1) started in 0.596 seconds
2017-02-10 18:02:51.917  INFO 4684 --- [           main] c.p.e.c.CamelTest                        : Started CamelTest in 3.813 seconds (JVM running for 5.397)
Received from topic: B
Received from queue: F
```

* Zookeeper用法：
```xml
        <dependency>
            <groupId>org.apache.camel</groupId>
            <artifactId>camel-zookeeper</artifactId>
        </dependency>
```

```java
@Component
public class ZookeeperConsumer {    // zookeeper消费者

    @Consume(uri = "zookeeper://localhost:2181/test/data?repeat=true")
    public void onNode(@Header(ZooKeeperMessage.ZOOKEEPER_NODE) String node, @Body String msg) {
        System.out.println("Received from " + node + ": " + msg);
    }

}
```

```java
    @EndpointInject(uri = "zookeeper://localhost:2181/test/data?create=true&createMode=PERSISTENT")
    private ProducerTemplate producerTemplate;

    @Test
    public void testZookeeper() {    // zookeeper生产者
        producerTemplate.sendBody("A");
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        producerTemplate.sendBody("B");
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        producerTemplate.sendBody("C");
        Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
        producerTemplate.sendBody("D");
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    }
```

```bash
2017-02-16 16:36:00.907  WARN 14212 --- [ain-EventThread] o.a.c.c.z.ZooKeeperProducer              : Node '/test/data' did not exist, creating it...
Received from /test/data: A
Received from /test/data: B
Received from /test/data: C
Received from /test/data: D
```

**Zookeeper** 支持内嵌服务：
```yml
emily:
  zookeeper:
    embedded:
      enable: true
      port: 2181
```

### Keycloak
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-keycloak</artifactId>
        </dependency>
```

```bash
# keycloak基本配置
keycloak.realm=master
keycloak.realmKey=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqdbd/hdjTONLPRzyFaGCEXtj27DtUyNRot9pznnNeNWTnduDwIu9iUMctJzqtD0qt10oNl28YUXakUUk9jPUEvjsoiwRmPPzuIj86VLHFo+lQKMjqWWnrhPRuxEM/9jcolwG8ajGHfzKHsuSrhhWVdbXYdjhU/bccBEpMd+9gi5XB05lIkZOnWnn6a2gV05VvaDaA9FZvDv0RKJC1Hh717D9+g3TqAs9KwjyI7T+IaRQvN3JgF2Mto/L5wV1EVz+tF346XAG23zNzu8TabAH98HCqK7A3rSET2V+p9/eAp2XQZao1Kg1qUMuyG0+4ADju62qIUPOrQf9xrwR5hTdPwIDAQAB
keycloak.auth-server-url=http://10.0.23.9/auth
keycloak.ssl-required=none
keycloak.resource=palmap_open
keycloak.use-resource-role-mappings=true
keycloak.public-client=true
keycloak.cors=true

# keycloak安全配置
keycloak.security-constraints[0].securityCollections[0].name=User
keycloak.security-constraints[0].securityCollections[0].authRoles[0]=role_user
keycloak.security-constraints[0].securityCollections[0].patterns[0]=/protected/*

# keyclaok admin 管理配置
emily.keycloak.admin.server-url=${keycloak.auth-server-url}
emily.keycloak.admin.realm=${keycloak.realm}
emily.keycloak.admin.username=pin.liu@alice&emily.com
emily.keycloak.admin.password=*****
emily.keycloak.admin.client-id=${keycloak.resource}
```

# 5. 打包部署

### JSW (长期运行）

* src/main/resources/META-INF 目录下添加**emily-wrapper.txt**标记文件
* pom中添加必要属性
```xml
    <properties>
        <wrapper.daemon.id>${project.artifactId}</wrapper.daemon.id>
        <wrapper.main.class>com.alice&emily.nagrand.ws.Nagrand</wrapper.main.class>
    </properties>
```

生成目录结构：
```bash
├─nagrand-ws-2017-02-05  // daemon.id + yyyy-MM-dd
│  ├─bin                 // 启动脚本
│  ├─conf                // 配置
│  ├─lib                 // 依赖库
│  └─logs                // 日志
```

CentOS设置开机启动:
```bash
ln -s /opt/nagrand-ws/bin/nagrand-ws /etc/init.d/
chkconfig nagrand-ws on
```

### Uber (简易部署）

* src/main/resources/META-INF 目录下添加**emily-uber.txt**标记文件
* pom中添加必要属性
```xml
    <properties>
        <uber.main.class>com.alice&emily.nagrand.console.NagrandConsole</uber.main.class>
    </properties>
```

CentOS后台运行:
```bash
nohup java -jar nagrand-console.jar > nc.log &
```

## 6. 项目历史

* emily 1.x
        基于[SpringBoot](http://projects.spring.io/spring-boot/)构建，采用多项Jboss开源产品