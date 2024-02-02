package com.izerofx.configuration.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;

/**
 * className: P6SpyLogger<br>
 * description: P6spy SQL 打印策略<br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public class P6SpyLogger implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
                                String prepared, String sql, String url) {
        return StringUtils.isNotBlank(sql) ? " Consume Time：" + elapsed + " ms " + now + "\n Execute SQL：" + sql.replaceAll("\\s+", " ") + "\n" : "";
    }
}
