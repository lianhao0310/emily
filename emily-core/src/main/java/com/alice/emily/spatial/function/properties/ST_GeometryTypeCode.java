package com.alice.emily.spatial.function.properties;


import com.alice.emily.spatial.utils.GeometryMetaData;

import java.io.IOException;

/**
 * Returns the OGC SFS {@link com.alice.emily.spatial.utils.GeometryTypeCodes} of a Geometry.
 * This function does not take account of Z nor M.
 * This function is not part of SFS; see {@link com.alice.emily.spatial.function.properties.ST_GeometryType}
 * It is used in constraints.
 */
public class ST_GeometryTypeCode {

    /**
     * @param geometry Geometry WKB.
     * @return Returns the OGC SFS {@link com.alice.emily.spatial.utils.GeometryTypeCodes} of a Geometry.
     * This function does not take account of Z nor M.
     * @throws IOException WKB is not valid.
     */
    public static Integer getTypeCode(byte[] geometry) throws IOException {
        if (geometry == null) {
            return null;
        }
        return GeometryMetaData.getMetaDataFromWKB(geometry).getGeometryType();
    }
}
