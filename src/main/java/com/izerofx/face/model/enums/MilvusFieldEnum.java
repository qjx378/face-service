package com.izerofx.face.model.enums;

import io.milvus.grpc.DataType;
import lombok.Getter;

/**
 * className: MilvusFiled<br>
 * description: <br>
 * createDate: 2024年01月16日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
@Getter
public enum MilvusFieldEnum {

    FACE_ID("face_id", DataType.Int64),
    FACE_ENCODING("face_encoding", DataType.FloatVector),
    FACE_TOKEN("face_token", DataType.VarChar);


    private final String fieldName;
    private final DataType fieldType;

    MilvusFieldEnum(String fieldName, DataType fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}
