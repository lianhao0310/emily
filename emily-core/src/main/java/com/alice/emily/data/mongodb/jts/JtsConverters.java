package com.alice.emily.data.mongodb.jts;

import com.vividsolutions.jts.geom.Coordinate;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ClassUtils;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Holder of geometry converters
 */
public final class JtsConverters {

    private static final boolean IS_JTS_PRESENT = ClassUtils.isPresent(
            "com.vividsolutions.jts.geom.Geometry", ClassUtils.getDefaultClassLoader());

    private JtsConverters() {

    }

    /**
     * Return a list of all converters needed to handle geometric data
     */
    public static List<Converter<?, ?>> geometryConverters() {
        if (!IS_JTS_PRESENT) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                CoordinateReadConverter.INSTANCE,
                CoordinateWriteConverter.INSTANCE,
                GeometryReadConverter.INSTANCE,
                GeometryWriteConverter.INSTANCE);
    }

    public enum CoordinateReadConverter implements Converter<Document, Coordinate> {
        INSTANCE;

        @Override
        public Coordinate convert(@Nonnull Document source) {
            Double x = (Double) source.getOrDefault("x", Double.NaN);
            Double y = (Double) source.getOrDefault("y", Double.NaN);
            Double z = (Double) source.getOrDefault("z", Double.NaN);
            return new Coordinate(x, y, z);
        }
    }

    public enum CoordinateWriteConverter implements Converter<Coordinate, Document> {
        INSTANCE;

        @Override
        public Document convert(@Nonnull Coordinate coordinate) {
            Document document = new Document();
            document.put("x", coordinate.x);
            document.put("y", coordinate.y);
            if (!Double.isNaN(coordinate.z)) {
                document.put("z", coordinate.z);
            }
            return document;
        }
    }
}
