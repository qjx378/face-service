package com.izerofx.common.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * className: NullStringJsonSerializer.java<br>
 * description: 空字符串JSON序列化类<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
public class NullStringJsonSerializer extends JsonSerializer<String> {

    public static final NullStringJsonSerializer INSTANCE = new NullStringJsonSerializer();

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (null == value) {
            gen.writeString("");
        } else {
            gen.writeString(value);
        }
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }
}

