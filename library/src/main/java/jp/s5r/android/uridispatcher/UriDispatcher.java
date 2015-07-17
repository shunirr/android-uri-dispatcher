package jp.s5r.android.uridispatcher;

import android.content.Intent;
import android.os.Bundle;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jp.s5r.android.uridispatcher.annotation.MatchUri;
import jp.s5r.android.uridispatcher.annotation.PathParam;
import jp.s5r.android.uridispatcher.annotation.QueryParam;

public final class UriDispatcher {

    public static void dispatch(Object target, Intent intent) {
        dispatch(target, URI.create(intent.getData().toString()), intent.getExtras());
    }

    public static void dispatch(Object target, URI uri) {
        dispatch(target, uri, null);
    }

    private static void dispatch(Object target, URI uri, Bundle params) {
        for (Method method : getMethodAssignedSchemeUrl(target)) {
            try {
                Annotation[] annotations = method.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (!annotation.annotationType().equals(MatchUri.class)) {
                        continue;
                    }

                    MatchUri matchUri = (MatchUri) annotation;
                    CustomUriMatcher matcher = new CustomUriMatcher(matchUri.value(), uri, params);
                    if (matcher.isMatch()) {
                        method.invoke(target, buildParams(method, matcher));
                    }
                }
            } catch (IllegalArgumentException | SecurityException | ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static List<Method> getMethodAssignedSchemeUrl(Object target) {
        ArrayList<Method> result = new ArrayList<>();

        Class clazz = target.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            try {
                Annotation[] annotations = method.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(MatchUri.class)) {
                        // target で SchemeUrl が指定されているメソッド一覧
                        result.add(method);
                        break;
                    }
                }
            } catch (IllegalArgumentException | SecurityException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    private static Object[] buildParams(Method method, CustomUriMatcher handler) {
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 0) {
            return null;
        }

        ArrayList<Object> objectArrayList = new ArrayList<>();

        int i = 0;
        for (Annotation[] annotations : paramAnnotations) {
            Class paramType = paramTypes[i++];
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(QueryParam.class)) {
                    QueryParam queryParam = (QueryParam) annotation;
                    String key = queryParam.value();
                    if (paramType.equals(String.class)) {
                        String value = handler.getParamString(key);
                        objectArrayList.add(value);
                    } else if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
                        Integer value = handler.getParamInteger(key);
                        objectArrayList.add(value);
                    }
                } else if (annotation.annotationType().equals(PathParam.class)) {
                    PathParam pathParam = (PathParam) annotation;
                    String key = pathParam.value();
                    if (paramType.equals(String.class)) {
                        String value = handler.getPathString(key);
                        objectArrayList.add(value);
                    } else if (paramType.equals(int.class) || paramType.equals(Integer.class)) {
                        Integer value = handler.getPathInteger(key);
                        objectArrayList.add(value);
                    }
                }
            }
        }

        if (objectArrayList.size() == 0) {
            return null;
        } else {
            return objectArrayList.toArray();
        }
    }
}
