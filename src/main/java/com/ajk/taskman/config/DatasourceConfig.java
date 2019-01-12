package com.ajk.taskman.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.ajk.taskman.repository")
public class DatasourceConfig {

    private final Environment env;

    public DatasourceConfig(Environment env) {
        this.env = env;
    }
}
