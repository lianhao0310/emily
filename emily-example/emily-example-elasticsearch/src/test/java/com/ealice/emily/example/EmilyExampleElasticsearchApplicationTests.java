package com.ealice.emily.example;

import com.ealice.emily.example.entity.Article;
import com.ealice.emily.example.entity.Author;
import com.ealice.emily.example.entity.Tutorial;
import com.ealice.emily.example.repository.ArticleSearchRepository;
import com.google.common.base.Splitter;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmilyExampleElasticsearchApplicationTests {

	@Autowired
	private ArticleSearchRepository articleSearchRepository;

	@Test
	public void testSaveArticleIndex(){
		Author author=new Author();
		author.setId(1L);
		author.setName("tianshouzhi");
		author.setRemark("java developer");

		Tutorial tutorial=new Tutorial();
		tutorial.setId(1L);
		tutorial.setName("elastic search");

		Article article =new Article();
		article.setId(1L);
		article.setTitle("springboot integreate elasticsearch");
		article.setAbstracts("springboot integreate elasticsearch is very easy");
		article.setTutorial(tutorial);
		article.setAuthor(author);
		article.setContent("elasticsearch based on lucene,"
				+ "spring-data-elastichsearch based on elaticsearch"
				+ ",this tutorial tell you how to integrete springboot with spring-data-elasticsearch");
		article.setPostTime(new Date());
		article.setClickCount(1L);

		articleSearchRepository.save(article);
	}


	@Test
	public void testSearch(){
		String queryString="springboot";//搜索关键字
		QueryStringQueryBuilder builder=new QueryStringQueryBuilder(queryString);
		Iterable<Article> searchResult = articleSearchRepository.search(builder);
		Iterator<Article> iterator = searchResult.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
	}

	@Test
	public void testGuava(){
		String str = "foo,bar,,   qux";
		Iterable<String> iterable = Splitter.on(',')
				.trimResults()
				.omitEmptyStrings()
				.split(str);
		System.out.println(iterable.toString());

		String[] strs = str.split(",");

		System.out.println(Arrays.asList(strs).toString());
	}
}
