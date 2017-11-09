package com.alice.emily.data.repo.mongo;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by lianhao on 2017/6/8.
 */
@Data
@Document(collection = "department")
public class Department {

    @Id
    private String id;

    private String name;

    private Geometry point;

    private List<Employee> employees;

}
