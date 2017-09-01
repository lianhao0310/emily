package com.palmaplus.euphoria.module.geo;

import com.vividsolutions.jts.geom.Geometry;
import org.junit.Test;

/**
 * Created by liupin on 2017/1/5.
 */
public class CRSTest {

    @Test
    public void testTransform() {
        Geometry geometry = GeoFunctions.geomFromText("POINT(38758575.2894 74764918.5831)", GeoCRSCode.WGS84_PSEUDO_MERCATOR);
        Geometry transform = GeoFunctions.transform(geometry, GeoCRSCode.WGS84);
        System.out.println(GeoFunctions.asText(transform));
    }
}
