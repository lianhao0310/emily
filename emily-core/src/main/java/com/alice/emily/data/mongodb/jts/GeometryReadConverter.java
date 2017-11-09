package com.alice.emily.data.mongodb.jts;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Converter for reading JTS {@link Geometry} out of mongodb GeoJSON
 *
 * @author Laurent Canet
 */
@ReadingConverter
public enum GeometryReadConverter implements Converter<Document, Geometry> {
    INSTANCE;

    private GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Geometry convert(@Nonnull Document document) {

        String type = document.getString("type");
        List coordinates = (List) document.get("coordinates");

        if ("Polygon".equals(type)) {
            return readPolygon(coordinates);
        } else if ("LineString".equals(type)) {
            return readLine(coordinates);
        } else if ("Point".equals(type)) {
            return readPoint(coordinates);
        } else if ("MultiPoint".equals(type)) {
            return readMultiPoint(coordinates);
        } else if ("MultiLineString".equals(type)) {
            return readMultiLineString(coordinates);
        } else if ("MultiPolygon".equals(type)) {
            return readMultiPolygon(coordinates);
        } else {
            throw new IllegalArgumentException("Unsupported geometry type " + type);
        }
    }

    private MultiPoint readMultiPoint(List<List<? extends Number>> coordinates) {
        Coordinate[] pointCoordinates = new Coordinate[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            pointCoordinates[i] = buildCoordinate(coordinates.get(i));
        }
        return geometryFactory.createMultiPoint(pointCoordinates);
    }

    private MultiLineString readMultiLineString(List<List<List<? extends Number>>> coordinates) {
        LineString[] lineStrings = new LineString[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            lineStrings[i] = readLine(coordinates.get(i));
        }
        return geometryFactory.createMultiLineString(lineStrings);
    }

    private MultiPolygon readMultiPolygon(List<List<List<List<? extends Number>>>> coordinates) {
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            polygons[i] = readPolygon(coordinates.get(i));
        }
        return geometryFactory.createMultiPolygon(polygons);
    }

    private Polygon readPolygon(List<List<List<? extends Number>>> polygon) {

        LinearRing shell = buildLinearRing(polygon.get(0));
        if (polygon.size() == 1) {
            return geometryFactory.createPolygon(shell);
        } else {
            LinearRing[] holes = new LinearRing[polygon.size() - 1];
            for (int i = 1; i < polygon.size(); i++) {
                holes[i - 1] = buildLinearRing(polygon.get(i));
            }
            return geometryFactory.createPolygon(shell, holes);
        }
    }

    private LinearRing buildLinearRing(List<List<? extends Number>> line) {
        Coordinate[] coordinates = new Coordinate[line.size()];
        for (int i = 0; i < line.size(); i++) {
            coordinates[i] = buildCoordinate(line.get(i));
        }
        return geometryFactory.createLinearRing(coordinates);
    }

    private LineString readLine(List<List<? extends Number>> line) {
        Coordinate[] coordinates = new Coordinate[line.size()];
        for (int i = 0; i < line.size(); i++) {
            coordinates[i] = buildCoordinate(line.get(i));
        }
        return geometryFactory.createLineString(coordinates);
    }

    private Point readPoint(List<? extends Number> pt) {
        return geometryFactory.createPoint(buildCoordinate(pt));
    }

    private Coordinate buildCoordinate(List<? extends Number> pt) {
        return new Coordinate(pt.get(0).doubleValue(), pt.get(1).doubleValue());
    }

}
