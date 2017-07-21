package com.alice.emily.module.geo;

import com.alice.emily.utils.Errors;
import com.vividsolutions.jts.geom.*;

import java.util.Date;
import java.util.List;

public class GeoFunctions {

    /**
     * Convert a Well Known Text geometry string into a geometry instance.
     */
    public static Geometry geomFromText(String wkt, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_GeomFromText.toGeometry(wkt, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_GeomFromText failed");
        }
    }

    /**
     * Compute geometry area.
     */
    public static Double area(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Area.getArea(geometry);
    }

    /**
     * Return the type of geometry : POINT, LINESTRING, POLYGON..
     */
    public static String geometryType(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_GeometryType.getGeometryType(geometry);
    }

    /**
     * Convert a WKT String into a POINT.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry pointFromText(String wkt, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_PointFromText.toGeometry(wkt, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_PointFromText failed");
        }
    }

    /**
     * Convert a WKT String into a MULTIPOINT.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry mPointFromText(String wkt, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_MPointFromText.toGeometry(wkt, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MPointFromText failed");
        }
    }

    /**
     * Convert a WKT String into a LINESTRING.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry lineFromText(String wkt, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_LineFromText.toGeometry(wkt, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_LineFromText failed");
        }
    }

    /**
     * Convert a WKT String into a MULTILINESTRING.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry mLineFromText(String wkt, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_MLineFromText.toGeometry(wkt, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MLineFromText failed");
        }
    }

    /**
     * Convert a WKT String into a POLYGON.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry polyFromText(String wkt, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_PolyFromText.toGeometry(wkt, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_PolyFromText failed");
        }
    }

    /**
     * Convert a WKT String into a MULTIPOLYGON.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry mPolyFromText(String wkt, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_MPolyFromText.toGeometry(wkt, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MPolyFromText failed");
        }
    }

    /**
     * Get dimension of a geometry 0 for a Point, 1 for a line and 2 for a polygon.
     */
    public static Integer dimension(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Dimension.getDimension(geometry);
    }

    /**
     * Convert a geometry into WKT, a text representation of the geometry.
     */
    public static String asText(Geometry geometry) {
        return org.h2gis.functions.spatial.convert.ST_AsText.asWKT(geometry);
    }

    /**
     * Convert a geometry into WKT, a text representation of the geometry.
     */
    public static String asWKT(Geometry geometry) {
        return org.h2gis.functions.spatial.convert.ST_AsWKT.asWKT(geometry);
    }

    /**
     * Convert Well Known Binary into Geometry then check that it is a POLYGON.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry polyFromWKB(byte[] bytes, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_PolyFromWKB.toPolygon(bytes, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_PolyFromWKB failed");
        }
    }

    /**
     * Check if the provided geometry is empty.
     */
    public static Boolean isEmpty(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_IsEmpty.isEmpty(geometry);
    }

    /**
     * Test if the provided geometry is simple.
     */
    public static Boolean isSimple(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_IsSimple.isSimple(geometry);
    }

    /**
     * Get geometry boundary as geometry.
     */
    public static Geometry boundary(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Boundary.getBoundary(geometry);
    }

    /**
     * Get geometry envelope as geometry.
     */
    public static Geometry envelope(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Envelope.getEnvelope(geometry);
    }

    /**
     * Get the first X coordinate.
     */
    public static Double x(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_X.getX(geometry);
    }

    /**
     * Get the first Y coordinate.
     */
    public static Double y(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Y.getY(geometry);
    }

    /**
     * Get the first Z coordinate.
     */
    public static Double z(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Z.getZ(geometry);
    }

    /**
     * Returns the first coordinate of a Geometry as a POINT, given that the Geometry is a LINESTRING or a MULTILINESTRING containing only one LINESTRING. Returns NULL for all other Geometries.
     */
    public static Geometry startPoint(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_StartPoint.getStartPoint(geometry);
    }

    /**
     * Returns the last coordinate of a Geometry as a POINT, given that the Geometry is a LINESTRING or a MULTILINESTRING containing only one LINESTRING. Returns NULL for all other Geometries.
     */
    public static Geometry endPoint(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_EndPoint.getEndPoint(geometry);
    }

    /**
     * Return TRUE if the provided geometry is a closed LINESTRING or MULTILINESTRING, null otherwise.
     */
    public static Boolean isClosed(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_IsClosed.isClosed(geometry);
    }

    /**
     * Return TRUE if the provided geometry is a closed and simple LINESTRING or MULTILINESTRING; NULL otherwise.
     */
    public static Boolean isRing(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_IsRing.isRing(geometry);
    }

    /**
     * Convert Well Known Binary into a LINESTRING.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry lineFromWKB(byte[] bytes, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_LineFromWKB.toLineString(bytes, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_LineFromWKB failed");
        }
    }

    /**
     * Returns the 2D length of the geometry if it is a LineString or MultiLineString.
     * 0 is returned for other geometries
     */
    public static Double length(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Length.getLength(geometry);
    }

    /**
     * Return the number of points in an LineString.
     */
    public static Integer numPoints(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_NumPoints.getNumPoints(geometry);
    }

    /**
     * Returns the <i>n</i>th point of a LINESTRING or a MULTILINESTRING containing exactly one LINESTRING; NULL otherwise. As the OGC specifies, ST_PointN is 1-N based.
     */
    public static Geometry pointN(Geometry a, int n) {
        try {
            return org.h2gis.functions.spatial.properties.ST_PointN.getPointN(a, n);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_PointN failed");
        }
    }

    /**
     * Computes the centroid of this Geometry. The centroid is equal to the centroid of the set of component Geometries of highest dimension (since the lower-dimension geometries contribute zero "weight" to the centroid) .
     */
    public static Geometry centroid(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Centroid.getCentroid(geometry);
    }

    /**
     * Get a Point that lie on the surface of a Surface Geometry. The returned point is always the same for the same geometry.
     */
    public static Geometry pointOnSurface(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_PointOnSurface.getInteriorPoint(geometry);
    }

    /**
     * Return true if Geometry A contains Geometry B
     */
    public static Boolean contains(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Contains.isContains(a, b);
    }

    /**
     * Returns a LinearRing instance or Null if parameter is not a Polygon.
     */
    public static Geometry exteriorRing(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_ExteriorRing.getExteriorRing(geometry);
    }

    /**
     * Return the number of holes in a geometry.
     */
    public static Integer numInteriorRings(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_NumInteriorRings.getHoles(geometry);
    }

    /**
     * Return the number of holes in a geometry.
     */
    public static Integer numInteriorRing(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_NumInteriorRing.getHoles(geometry);
    }

    /**
     * Returns interior ring number n from a Polygon. Use ST_NumInteriorRings to retrieve the total number of interior rings.
     */
    public static LineString interiorRingN(Geometry a, Integer n) {
        try {
            return org.h2gis.functions.spatial.properties.ST_InteriorRingN.getInteriorRing(a, n);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_InteriorRingN failed");
        }
    }

    /**
     * Get the number of geometries inside a geometry collection.
     */
    public static Integer numGeometries(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_NumGeometries.getNumGeometries(geometry);
    }

    /**
     * Returns Geometry number n from a GeometryCollection. Use ST_NumGeometries to retrieve the total number of Geometries.
     */
    public static Geometry geometryN(Geometry a, Integer n) {
        try {
            return org.h2gis.functions.spatial.properties.ST_GeometryN.getGeometryN(a, n);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_GeometryN failed");
        }
    }

    /**
     * Return true if Geometry A is equal to Geometry B
     */
    public static Boolean equals(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Equals.geomEquals(a, b);
    }

    /**
     * Return true if the two Geometries are disjoint
     */
    public static Boolean disjoint(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Disjoint.geomDisjoint(a, b);
    }

    /**
     * Return true if the geometry A touches the geometry B.
     */
    public static Boolean touches(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Touches.geomTouches(a, b);
    }

    /**
     * Return true if the geometry A is within the geometry B.
     */
    public static Boolean within(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Within.isWithin(a, b);
    }

    /**
     * Return true if the geometry A overlaps the geometry B.
     */
    public static Boolean overlaps(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Overlaps.isOverlaps(a, b);
    }

    /**
     * Return true if Geometry A crosses Geometry B
     */
    public static Boolean crosses(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Crosses.geomCrosses(a, b);
    }

    /**
     * Return true if the geometry A intersects the geometry B.
     */
    public static Boolean intersects(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Intersects.isIntersects(a, b);
    }

    /**
     * This function is used to compute the relation between two geometries, as described in the SFS specification. It can be used in two ways.
     * First, if it is given two geometries,it returns a 9-character String representation of the 2 geometries IntersectionMatrix. If it is
     * given two geometries and an IntersectionMatrix representation, it will return a boolean : true it the two geometries' IntersectionMatrix
     * match the given one, false otherwise.
     */
    public static Boolean relate(Geometry a, Geometry b, String iMatrix) {
        return org.h2gis.functions.spatial.predicates.ST_Relate.relate(a, b, iMatrix);
    }

    /**
     * For geometry type returns the 2-dimensional minimum Cartesian distance between two geometries in projected units (spatial ref units).
     */
    public static Double distance(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.properties.ST_Distance.distance(a, b);
    }

    /**
     * Compute the intersection of two Geometries
     */
    public static Geometry intersection(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.operators.ST_Intersection.intersection(a, b);
    }

    /**
     * Compute the difference between two Geometries
     */
    public static Geometry difference(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.operators.ST_Difference.difference(a, b);
    }

    /**
     * Compute the union of two or more Geometries
     */
    public static Geometry union(Geometry geometry) {
        return org.h2gis.functions.spatial.operators.ST_Union.union(geometry);
    }

    /**
     * Compute the symmetric difference between two Geometries
     */
    public static Geometry symDifference(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.operators.ST_SymDifference.symDifference(a, b);
    }

    /**
     * Computes the smallest convex POLYGON that contains all the points in the Geometry
     */
    public static Geometry convexHull(Geometry geometry) {
        return org.h2gis.functions.spatial.operators.ST_ConvexHull.convexHull(geometry);
    }

    /**
     * Retrieve the SRID from an EWKB encoded geometry.
     */
    public static Integer srid(byte[] bytes) {
        try {
            return org.h2gis.functions.spatial.properties.ST_SRID.getSRID(bytes);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_SRID failed");
        }
    }

    /**
     * Return true if the envelope of Geometry A intersects the envelope of Geometry B
     */
    public static Boolean envelopesIntersect(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_EnvelopesIntersect.intersects(a, b);
    }

    /**
     * Return a new geometry with a replaced spatial reference id. Warning, use ST_Transform if you want to change the coordinate
     * reference system as this method does not update the coordinates. This function can take at first argument an instance of Geometry or Envelope
     */
    public static Geometry setSRID(Geometry geometry, Integer srid) {
        try {
            return org.h2gis.functions.spatial.crs.ST_SetSRID.setSRID(geometry, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_SetSRID failed");
        }
    }

    /**
     * Transform a geometry from one CRS to another
     */
    public static Geometry transform(Geometry geometry, Integer srid) {
        return ST_Transform.ST_Transform(geometry, srid);
    }

    /**
     * Returns the dimension of the coordinates of the given geometry.
     */
    public static Integer coordDim(byte[] bytes) {
        try {
            return org.h2gis.functions.spatial.properties.ST_CoordDim.getCoordinateDimension(bytes);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_CoordDim failed");
        }
    }

    /**
     * Returns the OGC SFS geometry type code from a Geometry
     */
    public static Integer geometryTypeCode(byte[] bytes) {
        try {
            return org.h2gis.functions.spatial.properties.ST_GeometryTypeCode.getTypeCode(bytes);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_GeometryTypeCode failed");
        }
    }

    /**
     * Returns 1 if a geometry has a z-coordinate, otherwise 0.
     */
    public static int is3D(byte[] bytes) {
        try {
            return org.h2gis.functions.spatial.properties.ST_Is3D.is3D(bytes);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_Is3D failed");
        }
    }

    /**
     * Convert Well Known Binary into a POINT.
     * If an SRID is not specified, it defaults to 0.
     */
    public static Geometry pointFromWKB(byte[] bytes, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_PointFromWKB.toPoint(bytes, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_PointFromWKB failed");
        }
    }

    /**
     * Convert a binary large object to a geometry object.
     * An optional integer parameter could be used to specify the SRID.
     */
    public static Geometry geomFromWKB(byte[] bytes) {
        try {
            return org.h2gis.functions.spatial.convert.ST_GeomFromWKB.toGeometry(bytes);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_GeomFromWKB failed");
        }
    }

    /**
     * Returns the 3D length (of a LineString) or the 3D perimeter (of a Polygon).
     * Note : For 2D geometries, returns the 2D length.
     */
    public static Double stLength3D(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_3DLength.stLength3D(geometry);
    }

    /**
     * Adds a point to a geometry.
     * A tolerance could be set to snap the point to the geometry.
     */
    public static Geometry addPoint(Geometry a, Point point, double tolerance) {
        try {
            return org.h2gis.functions.spatial.edit.ST_AddPoint.addPoint(a, point, tolerance);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_AddPoint failed");
        }
    }

    /**
     * This function do a sum with the z value of (each vertex of) the
     * geometric parameter to the corresponding value given by a field.
     */
    public static Geometry addZ(Geometry geometry, double z) {
        try {
            return org.h2gis.functions.spatial.edit.ST_AddZ.addZ(geometry, z);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_AddZ failed");
        }
    }

    /**
     * Return the geometry as a Geometry Javascript Object Notation (GeoJSON 1.0) element.
     * 2D and 3D Geometries are both supported.
     * GeoJSON only supports SFS 1.1 geometry types (POINT, LINESTRING, POLYGON and COLLECTION).
     */
    public static String asGeoJSON(Geometry geometry) {
        return org.h2gis.functions.io.geojson.ST_AsGeoJSON.toGeojson(geometry);
    }

    /**
     * Return the geometry as a Keyhole Markup Language (KML) element.
     * Note this function supports two arguments : extrude (boolean) and altitude mode (integer).
     * Available extrude values are true, false or none.
     * Supported altitude mode :
     * For KML profil : CLAMPTOGROUND = 1; RELATIVETOGROUND = 2; ABSOLUTE = 4;
     * For GX profil : CLAMPTOSEAFLOOR = 8; RELATIVETOSEAFLOOR = 16;
     * No altitude : NONE = 0;
     */
    public static String asKml(Geometry geometry, boolean extrude, int altitudeModeEnum) {
        try {
            return org.h2gis.functions.io.kml.ST_AsKml.toKml(geometry, extrude, altitudeModeEnum);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_AsKml failed");
        }
    }

    /**
     * Compute the minimum bounding circle of a geometry
     */
    public static Geometry boundingCircle(Geometry geometry) {
        return org.h2gis.functions.spatial.create.ST_BoundingCircle.computeBoundingCircle(geometry);
    }

    /**
     * Compute the minimum bounding circle center of a geometry.This function is more precise than the conjunction of ST_CENTROID and ST_BoundingCircle
     */
    public static Point boundingCircleCenter(Geometry geometry) {
        return org.h2gis.functions.spatial.create.ST_BoundingCircleCenter.getCircumCenter(geometry);
    }

    /**
     * Compute the minimum bounding circle of a geometry. This is an alias for ST_BoundingCircle
     */
    public static Geometry minimumBoundingCircle(Geometry geometry) {
        return org.h2gis.functions.spatial.create.ST_MinimumBoundingCircle.computeBoundingCircle(geometry);
    }

    /**
     * Computes the closest coordinate(s) contained in the given geometry starting from the given point, using the 2D distance.
     */
    public static Geometry closestCoordinate(Point point, Geometry geometry) {
        return org.h2gis.functions.spatial.distance.ST_ClosestCoordinate.getFurthestCoordinate(point, geometry);
    }

    /**
     * Returns the 2D point on geometry A that is closest to geometry B.
     */
    public static Point closestPoint(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.distance.ST_ClosestPoint.closestPoint(a, b);
    }

    /**
     * Returns the compactness ratio of the given polygon, defined to be the the perimeter of a circle whose area is equal to the given geometry's area divided by the given polygon's perimeter.
     */
    public static Double compactnessRatio(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_CompactnessRatio.computeCompacity(geometry);
    }

    /**
     * Returns polygons that represent a Constrained Delaunay Triangulation from a geometry.
     * Output is a COLLECTION of polygons, for flag=0 (default flag) or a MULTILINESTRING for flag=1.
     * If the input geometry does not contain any lines, a delaunay triangulation will be computed.
     */
    public static GeometryCollection constrainedDelaunay(Geometry a, int flag) {
        try {
            return org.h2gis.functions.spatial.mesh.ST_ConstrainedDelaunay.createCDT(a, flag);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_ConstrainedDelaunay failed");
        }
    }

    /**
     * Returns true if no point in geometry B is outside geometry A.
     */
    public static Boolean covers(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.predicates.ST_Covers.covers(a, b);
    }

    /**
     * Returns true if the geometries are withinthe specified distance of one another.
     */
    public static Boolean dWithin(Geometry a, Geometry b, Double distance) {
        return org.h2gis.functions.spatial.predicates.ST_DWithin.isWithinDistance(a, b, distance);
    }

    /**
     * Returns polygons that represent a Delaunay Triangulation from a geometry.
     * Output is a COLLECTION of polygons, for flag=0 (default flag) or a MULTILINESTRING for flag=1
     */
    public static GeometryCollection delaunay(Geometry geometry, int flag) {
        try {
            return org.h2gis.functions.spatial.mesh.ST_Delaunay.createDT(geometry, flag);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_Delaunay failed");
        }
    }

    /**
     * Densifies a geometry using the given distance tolerance
     */
    public static Geometry densify(Geometry geometry, double tolerance) {
        return org.h2gis.functions.spatial.edit.ST_Densify.densify(geometry, tolerance);
    }

    /**
     * Expands a geometry's envelope by the given delta X and delta Y.
     * Both positive and negative distances are supported.
     */
    public static Geometry expand(Geometry geometry, double deltaX, double deltaY) {
        return org.h2gis.functions.spatial.create.ST_Expand.expand(geometry, deltaX, deltaY);
    }

    /**
     * ST_Extrude takes a LINESTRING or POLYGON as input
     * and extends it to a 3D representation, returning a geometry collection
     * containing floor, ceiling and wall geometries.
     * Note: the NaN z value of the input geometry are replaced by a zero.
     */
    public static Geometry extrude(Geometry geometry, double height, int flag) {
        try {
            return org.h2gis.functions.spatial.create.ST_Extrude.extrudeGeometry(geometry, height, flag);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_Extrude failed");
        }
    }

    /**
     * Computes the furthest coordinate(s) contained in the given geometry starting from the given point, using the 2D distance.
     */
    public static Geometry furthestCoordinate(Point point, Geometry geometry) {
        return org.h2gis.functions.spatial.distance.ST_FurthestCoordinate.getFurthestCoordinate(point, geometry);
    }

    /**
     * Returns the given geometry's holes as a GeometryCollection.
     */
    public static GeometryCollection holes(Geometry geometry) {
        try {
            return org.h2gis.functions.spatial.convert.ST_Holes.getHoles(geometry);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_Holes failed");
        }
    }

    /**
     * Interpolate the z values of a linestring or multilinestring based on
     * the start and the end z values. If the z values are equal to NaN return the
     * input geometry.
     */
    public static Geometry interpolate3DLine(Geometry geometry) {
        return org.h2gis.functions.spatial.edit.ST_Interpolate3DLine.interpolateLine(geometry);
    }

    /**
     * Returns true if the given geometry is a rectangle.
     */
    public static Boolean isRectangle(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_IsRectangle.isRectangle(geometry);
    }

    /**
     * Returns true if the given geometry is valid.
     */
    public static Boolean isValid(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_IsValid.isValid(geometry);
    }

    /**
     * Returns a MULTIPOINT containing points along the line segments of the given geometry matching the specified segment length fraction
     * and offset distance. A positive offset places the point to the left of the segment (with the ordering given by Coordinate traversal);
     * a negative offset to the right. For areal elements, only exterior rings are supported.
     */
    public static MultiPoint locateAlong(Geometry geometry, double segmentLengthFraction, double offsetDistance) {
        return org.h2gis.functions.spatial.distance.ST_LocateAlong.locateAlong(geometry, segmentLengthFraction, offsetDistance);
    }

    /**
     * Constructs an elliptical POLYGON with the given width and height centered at the given point. Each ellipse contains 100 line segments.
     */
    public static Polygon makeEllipse(Point point, double width, double height) {
        try {
            return org.h2gis.functions.spatial.create.ST_MakeEllipse.makeEllipse(point, width, height);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MakeEllipse failed");
        }
    }

    /**
     * Creates a rectangular POLYGON formed from the given x and y minima.
     * The user may specify an SRID; if no SRID is specified the unknown
     * spatial reference system is assumed.
     */
    public static Polygon makeEnvelope(double xmin, double ymin, double xmax, double ymax, int srid) {
        return org.h2gis.functions.spatial.create.ST_MakeEnvelope.makeEnvelope(xmin, ymin, xmax, ymax, srid);
    }

    /**
     * Constructs a LINESTRING from two POINT geometries.
     */
    public static LineString makeLine(GeometryCollection points) {
        try {
            return org.h2gis.functions.spatial.create.ST_MakeLine.createLine(points);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MakeLine failed");
        }
    }

    /**
     * Constructs POINT from two or three doubles
     */
    public static Point makePoint(double x, double y, double z) {
        try {
            return org.h2gis.functions.spatial.create.ST_MakePoint.createPoint(x, y, z);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MakePoint failed");
        }
    }

    /**
     * Gets the minimum rectangular POLYGON which encloses the input geometry.
     */
    public static Geometry minimumRectangle(Geometry geometry) {
        return org.h2gis.functions.spatial.create.ST_MinimumRectangle.computeMinimumRectangle(geometry);
    }

    /**
     * This function do a multiplication with the z value of (each vertex of) the
     * geometric parameter to the corresponding value given by a field.
     */
    public static Geometry multiplyZ(Geometry geometry, double z) {
        try {
            return org.h2gis.functions.spatial.edit.ST_MultiplyZ.multiplyZ(geometry, z);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MultiplyZ failed");
        }
    }

    /**
     * Converts this Geometry to normal form (canonical form).
     */
    public static Geometry normalize(Geometry geometry) {
        return org.h2gis.functions.spatial.edit.ST_Normalize.normalize(geometry);
    }

    /**
     * Computes the octogonal envelope of a geometry
     */
    public static Geometry octogonalEnvelope(Geometry geometry) {
        return org.h2gis.functions.spatial.create.ST_OctogonalEnvelope.computeOctogonalEnvelope(geometry);
    }

    /**
     * Polygonizes a set of Geometry which contain linework that represents the edges of a planar graph
     */
    public static Geometry polygonize(Geometry geometry) {
        return org.h2gis.functions.spatial.topology.ST_Polygonize.polygonize(geometry);
    }

    /**
     * Reduce the geometry precision. Decimal_Place is the number of decimals to keep.
     */
    public static Geometry precisionReducer(Geometry geometry, int nbDec) {
        try {
            return org.h2gis.functions.spatial.generalize.ST_PrecisionReducer.precisionReducer(geometry, nbDec);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_PrecisionReducer failed");
        }
    }

    /**
     * Remove all holes in a polygon or a multipolygon.
     * If the geometry doesn't contain any hole return the input geometry.
     * If the input geometry is not a polygon or multipolygon return null.
     */
    public static Geometry removeHoles(Geometry geometry) {
        return org.h2gis.functions.spatial.edit.ST_RemoveHoles.removeHoles(geometry);
    }

    /**
     * Remove all points on a geometry that are located within a polygon.
     */
    public static Geometry removePoints(Geometry geometry, Polygon polygon) {
        try {
            return org.h2gis.functions.spatial.edit.ST_RemovePoints.removePoint(geometry, polygon);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_RemovePoints failed");
        }
    }

    /**
     * Returns a version of the given geometry with duplicated points removed.
     */
    public static Geometry removeRepeatedPoints(Geometry geometry) {
        return org.h2gis.functions.spatial.edit.ST_RemoveRepeatedPoints.removeRepeatedPoints(geometry);
    }

    /**
     * Returns the geometry with vertex order reversed.
     */
    public static Geometry reverse(Geometry geometry) {
        return org.h2gis.functions.spatial.edit.ST_Reverse.reverse(geometry);
    }

    /**
     * Returns a 1 dimension geometry with vertex order reversed according
     * the ascending z values.
     * The z of the first point must be lower than the z of the end point.
     * If the z values are equal to NaN return the input geometry.
     */
    public static Geometry reverse3DLine(Geometry geometry, String order) {
        return org.h2gis.functions.spatial.edit.ST_Reverse3DLine.reverse3DLine(geometry, order);
    }

    /**
     * Rotates a geometry by a given angle (inradians) about the geometry's center.
     */
    public static Geometry rotate(Geometry geometry, double theta, double x0, double y0) {
        return org.h2gis.functions.spatial.affine_transformations.ST_Rotate.rotate(geometry, theta, x0, y0);
    }

    /**
     * Scales the given geometry by multiplying the coordinates by the indicated scale factors
     */
    public static Geometry scale(Geometry geometry, double xFactor, double yFactor, double zFactor) {
        return org.h2gis.functions.spatial.affine_transformations.ST_Scale.scale(geometry, xFactor, yFactor, zFactor);
    }

    /**
     * Returns a simplified version of the given geometry using the Douglas-Peuker algorithm.
     */
    public static Geometry simplify(Geometry geometry, double distance) {
        return org.h2gis.functions.spatial.generalize.ST_Simplify.simplify(geometry, distance);
    }

    /**
     * Simplifies a geometry and ensures that the result is a valid geometry.
     */
    public static Geometry simplifyPreserveTopology(Geometry geometry, double distance) {
        return org.h2gis.functions.spatial.generalize.ST_SimplifyPreserveTopology.simplyPreserve(geometry, distance);
    }

    /**
     * Snaps two geometries together with a given tolerance
     */
    public static Geometry snap(Geometry a, Geometry b, double distance) {
        return org.h2gis.functions.spatial.snap.ST_Snap.snap(a, b, distance);
    }

    /**
     * Returns a collection of geometries resulting by splitting a geometry.
     * Supported operations are : - split a polygon or a multipolygon by a linestring,
     * - split a linestring or a multilinestring by a linestring,
     * - split a linestring or a multilinestring by a point. At this stage a double tolerance
     * can be used to snap the point.
     */
    public static Geometry split(Geometry a, Geometry b) {
        try {
            return org.h2gis.functions.spatial.split.ST_Split.split(a, b);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_Split failed");
        }
    }

    /**
     * Constructs a MultiLineString from the given geometry's coordinates.
     */
    public static MultiLineString toMultiLine(Geometry geometry) {
        try {
            return org.h2gis.functions.spatial.convert.ST_ToMultiLine.createMultiLineString(geometry);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_ToMultiLine failed");
        }
    }

    /**
     * Constructs a MultiPoint from the given geometry's coordinates.
     */
    public static MultiPoint toMultiPoint(Geometry geometry) {
        return org.h2gis.functions.spatial.convert.ST_ToMultiPoint.createMultiPoint(geometry);
    }

    /**
     * Converts a geometry into a set of distinct segments stored in a MultiLineString.
     */
    public static void toMultiSegments(LineString lineString, List<LineString> result) {
        try {
            org.h2gis.functions.spatial.convert.ST_ToMultiSegments.createSegments(lineString, result);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_ToMultiSegments failed");
        }
    }

    /**
     * Translates a geometry using X, Y (and possibly Z) offsets.
     */
    public static Geometry translate(Geometry geometry, double x, double y, double z) {
        return org.h2gis.functions.spatial.affine_transformations.ST_Translate.translate(geometry, x, y, z);
    }

    /**
     * Compute the aspect of steepest downhill slope for a triangle
     * . The aspect value is expressed in degrees compared to the north direction.
     */
    public static Double triangleAspect(Geometry geometry) {
        try {
            return org.h2gis.functions.spatial.topography.ST_TriangleAspect.computeAspect(geometry);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_TriangleAspect failed");
        }
    }

    /**
     * Compute the steepest vector director for a triangle
     * and represent it as a linestring
     */
    public static LineString triangleDirection(Geometry geometry) {
        try {
            return org.h2gis.functions.spatial.topography.ST_TriangleDirection.computeDirection(geometry);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_TriangleDirection failed");
        }
    }

    /**
     * Compute the slope of a triangle expressed in percents.
     */
    public static Double triangleSlope(Geometry geometry) {
        try {
            return org.h2gis.functions.spatial.topography.ST_TriangleSlope.computeSlope(geometry);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_TriangleSlope failed");
        }
    }

    /**
     * This function replace the z value of (each vertex of) the
     * geometric parameter to the corresponding value given by a field.
     * The first argument is used to replace all existing z values.
     * The second argument is a int value.
     * Set 1 to replace all z values.
     * Set 2 to replace all z values excepted the NaN values.
     * Set 3 to replace only the NaN z values.
     */
    public static Geometry updateZ(Geometry geometry, double z, int updateCondition) {
        try {
            return org.h2gis.functions.spatial.edit.ST_UpdateZ.updateZ(geometry, z, updateCondition);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_UpdateZ failed");
        }
    }

    /**
     * Returns the maximal x-value of the given geometry.
     */
    public static Double xMax(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_XMax.getMaxX(geometry);
    }

    /**
     * Returns the minimal x-value of the given geometry.
     */
    public static Double xMin(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_XMin.getMinX(geometry);
    }

    /**
     * Returns the maximal y-value of the given geometry.
     */
    public static Double yMax(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_YMax.getMaxY(geometry);
    }

    /**
     * Returns the minimal y-value of the given geometry.
     */
    public static Double yMin(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_YMin.getMinY(geometry);
    }

    /**
     * Returns the maximal z-value of the given geometry.
     */
    public static Double zMax(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_ZMax.getMaxZ(geometry);
    }

    /**
     * Returns the minimal z-value of the given geometry.
     */
    public static Double zMin(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_ZMin.getMinZ(geometry);
    }

    /**
     * Replace the start and end z values of a linestring or multilinestring.
     * By default the other z values are interpolated according the length of the line.
     * Set false if you want to update only the start and end z values.
     */
    public static Geometry zUpdateLineExtremities(Geometry geometry, double startZ, double endZ, boolean interpolate) {
        return org.h2gis.functions.spatial.edit.ST_ZUpdateLineExtremities.updateZExtremities(geometry, startZ, endZ, interpolate);
    }

    /**
     * Compute the minimum diameter for a given geometrywhich is a linestring.
     */
    public static LineString minimumDiameter(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_MinimumDiameter.minimumDiameter(geometry);
    }

    /**
     * Compute a ring buffer around a geometry.
     * Avalaible arguments are :
     * (1) the geometry, (2) the size of each ring,  (3) the number of rings, (4) optional - the end cap style (square, round) Default is round
     * a list of blank-separated key=value pairs (string case) iso used t manage line style parameters.
     * Please read the ST_Buffer documention.
     * (5) optional - createHole True if you want to keep only difference between buffers Default is true.
     * Note : Holes are not supported by this function.
     */
    public static Geometry ringBuffer(Geometry geometry, double bufferDistance, int numBuffer, String parameters) {
        try {
            return org.h2gis.functions.spatial.create.ST_RingBuffer.ringBuffer(geometry, bufferDistance, numBuffer, parameters);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_RingBuffer failed");
        }
    }

    /**
     * Forces the geometries into a "2-dimensional mode"
     * so that all output representations will only have the X and Y coordinates.
     */
    public static Geometry force2D(Geometry geometry) {
        return org.h2gis.functions.spatial.convert.ST_Force2D.force2D(geometry);
    }

    /**
     * Forces the geometries into XYZ mode. This is an alias for ST_Force_3DZ.
     * If a geometry has no Z component, then a 0 Z coordinate is tacked on.
     */
    public static Geometry force3D(Geometry geometry) {
        return org.h2gis.functions.spatial.convert.ST_Force3D.force3D(geometry);
    }

    /**
     * Returns the azimuth of the segment defined by the given Point geometries,
     * or Null if the two points are coincident. Return value is in radians.
     * Angle is computed clockwise from the north equals to 0.
     */
    public static Double azimuth(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.trigonometry.ST_Azimuth.azimuth(a, b);
    }

    /**
     * Creates a Polygon formed by the given shell and optionally holes.
     * Input geometries must be closed Linestrings
     */
    public static Polygon makePolygon(Geometry shell, Geometry[] holes) {
        try {
            return org.h2gis.functions.spatial.create.ST_MakePolygon.makePolygon(shell, holes);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_MakePolygon failed");
        }
    }

    /**
     * Returns text stating if a geometry is valid or not and if not valid, a reason why.
     * The second argument is optional. It can have the following values (0 or 1)
     * 1 = It will validate inverted shells and exverted holes according the ESRI SDE model.
     * 0 = It will based on the OGC geometry model.
     */
    public static String isValidReason(Geometry geometry, int flag) {
        return org.h2gis.functions.spatial.properties.ST_IsValidReason.isValidReason(geometry, flag);
    }

    /**
     * Returns a valid_detail as an array of objects
     * [0] = isvalid,[1] = reason, [2] = error locationThe second argument is optional. It can have the following values (0 or 1)
     * 1 = It will validate inverted shells and exverted holes according the ESRI SDE model.
     * 0 = It will based on the OGC geometry model.
     */
    public static Object[] isValidDetail(Geometry geometry, int flag) {
        return org.h2gis.functions.spatial.properties.ST_IsValidDetail.isValidDetail(geometry, flag);
    }

    /**
     * Split an input geometry by another geometry.
     * This function uses a more robust intersection algorithm than the ST_Split function.
     * It computes the intersections between the line segments of the input geometries.A collection of LineString is returned.
     */
    public static Geometry lineIntersector(Geometry a, Geometry b) {
        try {
            return org.h2gis.functions.spatial.split.ST_LineIntersector.lineIntersector(a, b);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_LineIntersector failed");
        }
    }

    /**
     * Return an offset line or collection of lines at a given distance and side from an input geometry.
     * The optional third parameter can either specify number of segments used
     * to approximate a quarter circle (integer case, defaults to 8)
     * or a list of blank-separated key=value pairs (string case) to manage line style parameters :
     * 'quad_segs=8' endcap=round|flat|square' 'join=round|mitre|bevel' 'mitre_limit=5'
     */
    public static Geometry offSetCurve(Geometry geometry, double offset) {
        return org.h2gis.functions.spatial.buffer.ST_OffSetCurve.offsetCurve(geometry, offset);
    }

    /**
     * Extract an OSM XML file from the OSM api server using a the bounding box of a given geometry.
     * A path must be set to specified where the OSM file will be stored.
     */
    public static void oSMDownloader(Geometry area, String fileName) {
        try {
            org.h2gis.functions.io.osm.ST_OSMDownloader.downloadData(area, fileName);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_OSMDownloader failed");
        }
    }

    /**
     * Projet a point along a linestring. If the point projected is out of line the first or last point on the line will be returned otherwise the input point.
     */
    public static Point projectPoint(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.distance.ST_ProjectPoint.projectPoint(a, b);
    }

    /**
     * Given a (multi)geometry, returns a (multi)geometry consisting only of elements of the specified dimension.
     * Dimension numbers are 1 == POINT, 2 == LINESTRING, 3 == POLYGON
     */
    public static Geometry collectionExtract(Geometry geometry, int dimension) {
        try {
            return org.h2gis.functions.spatial.edit.ST_CollectionExtract.collectionExtract(geometry, dimension);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_CollectionExtract failed");
        }
    }

    /**
     * Return a buffer at a given distance on only one side of each input lines of the geometry.
     * The optional third parameter can either specify number of segments used
     * to approximate a quarter circle (integer case, defaults to 8)
     * or a list of blank-separated key=value pairs (string case) to manage line style parameters :
     * 'quad_segs=8' 'join=round|mitre|bevel' 'mitre_limit=5'
     * The end cap style for single-sided buffers is always ignored, and forced to the equivalent of flat.
     */
    public static Geometry sideBuffer(Geometry geometry, double distance, String parameters) {
        return org.h2gis.functions.spatial.buffer.ST_SideBuffer.singleSideBuffer(geometry, distance, parameters);
    }

    /**
     * Return a ring buffer at a given distance on only one side of each input lines of the geometry.
     * Avalaible arguments are :
     * (1) the geometry, (2) the size of each ring,  (3) the number of rings, (4) optional -
     * a list of blank-separated key=value pairs (string case) iso used t manage line style parameters.
     * The end cap style for single-sided buffers is always ignored, and forced to the equivalent of flat.
     * Please read the ST_Buffer documention.
     * (5) optional - createHole True if you want to keep only difference between buffers Default is true.
     * Note : Holes are not supported by this function.
     */
    public static Geometry ringSideBuffer(Geometry geometry, double bufferDistance, int numBuffer, String parameters) {
        try {
            return org.h2gis.functions.spatial.buffer.ST_RingSideBuffer.ringSideBuffer(geometry, bufferDistance, numBuffer, parameters);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_RingSideBuffer failed");
        }
    }

    /**
     * Return the sun position (horizontal coordinate system) as a Point where :
     * x = sun azimuth in radians (direction along the horizon, measured from north to
     * east).
     * y = sun altitude above the horizon in radians, e.g. 0 at the
     * horizon and PI/2 at the zenith.
     */
    public static Geometry sunPosition(Geometry point, Date date) {
        try {
            return org.h2gis.functions.spatial.earth.ST_SunPosition.sunPosition(point, date);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_SunPosition failed");
        }
    }

    /**
     * This function computes the shadow footprint as a polygon(s) for a LINE and a POLYGON
     * or LINE for a POINT.Avalaible arguments are :
     * (1) The geometry.(2 and 3) The position of the sun is specified with two parameters in radians : azimuth and altitude.
     * (4) The height value is used to extrude the facades of geometry.
     * (5) Optional parameter to unified or not the shadow polygons. True is the default value.
     * Note 1: The z of the output geometry is set to 0.
     * Note 2: The azimuth is a direction along the horizon, measured from north to east.
     * The altitude is expressed above the horizon in radians, e.g. 0 at the horizon and PI/2 at the zenith.
     * The users can set the azimut and the altitude using a point see ST_SunPosition function,
     * the folowing signature must be used ST_GeometryShadow(INPUT_GEOM,ST_SUNPosition(), HEIGHT).
     */
    public static Geometry geometryShadow(Geometry geometry, double azimuth, double altitude, double height) {
        return org.h2gis.functions.spatial.earth.ST_GeometryShadow.computeShadow(geometry, azimuth, altitude, height);
    }

    /**
     * Construct a voronoi diagram from a delaunay triangulation or a set of points.
     * ST_VORONOI(THE_GEOM MULTIPOLYGON)
     * ST_VORONOI(THE_GEOM MULTIPOLYGON,OUT_DIMENSION INTEGER)
     * ST_VORONOI(THE_GEOM MULTIPOLYGON,OUT_DIMENSION INTEGER,ENVELOPE POLYGON)
     * ST_VORONOI(THE_GEOM MULTIPOINTS)
     * ST_VORONOI(THE_GEOM MULTIPOINTS,OUT_DIMENSION INTEGER)
     * ST_VORONOI(THE_GEOM MULTIPOINTS,OUT_DIMENSION INTEGER,ENVELOPE POLYGON)
     * Ex:
     * SELECT ST_VORONOI(ST_DELAUNAY('MULTIPOINT(2 2 0,6 3 0,4 7 0,2 8 0,1 6 0,3 5 0)')) the_geom;
     * SELECT ST_VORONOI(ST_DELAUNAY('MULTIPOINT(2 2 0,6 3 0,4 7 0,2 8 0,1 6 0,3 5 0)'), 1)
     * SELECT ST_VORONOI(ST_DELAUNAY('MULTIPOINT(2 2 0,6 3 0,4 7 0,2 8 0,1 6 0,3 5 0)'), 1, ST_EXPAND('POINT(3 5)', 10, 10))
     */
    public static GeometryCollection voronoi(Geometry geomCollection, int outputDimension) {
        try {
            return org.h2gis.functions.spatial.mesh.ST_Voronoi.voronoi(geomCollection, outputDimension);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_Voronoi failed");
        }
    }

    /**
     * Return the tessellation of a (multi)polygon surface with adaptive triangles
     * Ex:
     * ```sql
     * SELECT ST_TESSELLATE('POLYGON ((-6 -2, -8 2, 0 8, -8 -7, -10 -1, -6 -2))') the_geom```
     */
    public static MultiPolygon tessellate(Geometry geometry) {
        try {
            return org.h2gis.functions.spatial.mesh.ST_Tessellate.tessellate(geometry);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_Tessellate failed");
        }
    }

    /**
     * Merges a collection of LineString elements in order to make create a new collection of maximal-length linestrings. If you provide something else than (multi)linestrings it returns an empty multilinestring
     */
    public static Geometry lineMerge(Geometry geometry) {
        try {
            return org.h2gis.functions.spatial.aggregate.ST_LineMerge.merge(geometry);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_LineMerge failed");
        }
    }

    /**
     * Returns a version of the given geometry with X and Y axis flipped. Useful for people who have built
     * latitude/longitude features and need to fix them.
     */
    public static Geometry flipCoordinates(Geometry geometry) {
        return org.h2gis.functions.spatial.edit.ST_FlipCoordinates.flipCoordinates(geometry);
    }

    /**
     * Returns the 2-dimensional largest distance between two geometries in projected units.
     * If the geometry 1 and geometry 2 is the same geometry the function will
     * return the distance between the two vertices most far from each other in that geometry.
     */
    public static Double maxDistance(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.distance.ST_MaxDistance.maxDistance(a, b);
    }

    /**
     * Returns the 2-dimensional longest line between the points of two geometries.If the geometry 1 and geometry 2 is the same geometry the function will
     * return the longest line between the two vertices most far from each other in that geometry.
     */
    public static Geometry longestLine(Geometry a, Geometry b) {
        return org.h2gis.functions.spatial.distance.ST_LongestLine.longestLine(a, b);
    }

    /**
     * Returns the length measurement of the boundary of a Polygon or a MultiPolygon.
     * Distance units are those of the geometry spatial reference system.
     */
    public static Double perimeter(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_Perimeter.perimeter(geometry);
    }

    /**
     * Returns the 3D length measurement of the boundary of a Polygon or a MultiPolygon.
     * Note : For 2D geometries, returns the 2D length.
     */
    public static Double st3Dperimeter(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_3DPerimeter.st3Dperimeter(geometry);
    }

    /**
     * Compute the 3D area of a polygon or a multipolygon derived from a 3D triangular decomposition.
     * Distance units are those of the geometry spatial reference system.
     */
    public static Double st3darea(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_3DArea.st3darea(geometry);
    }

    /**
     * Convert an input GML representation of geometry to a geometry.
     * An optional argument is used to set a SRID.
     * This function supports only GML 2.1.2
     */
    public static Geometry geomFromGML(String gmlFile, int srid) {
        try {
            return org.h2gis.functions.spatial.convert.ST_GeomFromGML.toGeometry(gmlFile, srid);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_GeomFromGML failed");
        }
    }

    /**
     * Convert a geojson representation of a geometry to a geometry object.
     */
    public static Geometry geomFromGeoJSON(String geojson) {
        try {
            return org.h2gis.functions.io.geojson.ST_GeomFromGeoJSON.geomFromGeoJSON(geojson);
        } catch (Exception e) {
            throw Errors.rethrow(e, "Geo function ST_GeomFromGeoJSON failed");
        }
    }

    /**
     * Store a geometry as a GML representation.
     * It supports OGC GML standard 2.1.2
     */
    public static String asGML(Geometry geometry) {
        return org.h2gis.functions.spatial.convert.ST_AsGML.toGML(geometry);
    }

    /**
     * Return the number of points (vertexes) in a geometry.
     */
    public static Integer nPoints(Geometry geometry) {
        return org.h2gis.functions.spatial.properties.ST_NPoints.getNPoints(geometry);
    }

}
