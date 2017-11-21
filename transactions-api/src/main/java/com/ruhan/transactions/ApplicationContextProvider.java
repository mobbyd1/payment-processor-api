package com.ruhan.transactions;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by ruhandosreis on 20/11/17.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext appContext;

    /* (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext globalAppContext)
            throws BeansException {

        this.appContext = globalAppContext;

    }

    public static ApplicationContext getApplicationContext() {
        return appContext;
    }
}
