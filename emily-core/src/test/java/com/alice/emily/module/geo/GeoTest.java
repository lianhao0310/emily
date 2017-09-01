package com.palmaplus.euphoria.module.geo;

import com.google.common.base.Strings;
import com.palmaplus.euphoria.utils.Errors;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.lang3.StringUtils;
import org.h2gis.api.Function;
import org.h2gis.api.ScalarFunction;
import org.h2gis.functions.factory.H2GISFunctions;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by liupin on 2017/1/4.
 */
public class GeoTest {

    @Test
    public void testGenerateGeoFunctions() {
        Set<String> imports = new HashSet<>();
        imports.add(Errors.class.getName());

        StringBuilder classBody = new StringBuilder();
        classBody.append("public class GeoFunctions {\n\n");
        for (Function function : H2GISFunctions.getBuiltInsFunctions()) {
            if (function instanceof ScalarFunction) {
                ScalarFunction scalarFunction = (ScalarFunction) function;
                generateMethod(imports, classBody, scalarFunction);
            }
        }
        classBody.append("}");

        StringBuilder java = new StringBuilder();
        if (!imports.isEmpty()) {
            imports.stream().sorted().forEach(i -> java.append("import ").append(i).append(";\n"));
        }
        java.append("\n").append(classBody);
        System.out.println(java.toString());
    }

    private boolean isIgnoreType(Class<?> type) {
        // skip sql related
        if (type.getName().startsWith("java.sql")) {
            return true;
        }
        // skip map link function
        if (type.getName().contains("MapLink")) {
            return true;
        }
        // skip no use function
        if (type.getName().startsWith("org.h2gis.functions.string")) {
            return true;
        }
        // skip h2 related
        if (type.getName().startsWith("org.h2gis.functions.spatial.type")) {
            return true;
        }
        if (type.getName().startsWith("org.h2.")) {
            return true;
        }

        return false;
    }

    private void addToImports(Set<String> imports, Class<?> type) {
        if (!type.getName().startsWith("java.lang") && !type.isArray() && !type.isPrimitive()) {
            imports.add(type.getName());
        }
    }

    private String determineParameterName(String name, Method method, Parameter parameter) {
        if (parameter.getType() == byte[].class) {
            return "bytes";
        }
        if (parameter.getType() == Point.class) {
            return "point";
        }
        if (parameter.getType() == Geometry.class) {
            if (method.getParameterCount() > 1) {
                Parameter[] parameters = method.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i].getType() == Geometry.class) {
                        if (parameters[i] == parameter) {
                            return "abc".charAt(i) + "";
                        }
                    } else {
                        break;
                    }
                }
            }
            return "geometry";
        }
        if (name.endsWith("FromText")) {
            if (parameter.getType() == String.class) {
                return "wkt";
            }
            if (parameter.getType() == int.class || parameter.getType() == Integer.class) {
                return "srid";
            }
        }
        if (name.endsWith("FromGeoJSON")) {
            if (parameter.getType() == String.class) {
                return "geojson";
            }
        }
        if (name.endsWith("FromWKB")) {
            if (parameter.getType() == int.class || parameter.getType() == Integer.class) {
                return "srid";
            }
        }
        if (name.endsWith("FromGML")) {
            if (parameter.getType() == String.class) {
                return "gmlFile";
            }
            if (parameter.getType() == int.class || parameter.getType() == Integer.class) {
                return "srid";
            }
        }
        if (name.endsWith("N")) {
            if (parameter.getType() == int.class || parameter.getType() == Integer.class) {
                return "n";
            }
        }
        if (name.endsWith("SRID")) {
            if (parameter.getType() == int.class || parameter.getType() == Integer.class) {
                return "n";
            }
        }

        return parameter.getName();
    }

    private void generateMethod(Set<String> imports, StringBuilder sb, ScalarFunction function) {
        String functionRemarks = getStringProperty(function, Function.PROP_REMARKS);
        String functionName = function.getJavaStaticMethod();
        String functionClass = function.getClass().getName();

        if (isIgnoreType(function.getClass())) {
            return;
        }

        Method functionMethod = null;
        for (Method method : function.getClass().getMethods()) {
            if (method.getName().equals(functionName)
                    && Modifier.isStatic(method.getModifiers())
                    && Modifier.isPublic(method.getModifiers())) {
                functionMethod = method;
                break;
            }
        }
        if (functionMethod == null) return;

        // return type
        Class<?> returnType = functionMethod.getReturnType();
        if (isIgnoreType(returnType)) {
            return;
        }
        addToImports(imports, returnType);
        String returnTypeValue = returnType.getSimpleName();

        // method name
        String methodName = functionName;
        if (function.getClass().getSimpleName().startsWith("ST_")) {
            String name = function.getClass().getSimpleName().substring(3);
            if ("1234567890".indexOf(name.charAt(0)) == -1) {
                methodName = StringUtils.uncapitalize(name);
            }
        }

        // parameter
        for (Parameter parameter : functionMethod.getParameters()) {
            Class<?> type = parameter.getType();
            if (isIgnoreType(type)) {
                return;
            }
            addToImports(imports, type);
        }

        String finalMethodName = methodName;
        Method finalFunctionMethod = functionMethod;
        String parameterDeclaration = Arrays.stream(functionMethod.getParameters())
                .map(parameter -> parameter.getType().getSimpleName() + " " + determineParameterName(finalMethodName, finalFunctionMethod, parameter))
                .collect(Collectors.joining(", "));
        String parameterValue = Arrays.stream(functionMethod.getParameters())
                .map(parameter -> determineParameterName(finalMethodName, finalFunctionMethod, parameter))
                .collect(Collectors.joining(", "));
        boolean hasThrowable = functionMethod.getExceptionTypes().length > 0;

        // comments
        if (!Strings.isNullOrEmpty(functionRemarks)) {
            sb.append("    /**\n");
            sb.append("     * ").append(functionRemarks.replace("\n", "\n     * ")).append("\n");
            sb.append("     */\n");
        }

        // method signature
        sb.append("    public static ").append(returnTypeValue).append(" ").append(methodName);
        sb.append("(").append(parameterDeclaration).append(") {\n");

        // method body
        if (hasThrowable) {
            sb.append("        try {\n").append("    ");
        }

        if ("void".equals(returnTypeValue)) {
            sb.append("        ");
        } else {
            sb.append("        return ");
        }
        sb.append(functionClass).append(".").append(functionName)
                .append("(").append(parameterValue).append(");\n");

        if (hasThrowable) {
            sb.append("        } catch (Exception e) {\n");
            sb.append("            throw Errors.rethrow(e, \"Geo function ").append(function.getClass().getSimpleName()).append(" failed\");\n");
            sb.append("        }\n");
        }

        sb.append("    }\n\n");
    }

    private String getStringProperty(Function function, String propertyKey) {
        Object value = function.getProperty(propertyKey);
        return value instanceof String ? (String) value : "";
    }
}
