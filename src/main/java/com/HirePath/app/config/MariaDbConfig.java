package com.HirePath.app.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mariadb.MariaDBVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class MariaDbConfig {

    @Bean(name = "mariaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mariadb")
    public DataSource mariaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mariaJdbcTemplate")
    public JdbcTemplate mariaJdbcTemplate(
            @Qualifier("mariaDataSource") DataSource mariaDataSource) {
        return new JdbcTemplate(mariaDataSource);
    }

    @Bean
    public VectorStore vectorStore(
            @Qualifier("mariaJdbcTemplate") JdbcTemplate mariaJdbcTemplate,
            EmbeddingModel embeddingModel) {

        return MariaDBVectorStore.builder(mariaJdbcTemplate, embeddingModel)
                .dimensions(1536)
                .distanceType(MariaDBVectorStore.MariaDBDistanceType.COSINE)
                .vectorTableName("vector_store")
                .initializeSchema(true)
                .build();
    }
}

