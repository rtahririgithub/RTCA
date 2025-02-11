package com.telus.credit.crda.factory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class BaseAssessmentFactory implements ApplicationContextAware {
    static ApplicationContext m_ApplicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        m_ApplicationContext = applicationContext;
    }
}
