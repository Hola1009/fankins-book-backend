package com.fankins.book.framework.stater.biz.context.config;

import com.fankins.book.framework.stater.biz.context.filter.HeaderUserId2ContextFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="https://github.com/hola1009">fancier</a>
 **/
@AutoConfiguration
public class ContextAutoConfiguration {
    @Bean
    public HeaderUserId2ContextFilter headerUserId2ContextFilter() {
        return new HeaderUserId2ContextFilter();
    }
}
