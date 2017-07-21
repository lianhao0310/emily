package ${package};

import ${package}.domain.Customer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerResourceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetById() {
        Customer customer = restTemplate.getForObject("/customer/1", Customer.class);
        Assert.assertEquals(customer.getId().intValue(), 1);
        Assert.assertEquals(customer.getFirstName(), "Jack");
    }

    @Test
    public void testGetByName() {
        List customers = restTemplate.getForObject("/customer/Bauer", List.class);
        Assert.assertEquals(customers.size(), 2);
    }
}