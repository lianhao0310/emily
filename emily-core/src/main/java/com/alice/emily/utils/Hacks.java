package com.alice.emily.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Map;

/**
 * Created by liupin on 2017/2/7.
 */
public class Hacks {

    public static void removeCryptographyRestrictions() {
        try {
            new RemoveCryptographyRestrictions().remove();
        } catch (Exception e) {
            throw Errors.rethrow(e, "Remove cryptography restrictions failed");
        }
    }

    private static class RemoveCryptographyRestrictions {

        private void remove() throws Exception {
            Class<?> jceSecurity = getClazz("javax.crypto.JceSecurity");
            Class<?> cryptoPermissions = getClazz("javax.crypto.CryptoPermissions");
            Class<?> cryptoAllPermission = getClazz("javax.crypto.CryptoAllPermission");
            if (jceSecurity == null || cryptoPermissions == null || cryptoAllPermission == null) {
                return;
            }
            setFinalStaticValue(jceSecurity, "isRestricted", false);
            PermissionCollection defaultPolicy = getFieldValue(jceSecurity, "defaultPolicy", null, PermissionCollection.class);
            Map<?, ?> map = getFieldValue(cryptoPermissions, "perms", defaultPolicy, Map.class);
            map.clear();
            Permission permission = getFieldValue(cryptoAllPermission, "INSTANCE", null, Permission.class);
            defaultPolicy.add(permission);
        }

        private Class<?> getClazz(String className) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (Exception ignore) {
            }
            return clazz;
        }

        private void setFinalStaticValue(Class<?> srcClazz, String fieldName, Object newValue) throws Exception {
            Field field = srcClazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, newValue);
        }

        private <T> T getFieldValue(Class<?> srcClazz, String fieldName, Object owner, Class<T> dstClazz) throws Exception {
            Field field = srcClazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return dstClazz.cast(field.get(owner));
        }

    }
}
