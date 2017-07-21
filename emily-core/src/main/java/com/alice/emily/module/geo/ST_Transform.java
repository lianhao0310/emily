package com.alice.emily.module.geo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import org.cts.CRSFactory;
import org.cts.IllegalCoordinateException;
import org.cts.crs.CRSException;
import org.cts.crs.CoordinateReferenceSystem;
import org.cts.crs.GeodeticCRS;
import org.cts.op.CoordinateOperation;
import org.cts.op.CoordinateOperationFactory;
import org.cts.registry.EPSGRegistry;
import org.h2gis.api.AbstractFunction;
import org.h2gis.api.ScalarFunction;
import org.h2gis.functions.spatial.crs.EPSGTuple;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is used to transform a geometry from one CRS to another.
 * Only integer codes available in the spatial_ref_sys table are allowed.
 * The default source CRS is the input geometry's internal CRS.
 *
 * @author Erwan Bocher
 * @author Adam Gouge
 */
public class ST_Transform extends AbstractFunction implements ScalarFunction {

    private static CRSFactory crsf = new CRSFactory();
    private static EPSGRegistry epsg = new EPSGRegistry();
    private static Map<EPSGTuple, CoordinateOperation> copPool = new CopCache(5);

    static {
        crsf.getRegistryManager().addRegistry(epsg);
    }

    /**
     * Constructor
     */
    public ST_Transform() {
        addProperty(PROP_REMARKS, "Transform a geometry from one CRS to another " +
                "using integer codes from the SPATIAL_REF_SYS table.");
    }

    @Override
    public String getJavaStaticMethod() {
        return "ST_Transform";
    }

    /**
     * Returns a new geometry transformed to the SRID referenced by the integer
     * parameter available in the spatial_ref_sys table
     *
     * @param geom
     * @param codeEpsg
     * @return
     * @throws GeoException
     */
    public static Geometry ST_Transform(Geometry geom, Integer codeEpsg) throws GeoException {
        if (geom == null) {
            return null;
        }
        if (codeEpsg == null) {
            throw new IllegalArgumentException("The SRID code cannot be null.");
        }
        try {
            int inputSRID = geom.getSRID();
            if (inputSRID == 0) {
                throw new GeoException("Cannot find a CRS");
            } else {
                CoordinateReferenceSystem inputCRS = crsf.getCRS(epsg.getRegistryName() + ":" + String.valueOf(inputSRID));
                CoordinateReferenceSystem targetCRS = crsf.getCRS(epsg.getRegistryName() + ":" + String.valueOf(codeEpsg));
                if (inputCRS.equals(targetCRS)) {
                    return geom;
                }
                EPSGTuple epsg = new EPSGTuple(inputSRID, codeEpsg);
                CoordinateOperation op = copPool.get(epsg);
                if (op != null) {
                    Geometry outPutGeom = (Geometry) geom.clone();
                    outPutGeom.apply(new CRSTransformFilter(op));
                    outPutGeom.setSRID(codeEpsg);
                    return outPutGeom;
                } else {
                    if (inputCRS instanceof GeodeticCRS && targetCRS instanceof GeodeticCRS) {
                        List<CoordinateOperation> ops = CoordinateOperationFactory
                                .createCoordinateOperations((GeodeticCRS) inputCRS, (GeodeticCRS) targetCRS);
                        if (!ops.isEmpty()) {
                            op = ops.get(0);
                            Geometry outPutGeom = (Geometry) geom.clone();
                            outPutGeom.apply(new CRSTransformFilter(op));
                            copPool.put(epsg, op);
                            outPutGeom.setSRID(codeEpsg);
                            return outPutGeom;
                        }
                    } else {
                        throw new GeoException("The transformation from "
                                + inputCRS + " to " + codeEpsg + " is not yet supported.");
                    }
                }
            }
        } catch (CRSException ex) {
            throw new GeoException("Cannot create the CRS", ex);
        }
        return null;

    }

    /**
     * This method is used to apply a {@link CoordinateOperation} to a geometry.
     * The transformation loops on each coordinate.
     */
    public static class CRSTransformFilter implements CoordinateFilter {

        private final CoordinateOperation coordinateOperation;

        public CRSTransformFilter(final CoordinateOperation coordinateOperation) {
            this.coordinateOperation = coordinateOperation;
        }

        @Override
        public void filter(Coordinate coord) {
            try {
                if (Double.isNaN(coord.z)) {
                    coord.z = 0;
                }
                double[] xyz = coordinateOperation
                        .transform(new double[]{coord.x, coord.y, coord.z});
                coord.x = xyz[0];
                coord.y = xyz[1];
                if (xyz.length > 2) {
                    coord.z = xyz[2];
                } else {
                    coord.z = Double.NaN;
                }
            } catch (IllegalCoordinateException ice) {
                throw new RuntimeException("Cannot transform the coordinate" + coord.toString(), ice);
            }

        }


    }

    /**
     * A simple cache to manage {@link CoordinateOperation}
     */
    public static class CopCache extends LinkedHashMap<EPSGTuple, CoordinateOperation> {

        private final int limit;

        public CopCache(int limit) {
            super(16, 0.75f, true);
            this.limit = limit;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<EPSGTuple, CoordinateOperation> eldest) {
            return size() > limit;
        }
    }
}