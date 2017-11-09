package com.alice.emily.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;

/**
 * Created by sifan on 2016/8/24.
 */
@UtilityClass
public class Version {

    public static int compare(String version1, String version2) {
        int[] versions1 = split(version1);
        int[] versions2 = split(version2);

        int max = Math.max(versions1.length, versions2.length);
        for (int i = 0; i < max; i++) {
            int v1 = versions1.length > i ? versions1[i] : 0;
            int v2 = versions2.length > i ? versions2[i] : 0;
            if (v1 > v2) {
                return 1;
            }
            if (v1 < v2) {
                return -1;
            }
        }

        return 0;
    }

    public static int[] split(String version) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(version), "version can not be null or empty");
        String[] versions = version.split("\\.");
        int[] ver = new int[versions.length];
        for (int i = 0; i < versions.length; i++) {
            ver[i] = Integer.valueOf(versions[i]);
        }
        return ver;
    }

}
