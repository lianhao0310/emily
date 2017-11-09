package com.alice.emily.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.alice.emily.spatial.utils.CRS;
import com.alice.emily.validator.constraints.FMax;
import com.alice.emily.validator.constraints.FMin;
import com.alice.emily.validator.constraints.FRange;
import com.alice.emily.validator.constraints.Negative;
import com.alice.emily.validator.constraints.Positive;
import com.alice.emily.validator.constraints.PseudoMercatorX;
import com.alice.emily.validator.constraints.PseudoMercatorY;
import com.alice.emily.validator.constraints.SRID;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Nonnull;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by lianhao on 2017/6/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ValidationTest {

    @Autowired
    private Validator validator;

    @Test
    public void testDMax() {
        validator(IndividualDemo.class)
                .validate("max", 220F)
                .assertNotPassed("must be less than or equal to 200.0")
                .validate("max", 200F)
                .assertPassed()
                .validate("max", 199F)
                .assertPassed();
    }

    @Test
    public void testDMin() {
        validator(IndividualDemo.class)
                .validate("min", 10.0)
                .assertNotPassed("must be greeter than or equal to 100.0")
                .validate("min", 100.0)
                .assertPassed()
                .validate("min", 101.0)
                .assertPassed();
    }

    @Test
    public void testDRange() {
        validator(IndividualDemo.class)
                .validate("range", 9.0)
                .assertNotPassed("must be between 10.0 and 20.0")
                .validate("range", 10.0)
                .assertPassed()
                .validate("range", 15.0)
                .assertPassed()
                .validate("range", 20.0)
                .assertPassed()
                .validate("range", 21.0)
                .assertNotPassed("must be between 10.0 and 20.0");
    }

    @Test
    public void testPositive() {
        validator(IndividualDemo.class)
                .validate("positive", 1)
                .assertPassed()
                .validate("positive", 0)
                .assertNotPassed("value or element value must be positive")
                .validate("positive", -1)
                .assertNotPassed("value or element value must be positive");

        validator(IndividualDemo.class)
                .validate("positiveArray", new Short[]{ 0, 1, 2 })
                .assertNotPassed("value or element value must be positive")
                .validate("positiveArray", new Short[]{ null, 1, 2 })
                .assertPassed()
                .validate("positiveArray", new Short[]{ 3, 1, 2 })
                .assertPassed();

        validator(IndividualDemo.class)
                .validate("positiveCollection", Lists.newArrayList(BigDecimal.valueOf(0), BigDecimal.valueOf(1)))
                .assertNotPassed("value or element value cannot be null and must be positive")
                .validate("positiveCollection", Lists.newArrayList(null, BigDecimal.valueOf(1)))
                .assertNotPassed("value or element value cannot be null and must be positive")
                .validate("positiveCollection", Lists.newArrayList(BigDecimal.valueOf(2), BigDecimal.valueOf(1)))
                .assertPassed();
    }

    @Test
    public void testNegative() {
        validator(IndividualDemo.class)
                .validate("negative", 1)
                .assertNotPassed("value or element value must be negative")
                .validate("negative", 0)
                .assertNotPassed("value or element value must be negative")
                .validate("negative", -1)
                .assertPassed();

        validator(IndividualDemo.class)
                .validate("negativeArray", new byte[]{ -1, 0, 1 })
                .assertNotPassed("value or element value must be negative")
                .validate("negativeArray", new byte[]{ -1, -2, -3 })
                .assertPassed();

        validator(IndividualDemo.class)
                .validate("negativeCollection", Sets.newHashSet(null, -1, -2))
                .assertNotPassed("value or element value cannot be null and must be negative")
                .validate("negativeCollection", Sets.newHashSet(0, -1, -2))
                .assertNotPassed("value or element value cannot be null and must be negative")
                .validate("negativeCollection", Sets.newHashSet(-3, -1, -2))
                .assertPassed()
                .validate("negativeCollection", Sets.newHashSet())
                .assertPassed();
    }

    @Test
    public void testPseudoMercatorX() {
        validator(IndividualDemo.class)
                .validate("x", -30026376.39)
                .assertNotPassed("must be in pseudo mercator x bound (-20026376.39, 20026376.39)")
                .validate("x", -20026376.39)
                .assertPassed()
                .validate("x", -100.0)
                .assertPassed()
                .validate("x", 20026376.39)
                .assertPassed()
                .validate("x", 30026376.39)
                .assertNotPassed("must be in pseudo mercator x bound (-20026376.39, 20026376.39)");
    }

    @Test
    public void testPseudoMercatorY() {
        validator(IndividualDemo.class)
                .validate("y", -30048966.10)
                .assertNotPassed("must be in pseudo mercator y bound (-20048966.10, 20048966.10)")
                .validate("y", -20048966.10)
                .assertPassed()
                .validate("y", 100.0)
                .assertPassed()
                .validate("y", 20048966.10)
                .assertPassed()
                .validate("y", 30048966.10)
                .assertNotPassed("must be in pseudo mercator y bound (-20048966.10, 20048966.10)");
    }

    @Test
    public void testSRID() {
        validator(IndividualDemo.class)
                .validate("coordinate", new Coordinate(-20026376.40, 20048966.0))
                .assertNotPassed("value or element value must be a valid EPSG 3857 geometry(coordinate) object")
                .validate("coordinate", new Coordinate(-20026375.39, 20048966.11))
                .assertNotPassed("value or element value must be a valid EPSG 3857 geometry(coordinate) object")
                .validate("coordinate", new Coordinate(300, 20048966.0))
                .assertPassed()
                .validate("coordinate", new Coordinate(13518793.30541396, 3664184.7410858735))
                .assertPassed()
                .validate("coordinate", new Coordinate(0, 0))
                .assertPassed();

        GeometryFactory factory = new GeometryFactory();

        validator(IndividualDemo.class)
                .validate("geometries", new Geometry[]{ factory.createPoint(new Coordinate(-180.0, 45.0)) })
                .assertNotPassed("value or element value cannot be null and must be a valid EPSG 4326 geometry(coordinate) object")
                .validate("geometries", new Geometry[]{ factory.createPoint(new Coordinate(60.0, 90.0)) })
                .assertPassed()
                .validate("geometries", new Geometry[]{ factory.createPoint(new Coordinate(60.0, 45.0)) })
                .assertPassed()
                .validate("geometries", new Geometry[]{ factory.createPoint(new Coordinate(0.0, 0.0)) })
                .assertPassed();
    }

    private <T> ValidatorAssertionBuilder<T> validator(@Nonnull Class<T> type) {
        return new ValidatorAssertionBuilder<>(type);
    }

    public class ValidatorAssertionBuilder<T> {

        private Set<ConstraintViolation<T>> violations;

        private final Class<T> type;

        ValidatorAssertionBuilder(Class<T> type) {
            this.type = type;
        }

        ValidatorAssertionBuilder<T> validate(String name, Object value) {
            violations = validator.validateValue(type, name, value);
            return this;
        }

        private ValidatorAssertionBuilder<T> assertNotPassed(String message) {
            assertThat(violations).isNotNull().hasSize(1);
            assertThat(violations.iterator().next())
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("message", message);
            return this;
        }

        private ValidatorAssertionBuilder<T> assertPassed() {
            assertThat(violations).isNullOrEmpty();
            return this;
        }
    }

    @Data
    public static class IndividualDemo {

        @FMin(100)
        private Double min;

        @FMax(200)
        private Float max;

        @FRange(min = 10, max = 20)
        private double range;

        @Negative
        private Long negative;

        @Negative
        private byte[] negativeArray;

        @Negative(elementNullable = false)
        private Set<Double> negativeCollection;

        @Positive
        private Long positive;

        @Positive
        private Short[] positiveArray;

        @Positive(elementNullable = false)
        private List<BigDecimal> positiveCollection;

        @PseudoMercatorX
        private Double x;

        @PseudoMercatorY
        private Double y;

        @SRID
        private Coordinate coordinate;

        @SRID(value = CRS.WGS84, elementNullable = false)
        private Geometry[] geometries;
    }
}
