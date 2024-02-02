package com.izerofx.configuration.p6spy;

import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.logging.LoggingEventListener;

import java.sql.SQLException;

/**
 * className: CustomLoggingEventListener<br>
 * description: 监听事件<br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public class CustomLoggingEventListener extends LoggingEventListener {
    private static CustomLoggingEventListener INSTANCE;

    public static CustomLoggingEventListener getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new CustomLoggingEventListener();
        }
        return INSTANCE;
    }

    @Override
    public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
        //忽略批量执行结果
    }
}
