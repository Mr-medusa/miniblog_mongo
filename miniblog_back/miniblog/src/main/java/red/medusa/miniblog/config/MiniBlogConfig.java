package red.medusa.miniblog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import red.medusa.miniblog.blog.bean.Blog;
import red.medusa.miniblog.pad.bean.Card;
import red.medusa.miniblog.pad.bean.Pad;
import red.medusa.miniblog.pad.vo.PadRoot;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class MiniBlogConfig implements WebMvcConfigurer {

    /**
     * 让REST暴露ID
     */
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.exposeIdsFor(PadRoot.class, Pad.class, Card.class, Blog.class);
            }
        };
    }

    /****************************** 解决中文乱码的配置 START*************************************/
    @Bean
    public HttpMessageConverter responseBodyConverter(){
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }


    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public MappingJackson2HttpMessageConverter messageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(getObjectMapper());
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        converters.add(messageConverter());
    }
    /****************************** 解决中文乱码的配置 END*************************************/


}