package cn.howaric.cache.enhancer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EnhancedCacheProperties.class)
public class EnhancedCacheAutoConfiguration implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private EnhancedCacheProperties enhancedCacheProperties;

    @Autowired
    public void setEnhancedCacheProperties(EnhancedCacheProperties enhancedCacheProperties) {
        this.enhancedCacheProperties = enhancedCacheProperties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (applicationContext.containsBean(enhancedCacheProperties.getEnhancedCacheManagerName())) {
            return bean;
        }
        if (bean instanceof CacheManager) {
            AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
            if (autowireCapableBeanFactory instanceof DefaultListableBeanFactory) {
                DefaultListableBeanFactory factory = (DefaultListableBeanFactory) autowireCapableBeanFactory;
                factory.registerSingleton(enhancedCacheProperties.getEnhancedCacheManagerName(), new EnhancedCacheManager((CacheManager) bean, enhancedCacheProperties));
            }
        }
        return bean;
    }

    @Bean
    public EnhancedCacheConfig enhancedCacheConfig(DefaultListableBeanFactory beanFactory) {
        return new EnhancedCacheConfig(beanFactory);
    }

}
