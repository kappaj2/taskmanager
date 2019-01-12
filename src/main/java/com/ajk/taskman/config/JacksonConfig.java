package com.ajk.taskman.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * Support for Hibernate types in Jackson.
     *
     * @return
     */
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    /**
     * Jackson Afterburner module to speed up serialization/deserialization.
     *
     * @return
     */
    @Bean
    public AfterburnerModule afterburnerModule() {
        return new AfterburnerModule();
    }

    /**
     * ObjectMapper for JSON data manipulation.
     *
     * @return
     */
    @Bean(name = "objectMapper")
    public ObjectMapper jsonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}
