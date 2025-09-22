package com.crud.tasks.config;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class AdminConfigTest {

    @Test
    void shouldReturnAdminMail() throws Exception {
        AdminConfig adminConfig = new AdminConfig();

        Field field = AdminConfig.class.getDeclaredField("adminMail");
        field.setAccessible(true);
        field.set(adminConfig, "test@email.com");

        assertThat(adminConfig.getAdminMail()).isEqualTo("test@email.com");
    }
}
