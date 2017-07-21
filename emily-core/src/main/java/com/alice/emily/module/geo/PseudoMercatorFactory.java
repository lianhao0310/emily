package com.alice.emily.module.geo;

import com.vividsolutions.jts.geom.*;

import java.util.Collection;

/**
 * Created by liupin on 2017/1/4.
 */
public class PseudoMercatorFactory {

    private static final GeometryFactory GEOMETRY_FACTORY =
            new GeometryFactory(new PrecisionModel(), GeoCRSCode.WGS84_PSEUDO_MERCATOR);

    public static Geometry buildGeometry(Collection<? extends Geometry> geomList) {
        return GEOMETRY_FACTORY.buildGeometry(geomList);
    }

    public static Geometry toGeometry(Envelope envelope) {
        return GEOMETRY_FACTORY.toGeometry(envelope);
    }

    public static Geometry createGeometry(Geometry g) {
        return GEOMETRY_FACTORY.createGeometry(g);
    }

    public static GeometryCollection createGeometryCollection(Geometry[] geometries) {
        return GEOMETRY_FACTORY.createGeometryCollection(geometries);
    }

    public static GeometryCollection createGeometryCollection(Collection<? extends Geometry> geometries) {
        return GEOMETRY_FACTORY.createGeometryCollection(geometries.toArray(new Geometry[geometries.size()]));
    }

    public static Point createPoint(Coordinate coordinate) {
        return GEOMETRY_FACTORY.createPoint(coordinate);
    }

    public static Point createPoint(CoordinateSequence coordinates) {
        return GEOMETRY_FACTORY.createPoint(coordinates);
    }

    public static MultiLineString createMultiLineString(LineString[] lineStrings) {
        return GEOMETRY_FACTORY.createMultiLineString(lineStrings);
    }

    public static MultiLineString createMultiLineString(Collection<LineString> lineStrings) {
        return GEOMETRY_FACTORY.createMultiLineString(lineStrings.toArray(new LineString[lineStrings.size()]));
    }

    public static MultiPolygon createMultiPolygon(Polygon[] polygons) {
        return GEOMETRY_FACTORY.createMultiPolygon(polygons);
    }

    public static MultiPolygon createMultiPolygon(Collection<Polygon> polygons) {
        return GEOMETRY_FACTORY.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
    }

    public static LinearRing createLinearRing(Coordinate[] coordinates) {
        return GEOMETRY_FACTORY.createLinearRing(coordinates);
    }

    public static LinearRing createLinearRing(CoordinateSequence coordinates) {
        return GEOMETRY_FACTORY.createLinearRing(coordinates);
    }

    public static LinearRing createLinearRing(Collection<Coordinate> coordinates) {
        return GEOMETRY_FACTORY.createLinearRing(coordinates.toArray(new Coordinate[coordinates.size()]));
    }

    public static MultiPoint createMultiPoint(Point[] points) {
        return GEOMETRY_FACTORY.createMultiPoint(points);
    }

    public static MultiPoint createMultiPoint(Coordinate[] coordinates) {
        return GEOMETRY_FACTORY.createMultiPoint(coordinates);
    }

    public static MultiPoint createMultiPoint(CoordinateSequence coordinates) {
        return GEOMETRY_FACTORY.createMultiPoint(coordinates);
    }

    public static LineString createLineString(Coordinate[] coordinates) {
        return GEOMETRY_FACTORY.createLineString(coordinates);
    }

    public static LineString createLineString(Collection<Coordinate> coordinates) {
        return GEOMETRY_FACTORY.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
    }

    public static LineString createLineString(CoordinateSequence coordinates) {
        return GEOMETRY_FACTORY.createLineString(coordinates);
    }

    public static Polygon createPolygon(LinearRing shell, LinearRing[] holes) {
        return GEOMETRY_FACTORY.createPolygon(shell, holes);
    }

    public static Polygon createPolygon(CoordinateSequence coordinates) {
        return GEOMETRY_FACTORY.createPolygon(coordinates);
    }

    public static Polygon createPolygon(Coordinate[] coordinates) {
        return GEOMETRY_FACTORY.createPolygon(coordinates);
    }

    public static Polygon createPolygon(LinearRing shell) {
        return GEOMETRY_FACTORY.createPolygon(shell);
    }
}
