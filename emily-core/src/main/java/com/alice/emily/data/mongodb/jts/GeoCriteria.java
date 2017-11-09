package com.alice.emily.data.mongodb.jts;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;

/**
 * Class for building geospatial 2D queries using JTS mapped {@link Geometry}.
 *
 * @author Laurent Canet
 */
public class GeoCriteria extends Criteria {

    private LinkedHashMap<String, Object> criteria = new LinkedHashMap<String, Object>();
    private String key;

    private GeoCriteria(String key) {
        super(key);
        this.key = key;
    }

    /**
     * Static factory method to create a Criteria using the provided key
     *
     * @param key
     * @return
     */
    public static GeoCriteria where(String key) {
        return new GeoCriteria(key);
    }

    /**
     * Creates a geospatial criterion using a $near operation. This is only available for Mongo 2.4 and higher.
     *
     * @param point must not be {@literal null}
     * @return
     */
    public GeoCriteria near(Point point) {
        Assert.notNull(point, "Point must not be null");
        criteria.put("$near", new Document("$geometry", GeometryWriteConverter.INSTANCE.convert(point)));
        return this;
    }

    /**
     * Creates a geospatial criterion using a $nearSphere operation. This is only available for Mongo 2.4 and higher.
     *
     * @param point must not be {@literal null}
     * @return
     */
    public GeoCriteria nearSphere(Point point) {
        Assert.notNull(point, "Point must not be null");
        criteria.put("$nearSphere", new Document("$geometry", GeometryWriteConverter.INSTANCE.convert(point)));
        return this;
    }

    /**
     * Creates a geospatial criterion using $geoWithin operation on the given geometry. This is only available for Mongo
     * 2.4 and higher.
     *
     * @param geometry
     * @return
     */
    public GeoCriteria geoWithin(Geometry geometry) {
        Assert.notNull(geometry, "Geometry must not be null");
        criteria.put("$geoWithin", new Document("$geometry", GeometryWriteConverter.INSTANCE.convert(geometry)));
        return this;
    }

    /**
     * Creates a geospatial criterion using $geoIntersects operation on the given geometry. This is only available for
     * Mongo 2.4 and higher.
     *
     * @param geometry
     * @return
     */
    public GeoCriteria geoIntersects(Geometry geometry) {
        Assert.notNull(geometry, "Geometry must not be null");
        criteria.put("$geoIntersects", new Document("$geometry", GeometryWriteConverter.INSTANCE.convert(geometry)));
        return this;
    }

    @Override
    protected Document getSingleCriteriaObject() {
        Document document = new Document();
        boolean not = false;
        for (String k : this.criteria.keySet()) {
            Object value = this.criteria.get(k);
            if (not) {
                Document notDocument = new Document();
                notDocument.put(k, value);
                document.put("$not", notDocument);
                not = false;
            } else {
                if ("$not".equals(k) && value == null) {
                    not = true;
                } else {
                    document.put(k, value);
                }
            }
        }

        Document queryCriteria = new Document();
        queryCriteria.putAll(super.getSingleCriteriaObject());
        queryCriteria.put(this.key, document);
        return queryCriteria;
    }

    /**
     * Return the key of this criteria
     *
     * @return
     */
    public String getKey() {
        return key;
    }

}
