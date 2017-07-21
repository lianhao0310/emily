package ${package}.resource;

import ${package}.domain.Customer;
import ${package}.domain.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Component
@Path("customer")
public class CustomerResource {

    @Autowired
    private CustomerRepository repository;

    @GET
    public List<Customer> browser() {
        return repository.findAll();
    }

    @GET
    @Path("{id:\\d+}")
    public Customer getById(@PathParam("id") Long id) {
        return repository.findOne(id);
    }

    @GET
    @Path("{lastName:\\w+}")
    public List<Customer> getByLastName(@PathParam("lastName") String lastName) {
        return repository.findByLastName(lastName);
    }
}