package com.izerofx.configuration.p6spy;

/**
 * className: StdoutLogger<br>
 * description: 输出 SQL 日志<br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public class StdoutLogger extends com.p6spy.engine.spy.appender.StdoutLogger {

    @Override
    public void logText(String text) {
        // 打印红色 SQL 日志
        System.err.println(text);
    }
}
