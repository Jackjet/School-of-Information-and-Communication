package d1.project.questionnaire.configuration;

import d1.framework.cache.DoCacheEhcache;
import d1.framework.cache.IDoCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

/**
 * 缓存注册
 *
 * @author Kikki 2019/8/26 9:42
 */
@Configuration
public class CacheConfiguration {
    @Bean
    IDoCache cache() throws MalformedURLException {
        return new DoCacheEhcache();
    }
}

