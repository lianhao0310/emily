package com.alice.emily.data;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.alice.emily.autoconfigure.data.MongoExtAutoConfiguration;
import com.alice.emily.data.repo.mongo.Department;
import com.alice.emily.data.repo.mongo.DepartmentRepository;
import com.alice.emily.data.repo.mongo.Employee;
import com.alice.emily.data.repo.mongo.ReactiveDepartmentRepository;
import com.alice.emily.spatial.utils.GeometryFactories;
import com.vividsolutions.jts.geom.Coordinate;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lianhao on 2017/6/7.
 */
@ImportAutoConfiguration({
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class,
        MongoReactiveAutoConfiguration.class,
        MongoReactiveDataAutoConfiguration.class,
        MongoReactiveRepositoriesAutoConfiguration.class,
        MongoExtAutoConfiguration.class })
@ContextConfiguration(initializers = MongoRepositoryTest.Initializer.class)
public class MongoRepositoryTest extends AbstractDataTest {

    @ClassRule
    public static GenericContainer mongo = new FixedHostPortGenericContainer("mongo:3.4.6")
            .withExposedPorts(27017)
            .withEnv("TZ", "Asia/Shanghai")
            .withEnv("MONGO_INITDB_DATABASE", "test");

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@Nonnull ConfigurableApplicationContext context) {
            Integer port = mongo.getMappedPort(27017);
            TestPropertyValues.of("spring.data.mongodb.port=" + port).applyTo(context);
            TestPropertyValues.of("spring.data.mongodb.database=test").applyTo(context);
        }
    }

    @Autowired
    private MongoOperations operations;

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private ReactiveDepartmentRepository reactiveRepository;

    @Test
    public void testCommonCrud() {
        Department department = new Department();
        department.setId("110");
        department.setName("CIA");
        department.setPoint(GeometryFactories.WGS84().createPoint(new Coordinate(30, 90)));
        Department persisted = repository.save(department);

        assertThat(persisted)
                .isNotNull()
                .isEqualTo(department);
        assertTypeFieldNotExist();

        Department found = repository.findByName("CIA");
        assertThat(found)
                .isNotNull()
                .isEqualTo(department);

        Employee employee = new Employee();
        employee.setName("William");
        employee.setPhoneNum("**********");
        department.setEmployees(Lists.newArrayList(employee));

        persisted = repository.save(department);
        assertThat(persisted)
                .isNotNull()
                .isEqualTo(department);
        assertThat(persisted.getEmployees())
                .isNotNull()
                .hasSize(1)
                .contains(employee);
        assertThat(repository.count()).isEqualTo(1);

        assertThat(repository.getCollectionNames())
                .isNotNull()
                .hasSize(1)
                .contains("department");

        assertThat(repository.count(Query.query(Criteria.where("name").is("CIA"))))
                .isEqualTo(1);

        repository.delete(department);
        assertThat(repository.existsById("110")).isFalse();
    }

    @Test
    public void testReactive() {
        Department department = new Department();
        department.setId("111");
        department.setName("FBI");
        department.setPoint(GeometryFactories.WGS84().createPoint(new Coordinate(30, 90)));
        Department persisted = reactiveRepository.save(department).block();

        assertThat(persisted)
                .isNotNull()
                .isEqualTo(department);
        assertTypeFieldNotExist();

        Department found = reactiveRepository.findAll()
                .filter(d -> "FBI".equals(d.getName()))
                .blockFirst();
        assertThat(found)
                .isNotNull()
                .isEqualTo(department);

        Employee employee = new Employee();
        employee.setName("Jack");
        employee.setPhoneNum("**********");
        department.setEmployees(Lists.newArrayList(employee));
        persisted = reactiveRepository.save(department).block();
        assertThat(persisted)
                .isNotNull()
                .isEqualTo(department);
        assertThat(persisted.getEmployees())
                .isNotNull()
                .hasSize(1)
                .contains(employee);

        reactiveRepository.count()
                .doOnNext(c -> assertThat(c).isEqualTo(1))
                .block();

        reactiveRepository.delete(department).block();
        reactiveRepository.existsById("111")
                .doOnNext(b -> assertThat(b).isFalse())
                .block();
    }

    private <T> void assertTypeFieldNotExist() {
        MongoCollection<Document> collection = operations.getCollection("department");
        assertThat(collection).isNotNull();
        collection.find().forEach((Consumer<? super Document>) document -> {
            assertThat(document.containsKey(DefaultMongoTypeMapper.DEFAULT_TYPE_KEY))
                    .isFalse();
        });
    }
}
