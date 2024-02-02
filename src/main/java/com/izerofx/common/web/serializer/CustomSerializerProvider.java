package com.izerofx.common.web.serializer;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

/**
 * className: CustomSerializerProvider.java<br>
 * description: 自定义序列化提供类<br>
 * createDate: 2022年06月19日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 *
 */
public class CustomSerializerProvider extends DefaultSerializerProvider {

    private static final long serialVersionUID = -2237536491398477297L;

    public CustomSerializerProvider() {
        super();

    }

    public CustomSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
        super(src, config, f);
    }

    @Override
    public DefaultSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new CustomSerializerProvider(this, config, jsf);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public JsonSerializer findNullValueSerializer(BeanProperty property) throws JsonMappingException {

        JavaType type = property.getType();

        if (type.getRawClass().equals(String.class)) {
            return NullStringJsonSerializer.INSTANCE;
        }

        if (type.isArrayType() || type.isCollectionLikeType()) {
            return NullArrayJsonSerializer.INSTANCE;
        }

        return super.findNullValueSerializer(property);
    }
}

