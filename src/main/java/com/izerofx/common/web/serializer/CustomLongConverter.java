package com.izerofx.common.web.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * className: CustomLongConverter.java<br>
 * description: 自定义long类型转换器<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
public class CustomLongConverter extends StdSerializer<Long> {

    private static final long serialVersionUID = -4989966626382389604L;

    public CustomLongConverter() {
        super(Long.class);
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value.toString().length() > 12) {
            gen.writeString(value.toString());
        } else {
            gen.writeNumber(value);
        }
    }
}
