package com.izerofx.common.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * className: NullArrayJsonSerializer.java<br>
 * description: 空数组json序列化类<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
public class NullArrayJsonSerializer extends JsonSerializer<Object> {

    public static final NullArrayJsonSerializer INSTANCE = new NullArrayJsonSerializer();

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeStartArray();
            gen.writeEndArray();
        } else {
            gen.writeObject(value);
        }

    }

    @Override
    public Class<Object> handledType() {
        return Object.class;

    }

}

