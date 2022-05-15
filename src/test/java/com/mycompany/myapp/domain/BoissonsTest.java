package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoissonsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boissons.class);
        Boissons boissons1 = new Boissons();
        boissons1.setId(1L);
        Boissons boissons2 = new Boissons();
        boissons2.setId(boissons1.getId());
        assertThat(boissons1).isEqualTo(boissons2);
        boissons2.setId(2L);
        assertThat(boissons1).isNotEqualTo(boissons2);
        boissons1.setId(null);
        assertThat(boissons1).isNotEqualTo(boissons2);
    }
}
