package com.alice.emily.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by lianhao on 2016/12/8.
 */
public class URLUtils {

    public static final String CLASSPATH_URL_PREFIX = "classpath:"; // Pseudo URL prefix for loading from the class path: "classpath:"
    public static final String FILE_URL_PREFIX = "file:";           // URL prefix for loading from the file system: "file:"
    public static final String JAR_URL_PREFIX = "jar:";             // URL prefix for loading from a jar file: "jar:"
    public static final String WAR_URL_PREFIX = "war:";             // URL prefix for loading from a war file on Tomcat: "war:"

    public static final String URL_PROTOCOL_FILE = "file";          // URL protocol for a file in the file system: "file"
    public static final String URL_PROTOCOL_JAR = "jar";            //  URL protocol for an entry from a jar file: "jar"
    public static final String URL_PROTOCOL_ZIP = "zip";            // URL protocol for an entry from a zip file: "zip"
    public static final String URL_PROTOCOL_WSJAR = "wsjar";        // URL protocol for an entry from a WebSphere jar file: "wsjar"
    public static final String URL_PROTOCOL_VFSZIP = "vfszip";      // URL protocol for an entry from a JBoss jar file: "vfszip"
    public static final String URL_PROTOCOL_VFSFILE = "vfsfile";    // URL protocol for a JBoss file system resource: "vfsfile"
    public static final String URL_PROTOCOL_VFS = "vfs";            // URL protocol for a general JBoss VFS resource: "vfs"

    public static final String JAR_FILE_EXTENSION = ".jar";         // File extension for a regular jar file: ".jar"
    public static final String JAR_URL_SEPARATOR = "!/";            // Separator between JAR URL and file path within the JAR: "!/"
    public static final String WAR_URL_SEPARATOR = "*/";            // Special separator between WAR URL and jar part on Tomcat

    private URLUtils() { }

    public static String getString(String url) throws IOException {
        Preconditions.checkArgument(isUrl(url), "%s is not a valid url", url);
        return IOUtils.toString(getURL(url), StandardCharsets.UTF_8);
    }

    public static Properties getProperties(String url) throws IOException {
        Preconditions.checkArgument(isUrl(url), "%s is not a valid url", url);
        Properties properties = new Properties();
        properties.load(getURL(url).openStream());
        return properties;
    }

    public static String getClasspathURL(String classpath) {
        return classpath.startsWith(CLASSPATH_URL_PREFIX) ? classpath : CLASSPATH_URL_PREFIX + classpath;
    }

    public static String getFileURL(String path) {
        return path.startsWith(FILE_URL_PREFIX) ? path : FILE_URL_PREFIX + path;
    }

    public static String getURL(File dir, String file) {
        Preconditions.checkArgument(dir.isDirectory(), "File %s is not a directory", dir);
        try {
            return new File(dir, file).toURI().toURL().toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Resolve the given resource location to a {@code java.net.URL}.
     * <p>Does not check whether the URL actually exists; simply returns
     * the URL that the given location would correspond to.
     *
     * @param resourceLocation the resource location to getBean: either a
     *                         "classpath:" pseudo URL, a "file:" URL, or a plain file path
     * @return a corresponding URL object
     * @throws IOException if the resource cannot be resolved to a URL
     */
    public static URL getURL(String resourceLocation) throws IOException {
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                String description = "class path resource [" + path + "]";
                throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
            }
            return url;
        }
        try {
            // try URL
            return new URL(resourceLocation);
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new FileNotFoundException("URLUtils location [" + resourceLocation + "] is neither a URL not a well-formed file path");
            }
        }
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUrl the resource URL to getBean
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     */
    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    /**
     * Resolve the given resource URL to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUrl the resource URL to getBean
     * @param description a description of the original resource that
     *                    the URL was created for (for example, a class path location)
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     */
    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(
                    description + " cannot be resolved to absolute file path " +
                            "because it does not reside in the file system: " + resourceUrl);
        }
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException ex) {
            // Fallback for URLUtils that are not valid URIs (should hardly ever happen).
            return new File(resourceUrl.getFile());
        }
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUri the resource URI to getBean
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     */
    public static File getFile(URI resourceUri) throws FileNotFoundException {
        return getFile(resourceUri, "URI");
    }

    /**
     * Resolve the given resource URI to a {@code java.io.File},
     * i.e. to a file in the file system.
     *
     * @param resourceUri the resource URI to getBean
     * @param description a description of the original resource that
     *                    the URI was created for (for example, a class path location)
     * @return a corresponding File object
     * @throws FileNotFoundException if the URL cannot be resolved to
     *                               a file in the file system
     */
    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
        if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path " +
                    "because it does not reside in the file system: " + resourceUri);
        }
        return new File(resourceUri.getSchemeSpecificPart());
    }

    /**
     * Return whether the given resource location is a URL:
     * either a special "classpath" pseudo URL or a standard URL.
     *
     * @param resourceLocation the location String to check
     * @return whether the location qualifies as a URL
     * @see #CLASSPATH_URL_PREFIX
     * @see URL
     */
    public static boolean isUrl(String resourceLocation) {
        if (resourceLocation == null) {
            return false;
        }
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return true;
        }
        try {
            new URL(resourceLocation);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    /**
     * Determine whether the given URL points to a resource in the file system,
     * that is, has protocol "file", "vfsfile" or "vfs".
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a file system URL
     */
    public static boolean isFileURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) ||
                URL_PROTOCOL_VFS.equals(protocol));
    }

    /**
     * Determine whether the given URL points to a resource in a jar file,
     * that is, has protocol "jar", "zip", "vfszip" or "wsjar".
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a JAR URL
     */
    public static boolean isJarURL(URL url) {
        String protocol = url.getProtocol();
        return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol) ||
                URL_PROTOCOL_VFSZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol));
    }

    /**
     * Determine whether the given URL points to a jar file itself,
     * that is, has protocol "file" and ends with the ".jar" extension.
     *
     * @param url the URL to check
     * @return whether the URL has been identified as a JAR file URL
     * @since 4.1
     */
    public static boolean isJarFileURL(URL url) {
        return (URL_PROTOCOL_FILE.equals(url.getProtocol()) && url.getPath().toLowerCase().endsWith(JAR_FILE_EXTENSION));
    }

    /**
     * Extract the URL for the actual jar file from the given URL
     * (which may point to a resource in a jar file or to a jar file itself).
     *
     * @param jarUrl the original URL
     * @return the URL for the actual jar file
     * @throws MalformedURLException if no valid jar file URL could be extracted
     */
    public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();
        int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
        if (separatorIndex != -1) {
            String jarFile = urlFile.substring(0, separatorIndex);
            try {
                return new URL(jarFile);
            } catch (MalformedURLException ex) {
                // Probably no protocol in original jar URL, like "jar:C:/mypath/myjar.jar".
                // This usually indicates that the jar file resides in the file system.
                if (!jarFile.startsWith("/")) {
                    jarFile = "/" + jarFile;
                }
                return new URL(FILE_URL_PREFIX + jarFile);
            }
        } else {
            return jarUrl;
        }
    }

    /**
     * Extract the URL for the outermost archive from the given jar/war URL
     * (which may point to a resource in a jar file or to a jar file itself).
     * <p>In the case of a jar file nested within a war file, this will return
     * a URL to the war file since that is the one resolvable in the file system.
     *
     * @param jarUrl the original URL
     * @return the URL for the actual jar file
     * @throws MalformedURLException if no valid jar file URL could be extracted
     * @see #extractJarFileURL(URL)
     * @since 4.1.8
     */
    public static URL extractArchiveURL(URL jarUrl) throws MalformedURLException {
        String urlFile = jarUrl.getFile();

        int endIndex = urlFile.indexOf(WAR_URL_SEPARATOR);
        if (endIndex != -1) {
            // Tomcat's "jar:war:file:...mywar.war*/WEB-INF/lib/myjar.jar!/myentry.txt"
            String warFile = urlFile.substring(0, endIndex);
            int startIndex = warFile.indexOf(WAR_URL_PREFIX);
            if (startIndex != -1) {
                return new URL(warFile.substring(startIndex + WAR_URL_PREFIX.length()));
            }
        }

        // Regular "jar:file:...myjar.jar!/myentry.txt"
        return extractJarFileURL(jarUrl);
    }

    /**
     * Create a URI instance for the given URL,
     * replacing spaces with "%20" URI encoding first.
     * <p>Furthermore, this method works on JDK 1.4 as well,
     * in contrast to the {@code URL.toURI()} method.
     *
     * @param url the URL to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the URL wasn't a valid URI
     * @see URL#toURI()
     */
    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * Create a URI instance for the given location String,
     * replacing spaces with "%20" URI encoding first.
     *
     * @param location the location String to convert into a URI instance
     * @return the URI instance
     * @throws URISyntaxException if the location wasn't a valid URI
     */
    public static URI toURI(String location) throws URISyntaxException {
        return new URI(location.replace(" ", "%20"));
    }

    /**
     * Set the {@link URLConnection#setUseCaches "useCaches"} flag on the
     * given connection, preferring {@code false} but leaving the
     * flag at {@code true} for JNLP based resources.
     *
     * @param con the URLConnection to set the flag on
     */
    public static void useCachesIfNecessary(URLConnection con) {
        con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
    }

}
