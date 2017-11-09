package com.alice.emily.spatial.function.properties;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Return the type of geometry : POINT, LINESTRING, POLYGON...
 */
public class ST_GeometryType {

    /**
     * @param geometry Geometry instance
     * @return Geometry type equivalent to {@link com.vividsolutions.jts.geom.Geometry#getGeometryType()}
     */
    public static String getGeometryType(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        return geometry.getGeometryType();
    }
}
