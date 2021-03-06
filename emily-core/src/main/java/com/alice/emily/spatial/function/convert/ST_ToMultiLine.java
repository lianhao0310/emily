package com.alice.emily.spatial.function.convert;

import com.alice.emily.spatial.utils.GeometryFactories;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * ST_ToMultiLine constructs a MultiLineString from the given geometry's
 * coordinates. Returns MULTILINESTRING EMPTY for geometries of dimension 0.
 */
public class ST_ToMultiLine {

    /**
     * Constructs a MultiLineString from the given geometry's coordinates.
     *
     * @param geom Geometry
     * @return A MultiLineString constructed from the given geometry's coordinates
     * @throws SQLException
     */
    public static MultiLineString createMultiLineString(Geometry geom) throws SQLException {
        if (geom != null) {
            GeometryFactory gf = GeometryFactories.default_();
            if (geom.getDimension() > 0) {
                final List<LineString> lineStrings = new LinkedList<LineString>();
                toMultiLineString(geom, lineStrings);
                return gf.createMultiLineString(
                        lineStrings.toArray(new LineString[lineStrings.size()]));
            } else {
                return gf.createMultiLineString(null);
            }
        } else {
            return null;
        }
    }

    private static void toMultiLineString(final Geometry geometry,
                                          final List<LineString> lineStrings) throws SQLException {
        if ((geometry instanceof Point) || (geometry instanceof MultiPoint)) {
            throw new SQLException("Found a point! Cannot create a MultiLineString.");
        } else if (geometry instanceof LineString) {
            toMultiLineString((LineString) geometry, lineStrings);
        } else if (geometry instanceof Polygon) {
            toMultiLineString((Polygon) geometry, lineStrings);
        } else if (geometry instanceof GeometryCollection) {
            toMultiLineString((GeometryCollection) geometry, lineStrings);
        }
    }

    private static void toMultiLineString(final LineString lineString,
                                          final List<LineString> lineStrings) {
        lineStrings.add(lineString);
    }

    private static void toMultiLineString(final Polygon polygon,
                                          final List<LineString> lineStrings) {
        lineStrings.add(polygon.getExteriorRing());
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            lineStrings.add(polygon.getInteriorRingN(i));
        }
    }

    private static void toMultiLineString(final GeometryCollection geometryCollection,
                                          final List<LineString> lineStrings) throws SQLException {
        for (int i = 0; i < geometryCollection.getNumGeometries(); i++) {
            toMultiLineString(geometryCollection.getGeometryN(i), lineStrings);
        }
    }
}
