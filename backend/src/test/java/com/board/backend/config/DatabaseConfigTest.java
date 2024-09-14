package com.board.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConfigTest {

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Autowired
    private JpaTransactionManager transactionManager;

    @Test
    public void testEntityManagerFactoryBeanCreated() {
        assertNotNull(entityManagerFactory);
        assertEquals("com.board.backend.entity", entityManagerFactory.getPersistenceUnitInfo().getManagedClassNames().get(0));
    }

    @Test
    public void testJpaTransactionManagerCreated() {
        assertNotNull(transactionManager);
        assertNotNull(transactionManager.getEntityManagerFactory());
    }
}