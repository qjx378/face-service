package com.izerofx.configuration.p6spy;

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.logging.P6LogOptions;
import com.p6spy.engine.spy.P6Factory;
import com.p6spy.engine.spy.P6LoadableOptions;
import com.p6spy.engine.spy.option.P6OptionsRepository;

/**
 * className: CustomLogFactory<br>
 * description: 扩展 p6spy<br>
 * createDate: 2022年06月27日<br>
 *
 * @author JiaXue.Qin<br>
 * @version v1.0
 */
public class CustomLogFactory implements P6Factory {

    @Override
    public P6LoadableOptions getOptions(P6OptionsRepository optionsRepository) {
        return new P6LogOptions(optionsRepository);
    }

    @Override
    public JdbcEventListener getJdbcEventListener() {
        return CustomLoggingEventListener.getInstance();
    }
}
