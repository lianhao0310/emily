package com.alice.emily.data;

import com.google.common.collect.Lists;
import com.alice.emily.autoconfigure.data.ElasticsearchExtAutoConfiguration;
import com.alice.emily.data.repo.elasticsearch.Article;
import com.alice.emily.data.repo.elasticsearch.ArticleRepository;
import com.alice.emily.data.repo.elasticsearch.Comment;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import javax.annotation.Nonnull;

/**
 * Created by lianhao on 2017/6/8.
 */
@ImportAutoConfiguration({
        ElasticsearchAutoConfiguration.class,
        ElasticsearchDataAutoConfiguration.class,
        ElasticsearchRepositoriesAutoConfiguration.class,
        ElasticsearchExtAutoConfiguration.class })
@ContextConfiguration(initializers = ElasticsearchRepositoryTest.Initializer.class)
public class ElasticsearchRepositoryTest extends AbstractDataTest {

    @ClassRule
    public static GenericContainer elasticsearch =
            new FixedHostPortGenericContainer("docker.elastic.co/elasticsearch/elasticsearch:5.5.1")
                    .withExposedPorts(9300)
                    .withEnv("ES_JAVA_OPTS", "-Xms256m -Xmx256m")
                    .withEnv("cluster.name", "test")
                    .withEnv("xpack.security.enabled", "false")
                    .withEnv("http.cors.enabled", "true")
                    .withEnv("http.cors.allow-origin", "*")
                    .withEnv("http.host", "0.0.0.0")
                    .withEnv("transport.host", "0.0.0.0");

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@Nonnull ConfigurableApplicationContext context) {
            String address = elasticsearch.getContainerIpAddress() + ":" + elasticsearch.getMappedPort(9300);
            TestPropertyValues.of("spring.data.elasticsearch.cluster-nodes=" + address)
                    .applyTo(context);
        }
    }

    @Autowired
    private ArticleRepository repository;

    @Test
    public void testCommonCrud() {
        Article article = new Article();
        article.setId("1");
        article.setAuthor("Rose");
        article.setTitle("泰坦尼克号");
        article.setContent("You jump I jump");

        Article persisted = repository.save(article);
        Assertions.assertThat(persisted)
                .isNotNull()
                .isEqualTo(article);

        Comment comment = new Comment();
        comment.setNickName("Jack");
        comment.setStatement("Shut up, just jump!");
        article.setComments(Lists.newArrayList(comment));

        persisted = repository.save(article);
        Assertions.assertThat(persisted)
                .isNotNull()
                .isEqualTo(article);

        Article found = repository.findById("1").orElse(null);
        Assertions.assertThat(found)
                .isNotNull()
                .isEqualTo(article);

        CriteriaQuery criteriaQuery = new CriteriaQuery(Criteria.where("title").startsWith("泰坦"));
        found = repository.queryForObject(criteriaQuery);
        Assertions.assertThat(found)
                .isNotNull()
                .isEqualTo(article);

        repository.deleteById("1");
        found = repository.findById("1").orElse(null);
        Assertions.assertThat(found).isNull();
    }
}
