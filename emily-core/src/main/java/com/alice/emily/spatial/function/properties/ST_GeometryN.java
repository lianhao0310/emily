package com.alice.emily.spatial.function.properties;

import com.alice.emily.spatial.SpatialException;
import com.vividsolutions.jts.geom.Geometry;


/**
 * Return Geometry number n from the given GeometryCollection. Use {@link
 * com.alice.emily.spatial.function.properties.ST_NumGeometries}
 * to retrieve the total number of Geometries.
 */
public class ST_GeometryN {

    private static final String OUT_OF_BOUNDS_ERR_MESSAGE =
            "Geometry index out of range. Must be between 1 and ST_NumGeometries.";

    /**
     * Return Geometry number n from the given GeometryCollection.
     *
     * @param geometry GeometryCollection
     * @param n        Index of Geometry number n in [1-N]
     * @return Geometry number n or Null if parameter is null.
     */
    public static Geometry getGeometryN(Geometry geometry, Integer n) {
        if (geometry == null) {
            return null;
        }
        if (n >= 1 && n <= geometry.getNumGeometries()) {
            return geometry.getGeometryN(n - 1);
        } else {
            throw new SpatialException(OUT_OF_BOUNDS_ERR_MESSAGE);
        }
    }
}
