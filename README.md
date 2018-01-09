# Emily

## 1. 编码规范

> * [Google Java编码规范](https://segmentfault.com/a/1190000002761014)
> * [阿里巴巴Java开发手册](https://yq.aliyun.com/attachment/download/?id=1170)

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
[ERROR] D:\Source\emily\emily-core\src\main\java\com.alice.emily\aop\AlertIfAspect.java:45: 'if' 结构必须使用大括号 '{}'。 [NeedBraces]
Audit done.
```

## 2. 开发环境

> * JDK 1.8+
> * Maven 3.3.9+

## 3. 框架选型

### Logging

* [log4j2](https://logging.apache.org/log4j/2.x/)
* [slf4J](https://www.slf4j.org/)

> 可使用**lombok.extern.log4j.Log4j2**自动生成日志属性字段

### Json

* [Jackson](https://github.com/FasterXML/jackson/)

> **com.alice.emily.utils.JSON**封装了常用的json操作

### Http

* [RestTemplate](http://www.baeldung.com/rest-template)
* [HttpRequest](https://github.com/kevinsawicki/http-request)

> **com.alice.emily.utils.HTTP**封装了对HttpRequest的引用

### Web

* [Undertow](http://undertow.io/)
* [SpringMVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html)
* [Resteasy](http://resteasy.jboss.org/)

> 推荐使用**SpringMVC**

### Validator

* [Hibernate Validator](http://hibernate.org/validator/)

### Template

* [Beetl](http://ibeetl.com/)
* [Freemarker](http://freemarker.org/)
* [Thymeleaf](http://www.thymeleaf.org/)

### Database

* [sql2o](http://www.sql2o.org/)
* [Hibernate JPA](http://hibernate.org/)
* [BeetlSQL](http://ibeetl.com/)
* [JdbcTemplate](https://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html)

### JTA

* [Narayana](http://www.narayana.io/)

### Cache

* [Infinispan](http://infinispan.org/)
* [Caffeine](https://github.com/ben-manes/caffeine)

### RPC

* [gRPC](http://www.grpc.io/)

### NoSQL

* [Elasticsearch](https://www.elastic.co/products/elasticsearch)
* [MongoDB](https://docs.mongodb.com/)

### Utils

* [Guava](https://github.com/google/guava)
* [commons-lang3](https://commons.apache.org/proper/commons-lang/)
* [commons-io](https://commons.apache.org/proper/commons-io/)
* [jool](https://github.com/jOOQ/jOOL)
* [jodd](http://jodd.org/)
* [mapstruct](http://mapstruct.org/)
* [streamex](https://github.com/amaembo/streamex)

## 4. 快速入手

### 建立新项目

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>emily-parent</artifactId>
        <groupId>com.alice.emily</groupId>
        <version>3.2</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.alice.test</groupId>
    <artifactId>test-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <wrapper.daemon.id>${project.artifactId}</wrapper.daemon.id>
        <wrapper.main.class>com.alice.test.EmilyApplication</wrapper.main.class>
    </properties>
    
    <!--Add repositories-->
    <repositories>
    	<repository>
    		<id>haoch-maven-snapshot-repository</id>
    		<name>haoch-maven-snapshot-repository</name>
    		<url>https://raw.github.com/lianhao0310/maven/snapshot/</url>
    	</repository>
    	<repository>
    		<id>haoch-maven-release-repository</id>
    		<name>haoch-maven-release-repository</name>
    		<url>https://raw.github.com/lianhao0310/maven/release/</url>
    	</repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-metrics</artifactId>
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

    <repositories>        
        <repository>
            <id>central</id>
            <url>http://repo.maven.apache.org/maven2</url>
        </repository>
    </repositories>

    <pluginRepositories>       
        <pluginRepository>
            <id>central</id>
            <url>http://repo.maven.apache.org/maven2</url>
        </pluginRepository>
    </pluginRepositories>
</project>
```

### Core

```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-core</artifactId>
        </dependency>
```
> 注： 引入其他模块会自动引入本模块

* **@AlertIf**

异常监控，注解在方法上，如果方法执行过程中抛出异常，会触发Spring事件通知
```java
    @AlertIf(value = Exception.class, type = "nagrand", desc = "Sorry, just for fun!")
    public void exceptionMethod() throws Exception {
        throw new Exception();
    }
```

* **@Audit**

方法执行时间计量，在日志中输出方法执行耗费时间
```java
    @Audit("INFO")
    public void longRunningTask(String mission) {
        System.out.println("Long Task " + mission + " begin...");
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        System.out.println("Long Task " + mission + " complete...");
    }
```

* **Jackson**

Emily提供JSON工具类，其使用Spring管理的ObjectMapper序列化和反序列化配置:
> **com.alice.emily.utils.JSON**

```properties
spring.jackson.default-property-inclusion=non_null
spring.jackson.deserialization.accept-float-as-int=true
spring.jackson.deserialization.fail-on-unknown-properties=false
spring.jackson.serialization.indent_output=true
spring.jackson.mapper.use-static-typing=false
```

* **Http**

Emily提供HTTP工具类，提供流式操作：
> **com.alice.emily.utils.HTTP**

```java
    HttpRequest httpRequest = HTTP.post(RESOURCE_ADMIN)
            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .header(HttpHeaders.AUTHORIZATION, getToken())
            .form("greet", "I will be accepted");

    assertThat(httpRequest.code()).isEqualTo(HttpResponseCodes.SC_OK);
    assertThat(httpRequest.body()).isEqualTo("Greetings from constraint admin post resource method!");
```

* **Web**

Emily提供了对文件断点下载的支持：
> **com.alice.emily.web.multipart.MultipartFileSender**

```java
    @GetMapping("/file")
    public void getMapFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = service.getFile();
        if (file != null && file.exists()) {
            MultipartFileSender.fromFile(file)
                    .with(request)
                    .with(response)
                    .serveResource();
        }
    }
```

> 如果开启了ShallowEtag支持，需要排除文件下载的路径

```properties
emily.web.shallow-etag.enabled=true                  # 开启ShallowEtag支持
emily.web.shallow-etag.write-weak-etag=true
emily.web.shallow-etag.exclude-paths=/file           # 排除/file
```

* **Cache**

推荐使用 **[JSR107(JCache)](https://github.com/jsr107/jsr107spec)**，默认为 Caffeine JCacheProvider

```properties
spring.cache.type=jcache
spring.cache.jcache.provider=com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider
```

> JCache 不能动态创建Cache， 必须在配置文件中显示创建（application.properties、infinispan.xml...）
```properties
spring.cache.cache-names=test
```

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

> 可使用 **@ManagedCache** 注解获取对应的cache实例：
```java
    @ManagedCache("test")
    private Cache cache;
```

> 也可以直接使用 **Caffeine**
```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=10_000,expireAfterWrite=3m
```

* **Spatial**

> 封装大量常用Spatial函数：
```Java
public void testTransform() {
    Geometry geometry = ST_GeomFromText.toGeometry("POINT(38758575.2894 74764918.5831)", CRS.WGS84_PSEUDO_MERCATOR);
    Geometry transform = ST_Transform.transform(geometry, CRS.WGS84);
    System.out.println(ST_AsText.asText(transform));
}
```

### Restful
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-resteasy</artifactId>
        </dependency>
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
```properties
emily.resteasy.cors=true            # 启用CORS Filter
```

> 对于Http Option请求， emily默认提供了HttpOptionMethodFilter过滤器

```properties
spring.mvc.dispatch-options-request=false   # 关闭SpringMVC分发Options请求
```

* **Http Cache**
```properties
emily.resteasy.http-cache=true      # 启用Http Cache
```

* **File Upload**
```java
@Log4j2
@Component
@Path("fileUpload")
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class FileUploadResource {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@MultipartParam("file") MultipartFile file) throws IOException {  // 可以使用SpringMVC对Multipart的支持
        String statement = new String(file.getBytes(), StandardCharsets.UTF_8);
        log.info("Received upload file with MediaType {}:\n {}", file.getContentType(), statement);
        return Response.ok(statement).header("Content-Type", file.getContentType()).build();
    }

    @POST
    @Path("part")
    public Response uploadPart(@MultipartParam("file") Part part) throws IOException {
        String statement = IOUtils.toString(part.getInputStream(), StandardCharsets.UTF_8);
        log.info("Received upload file with MediaType {}:\n {}", part.getContentType(), statement);
        return Response.ok(statement).header("Content-Type", part.getContentType()).build();
    }
}
```

### JPA
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-jpa</artifactId>
        </dependency>
```

> 默认使用 **[HikariCP](http://brettwooldridge.github.io/HikariCP/)** 数据源

* **hibernate**
```properties
# JPA规范推荐命名策略
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

# Spring推荐命名策略
spring.jpa.hibernate.naming.implicit-strategy=org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
spring.jpa.hibernate.naming.physical-strategy=org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
```

* **多数据源**
> 数据源Properties配置：
```properties
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

> 数据源JavaConfig：
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
        return oceanDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @Qualifier("nagrandDataSource")
    public DataSource nagrandDataSource() {
        return nagrandDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @Qualifier("popDataSource")
    public DataSource popDataSource() {
        return popDataSourceProperties().initializeDataSourceBuilder().build();
    }
}
```

### JTA
```xml
<dependency>
    <groupId>com.alice.emily</groupId>
    <artifactId>emily-starter-narayana</artifactId>
</dependency>
```

```properties
spring.transaction.rollback-on-commit-failure=true
spring.jta.enabled=true
spring.jta.transaction-manager-id=1
spring.jta.log-dir=./target/tx-object-store
spring.jta.narayana.transaction-manager-id=1
spring.jta.narayana.default-timeout=60
spring.jta.narayana.one-phase-commit=true
spring.jta.narayana.periodic-recovery-period=120
spring.jta.narayana.recovery-backoff-period=10
spring.jta.narayana.recovery-db-user=               # recovery manager 数据库用户名
spring.jta.narayana.recovery-db-pass=               # recovery manager 数据库用户密码
```

### RPC
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-grpc</artifactId>
        </dependency>
```

* 服务器端： 支持自动部署RPC服务， 可配置端口号，设置拦截器（包括全局）
* 客户端：发现并编译处于指定目录的proto文件， 可直接在IOC环境中使用
```properties
# 客户端分组，支持多个分组同时启用
emily.grpc.clients.pop.host=10.0.23.23
emily.grpc.clients.pop.port=38628

# 服务监听端口
emily.server.port=50000
```

```java
    @GRpcClient("pop")
    SceneRpcServiceGrpc.SceneRpcServiceBlockingStub stub1;
```

### CMD
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-command</artifactId>
        </dependency>
```

* 命令方法基于注解发现：
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

* 基于CMD模块实现的Console应用：
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

### Curator | Zookeeper
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-curator</artifactId>
        </dependency>
```

```properties
emily.curator.connect-string=localhost:2181
```

> 实现了类似@JmsListener的注解驱动MDP
```java
    @CuratorCacheListener("${curator.test.path.node-cache}")
    public void onSceneNode(@Payload String data,
                            @Header("path") String path,
                            ChildData childData) {
        System.out.format("****** [%s]: %s", path, data);
        System.out.println();

        assertThat(childData)
                .isNotNull()
                .hasFieldOrPropertyWithValue("path", path)
                .hasFieldOrPropertyWithValue("path", "/data/scene")
                .hasFieldOrPropertyWithValue("data", data == null ? new byte[0] : data.getBytes());
    }
```

**Zookeeper** 支持内嵌服务：
```properties
emily.zookeeper.embedded.enable=true
emily.zookeeper.embedded.port=2181
```

### Elasticsearch
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-elasticsearch</artifactId>
        </dependency>
```

* Spring data elasticsearch 增强 Repository

> 可以使用增强的Repository接口，其暴露了大量ElasticsearchOperations的操作：

```java
@NoRepositoryBean
public interface ElasticsearchExtRepository<T, ID extends Serializable>
        extends ElasticsearchRepository<T, ID>, ElasticsearchOperationExecutor<T> {
}
```

其实现基类是:
 > **com.alice.emily.data.elasticsearch.repository.SimpleElasticsearchExtRepository**

### MongoDB
```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-mongodb</artifactId>
        </dependency>
```
* Spring data mongo 增强 Repository

> 可以使用增强的Repository接口，其暴露了大量MongoOperations的操作:

```java
@NoRepositoryBean
public interface MongoExtRepository<T, ID extends Serializable>
        extends MongoRepository<T, ID>, MongoOperationsExecutor<T> {
}

```
其实现基类是:
> **com.alice.emily.data.mongodb.repository.SimpleMongoExtRepository**
> **com.alice.emily.data.mongodb.repository.SimpleMongoQuerydslExtRepository**

# 5. 应用监控

```xml
        <dependency>
            <groupId>com.alice.emily</groupId>
            <artifactId>emily-starter-monitor</artifactId>
        </dependency>
```

* JMX
> Emily默认打开Spring的JMX监控

* Spring Boot Actuator

* **[JavaMelody](https://github.com/javamelody/javamelody)**

```properties
javamelody.enabled=true
# log http requests
javamelody.init-parameters.log=true
# to exclude images, css, fonts and js urls from the monitoring
javamelody.init-parameters.url-exclude-pattern=(/webjars/.*|/css/.*|/images/.*|/fonts/.*|/js/.*)
# to add basic auth
javamelody.init-parameters.authorized-users=emily:javamelody
```

> 监控界面 http://localhost:8080/monitoring

# 6. 构建生成

### gRPC

* src/main/resources 目录下存在 protos目录

```bash
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─alice
│  │  │          └─nagrand
│  │  │              └─grpc
│  │  │                  └─impl
│  │  └─resources
│  │      ├─META-INF
│  │      └─protos      // proto 文件位置
```

### QueryDSL
* src/main/resources/META-INF 目录下添加**emily-querydsl.marker**标记文件

# 7. 打包部署

### JSW

* src/main/resources/META-INF 目录下添加**emily-wrapper.marker**标记文件
* pom中添加必要属性
```xml
    <properties>
        <wrapper.daemon.id>${project.artifactId}</wrapper.daemon.id>
        <wrapper.main.class>com.alice.nagrand.ws.Nagrand</wrapper.main.class>
    </properties>
```

生成目录结构：
```bash
├─nagrand-ws-2017-02-05  // daemon.id + yyyy-MM-dd
│  ├─bin                 // 启动脚本
│  ├─conf                // 配置
│  ├─doc                 // 文档
│  ├─lib                 // 依赖库
│  └─logs                // 日志
```

CentOS设置开机启动:
```bash
ln -s /opt/nagrand-ws/bin/nagrand-ws /etc/init.d/
chkconfig nagrand-ws on
```

### Uber

* src/main/resources/META-INF 目录下添加**emily-uber.marker**标记文件
* pom中添加必要属性
```xml
    <properties>
        <uber.main.class>com.alice.nagrand.console.NagrandConsole</uber.main.class>
    </properties>
```

CentOS后台运行:
```bash
nohup java -jar nagrand-console.jar > nc.log &
```

### **Docker**(結合Uber)
- 添加Maven插件
```xml
    <plugin>
        <groupId>com.spotify</groupId>
        <artifactId>dockerfile-maven-plugin</artifactId>
        <executions>
            <execution>
                <id>default</id>
                <goals>
                    <goal>build</goal>
                </goals>
                <phase>install</phase>
            </execution>
        </executions>
        <configuration>
            <repository>${project.artifactId}</repository>
            <tag>${project.version}</tag>
        </configuration>
    </plugin>
```

- 编写Dockerfile
```bash
FROM openjdk:8u131-jdk-alpine

WORKDIR /nagrand

EXPOSE 58080
EXPOSE 50000

COPY target/nagrand-ws-2.1-SNAPSHOT.jar /nagrand/nagrand-ws.jar

ENTRYPOINT ["java","-jar","/nagrand/nagrand-ws.jar"]
```

```bash
cd Dockerfile所在目录
docker build -f Dockerfile -t nagrand-ws:2.1-SNAPSHOT .
docker save -o target\nagrand-ws.tar nagrand-ws:2.1-SNAPSHOT
```

## 6. 项目历史

* emily 1.x
    > * 基于[SpringBoot](http://projects.spring.io/spring-boot/)构建，采用多项Jboss开源产品

* emily 2.x
    > * 采用Spring Boot推荐的架构重新组织项目结构
    > * 引入Elasticsearch，MongoDB，并增强Repository
    > * 非WEB环境下模板服务
    > * 默认缓存使用Caffeine(JCache)
    > * 增加Docker生成Profile