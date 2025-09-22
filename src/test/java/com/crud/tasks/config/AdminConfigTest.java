package com.crud.tasks.config;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class AdminConfigTest {

    @Test
    void shouldReturnAdminMail() throws Exception {
        AdminConfig adminConfig = new AdminConfig();
        adminConfig.setAdminMail("admin@admin.com");

        assertThat(adminConfig.getAdminMail()).isEqualTo("admin@admin.com");
    }
}
