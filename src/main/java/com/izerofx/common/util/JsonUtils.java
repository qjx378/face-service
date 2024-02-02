package com.izerofx.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * className: JsonUtils.java<br>
 * description: Jackson工具类<br>
 * createDate: 2022年10月25日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Slf4j
public class JsonUtils {
    // 加载速度太慢了，放在静态代码块中
    private static final JsonMapper mapper;

    /*
      设置一些通用的属性
     */
    static {
        mapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                // 如果json中有新增的字段并且是实体类类中不存在的，不报错
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                // 如果存在未知属性，则忽略不报错
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 允许key没有双引号
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
                // 允许key有单引号
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                // 允许整数以0开头
                .configure(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS, true)
                // 允许字符串中存在回车换行控制符
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
                // 取消默认转换timestamps形式
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
                .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .build();
    }

    private JsonUtils() {
    }

    public static JsonMapper getMapper() {
        return mapper;
    }

    public static String toJSONString(Object obj) {
        return obj != null ? toJSONString(obj, () -> "", false) : "";
    }

    public static String toFormatJSONString(Object obj) {
        return obj != null ? toJSONString(obj, () -> "", true) : "";
    }

    public static String toJSONString(Object obj, Supplier<String> defaultSupplier, boolean format) {
        try {
            if (obj == null) {
                return defaultSupplier.get();
            }
            if (obj instanceof String) {
                return obj.toString();
            }
            if (obj instanceof Number) {
                return obj.toString();
            }
            if (format) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (Throwable e) {
            log.error(String.format("toJSONString %s", obj != null ? obj.toString() : "null"), e);
        }
        return defaultSupplier.get();
    }

    public static <T> T toJavaObject(String value, Class<T> tClass) {
        return StringUtils.isNotBlank(value) ? toJavaObject(value, tClass, () -> null) : null;
    }

    public static <T> T toJavaObject(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObject(toJSONString(obj), tClass, () -> null) : null;
    }

    public static <T> T toJavaObject(String value, Class<T> tClass, Supplier<T> defaultSupplier) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultSupplier.get();
            }
            return mapper.readValue(value, tClass);
        } catch (Throwable e) {
            log.error(String.format("toJavaObject exception: %s %s", value, tClass), e);
        }
        return defaultSupplier.get();
    }

    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass) {
        return StringUtils.isNotBlank(value) ? toJavaObjectList(value, tClass, () -> null) : null;
    }

    public static <T> List<T> toJavaObjectList(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObjectList(toJSONString(obj), tClass, () -> null) : null;
    }

    public static <T> List<T> toJavaObjectList(String value, Class<T> tClass, Supplier<List<T>> defaultSupplier) {
        try {
            if (StringUtils.isBlank(value)) {
                return defaultSupplier.get();
            }
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, tClass);
            return mapper.readValue(value, javaType);
        } catch (Throwable e) {
            log.error(String.format("toJavaObjectList exception %s %s", value, tClass), e);
        }
        return defaultSupplier.get();
    }

    // 简单地直接用json复制或者转换(Cloneable)
    public static <T> T jsonCopy(Object obj, Class<T> tClass) {
        return obj != null ? toJavaObject(toJSONString(obj), tClass) : null;
    }

    public static Map<String, Object> toMap(String value) {
        return StringUtils.isNotBlank(value) ? toMap(value, () -> null) : null;
    }

    public static Map<String, Object> toMap(Object value) {
        return value != null ? toMap(value, () -> null) : null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object value, Supplier<Map<String, Object>> defaultSupplier) {
        if (value == null) {
            return defaultSupplier.get();
        }
        try {
            if (value instanceof Map) {
                return (Map<String, Object>) value;
            }
        } catch (Exception e) {
            log.info("fail to convert" + toJSONString(value), e);
        }
        return toMap(toJSONString(value), defaultSupplier);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String value, Supplier<Map<String, Object>> defaultSupplier) {
        if (StringUtils.isBlank(value)) {
            return defaultSupplier.get();
        }
        try {
            return toJavaObject(value, LinkedHashMap.class);
        } catch (Exception e) {
            log.error(String.format("toMap exception %s", value), e);
        }
        return defaultSupplier.get();
    }

    public static List<Map<String, Object>> toListMap(String value, Supplier<List<Map<String, Object>>> defaultSupplier) {
        if (StringUtils.isBlank(value)) {
            return defaultSupplier.get();
        }
        try {
            CollectionLikeType type = mapper.getTypeFactory().constructCollectionLikeType(List.class, Map.class);
            return mapper.readValue(value, type);
        } catch (Exception e) {
            log.error(String.format("toListMap exception %s", value), e);
        }
        return defaultSupplier.get();
    }

    public static List<Object> toList(String value) {
        return StringUtils.isNotBlank(value) ? toList(value, () -> null) : null;
    }

    public static List<Object> toList(Object value) {
        return value != null ? toList(value, () -> null) : null;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> toList(String value, Supplier<List<Object>> defaultSuppler) {
        if (StringUtils.isBlank(value)) {
            return defaultSuppler.get();
        }
        try {
            return toJavaObject(value, List.class);
        } catch (Exception e) {
            log.error("toList exception\n" + value, e);
        }
        return defaultSuppler.get();
    }

    @SuppressWarnings("unchecked")
    public static List<Object> toList(Object value, Supplier<List<Object>> defaultSuppler) {
        if (value == null) {
            return defaultSuppler.get();
        }
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return toList(toJSONString(value), defaultSuppler);
    }

    public static long getLong(Map<String, Object> map, String key) {
        if (map == null || map.size() == 0) {
            return 0L;
        }
        String valueStr = String.valueOf(map.get(key));
        if (StringUtils.isBlank(valueStr) || !StringUtils.isNumeric(valueStr)) {
            return 0L;
        }
        return Long.parseLong(valueStr);
    }

    public static int getInt(Map<String, Object> map, String key) {
        if (map == null || map.size() == 0) {
            return 0;
        }
        String valueStr = String.valueOf(map.get(key));
        if (StringUtils.isBlank(valueStr) || !StringUtils.isNumeric(valueStr)) {
            return 0;
        }
        return Integer.parseInt(valueStr);
    }

    public static String getString(Map<String, Object> map, String key) {
        if (map == null || map.size() == 0) {
            return "";
        }
        return String.valueOf(map.get(key));
    }

    /**
     * 获取指定的键值
     *
     * @param json
     * @param key
     * @return
     */
    public static String getValue(String json, String key) {
        String result = null;
        JsonNode jsonNode = toJsonNode(json);
        if (jsonNode != null) {
            try {
                result = jsonNode.findValue(key).asText();
            } catch (Exception e) {
            }
        }
        return result == null ? "" : result;
    }

    /**
     * JSON字符串转JSON树
     *
     * @param json
     * @return
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * map转JSON树
     *
     * @param json
     * @return
     */
    public static JsonNode toJsonNode(Map<?, ?> json) {
        return mapper.convertValue(json, JsonNode.class);
    }

    /**
     * 解析jsonNode的值
     *
     * @param node
     * @param attrs "aa.bb.cc"
     * @return
     */
    public static String getJsonNodeValue(JsonNode node, String attrs) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                return node.get(attrs).asText();
            }
            return "";
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            return getJsonNodeValue(node.get(s1), s2);
        }
    }
}
