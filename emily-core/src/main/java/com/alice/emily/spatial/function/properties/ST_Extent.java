package com.alice.emily.spatial.function.properties;

import com.alice.emily.spatial.utils.GeometryFactories;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * ST_Extent returns an {@link com.vividsolutions.jts.geom.Envelope} that cover all aggregated geometries.
 */
public class ST_Extent {

    private Envelope aggregatedEnvelope = new Envelope();

    public void add(Geometry geometry) {
        aggregatedEnvelope.expandToInclude(geometry.getEnvelopeInternal());
    }

    public static Geometry extend(Geometry... geometries) {
        Envelope aggregatedEnvelope = new Envelope();
        for (Geometry geometry : geometries) {
            aggregatedEnvelope.expandToInclude(geometry.getEnvelopeInternal());
        }
        if (aggregatedEnvelope.isNull()) {
            return null;
        } else {
            return GeometryFactories.default_().toGeometry(aggregatedEnvelope);
        }
    }
}
