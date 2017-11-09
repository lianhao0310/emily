package com.alice.emily.validator;

import com.alice.emily.spatial.function.crs.ST_Transform;
import com.alice.emily.spatial.utils.CRS;
import com.alice.emily.spatial.utils.GeometryFactories;
import com.alice.emily.validator.constraints.SRID;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import javax.annotation.Nonnull;

/**
 * Created by lianhao on 2017/6/21.
 */
public class SRIDValidator extends AbstractValidator<SRID, Object> {

    private int srid;

    @Override
    public void initialize(SRID constraintAnnotation) {
        srid = constraintAnnotation.value();
        elementNullable = constraintAnnotation.elementNullable();
    }

    @Override
    protected ValidateFunc<?> getValidator(@Nonnull Class<?> type) {
        if (Coordinate.class.isAssignableFrom(type)) {
            return c -> isValid(GeometryFactories.factory(srid).createPoint((Coordinate) c));
        } else if (Geometry.class.isAssignableFrom(type)) {
            return g -> isValid((Geometry) g);
        } else {
            return null;
        }
    }

    private boolean isValid(Geometry geometry) {
        Geometry transformed = ST_Transform.transform(geometry, srid, CRS.WGS84_PSEUDO_MERCATOR);
        Coordinate[] coordinates = transformed.getCoordinates();
        for (Coordinate coordinate : coordinates) {
            if (coordinate.x < -20026376.39 || coordinate.x > 20026376.39) {
                return false;
            }
            if (coordinate.y < -20048966.10 || coordinate.y > 20048966.10) {
                return false;
            }
        }
        return true;
    }
}
