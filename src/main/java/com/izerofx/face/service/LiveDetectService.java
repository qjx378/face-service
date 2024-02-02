package com.izerofx.face.service;

import com.izerofx.face.model.vo.LiveDetectVO;

import java.io.InputStream;

/**
 * className: LiveDetectService<br>
 * description: 活体检测服务接口<br>
 * createDate: 2022年07月04日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public interface LiveDetectService {


    /**
     * 静默活体检测
     *
     * @param imageStream 图片输入流
     * @return 活体检测结果
     */
    LiveDetectVO liveDetectFace(InputStream imageStream);

}
