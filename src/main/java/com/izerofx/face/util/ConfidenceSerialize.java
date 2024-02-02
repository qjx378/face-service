package com.izerofx.face.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * className: ConfidenceSerialize<br>
 * description: 人脸置信度序列化类<br>
 * createDate: 2022年06月23日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public class ConfidenceSerialize extends JsonSerializer<Float> {

    @Override
    public void serialize(Float value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (!value.isNaN()) {
            //四舍五入，换算为百分比
            gen.writeNumber(new BigDecimal(value).multiply(new BigDecimal(100)).setScale(3, RoundingMode.HALF_UP).floatValue());
        } else {
            gen.writeNumber(value);
        }
    }
}
