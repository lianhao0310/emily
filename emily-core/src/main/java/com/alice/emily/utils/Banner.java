package com.alice.emily.utils;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liupin on 2016/12/1.
 */
public class Banner {

    private static String versionKey = "Emily-Build-Version";
    private static String timestampKey = "Emily-Build-Date";

    private Banner() { }

    @SneakyThrows
    public static void show(Logger log, Class<?> clazz) {
        Preconditions.checkNotNull(log, "output log must be provided");
        URL resource = clazz.getResource("/banner.txt");
        if (resource == null) return;

        List<String> lines = Resources.readLines(resource, StandardCharsets.UTF_8);
        int maxLength = 0;
        for (String line : lines) {
            maxLength = Math.max(maxLength, line.length());
            log.info(line);
        }

        String version = version(clazz);
        String date = timestamp(clazz);

        log.info(String.format("%" + (maxLength / 2) + "s: %s", "build-version ", version));
        log.info(String.format("%" + (maxLength / 2) + "s: %s", "build-date    ", date));
        log.info("");
    }

    private static Manifest getManifest(Class<?> clz) {
        String resource = "/" + clz.getName().replace(".", "/") + ".class";
        String fullPath = clz.getResource(resource).toString();
        String archivePath = fullPath.substring(0, fullPath.length() - resource.length());
        if (archivePath.endsWith("\\WEB-INF\\classes") || archivePath.endsWith("/WEB-INF/classes")) {
            archivePath = archivePath.substring(0, archivePath.length() - "/WEB-INF/classes".length()); // Required for wars
        }
        Manifest manifest = new Manifest();
        try (InputStream input = new URL(archivePath + "/META-INF/MANIFEST.MF").openStream()) {
            manifest.read(input);
        } catch (Exception ignore) {
        }
        return manifest;
    }

    public static String version(Class<?> clz) {
        Manifest manifest = getManifest(clz);
        Attributes attributes = manifest.getMainAttributes();
        String version = attributes.getValue(versionKey);

        if (version == null) {
            String path = clz.getProtectionDomain().getCodeSource().getLocation().getPath();
            Pattern pattern = Pattern.compile(".*([\\d.]+(-SNAPSHOT)?)\\.jar$");
            Matcher matcher = pattern.matcher(path);
            if (matcher.matches()) {
                version = matcher.group(1);
            }
        }

        return version == null ? "unknown" : version;
    }

    public static String timestamp(Class<?> clz) {
        Manifest manifest = getManifest(clz);
        Attributes attributes = manifest.getMainAttributes();
        String timestamp = attributes.getValue(timestampKey);
        return timestamp == null ? "unknown" : timestamp;
    }
}
