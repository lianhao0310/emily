package com.alice.emily.spatial.function.create;

import com.alice.emily.spatial.utils.GeometryFactories;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.OctagonalEnvelope;

/**
 * Computes the octogonal envelope of a geometry.
 *
 * @author Erwan Bocher
 * @see com.vividsolutions.jts.geom.OctagonalEnvelope
 */
public class ST_OctogonalEnvelope {

    public static Geometry computeOctogonalEnvelope(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        return new OctagonalEnvelope(geometry).toGeometry(GeometryFactories.default_());
    }
}
