package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoissonsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoissonsDTO.class);
        BoissonsDTO boissonsDTO1 = new BoissonsDTO();
        boissonsDTO1.setId(1L);
        BoissonsDTO boissonsDTO2 = new BoissonsDTO();
        assertThat(boissonsDTO1).isNotEqualTo(boissonsDTO2);
        boissonsDTO2.setId(boissonsDTO1.getId());
        assertThat(boissonsDTO1).isEqualTo(boissonsDTO2);
        boissonsDTO2.setId(2L);
        assertThat(boissonsDTO1).isNotEqualTo(boissonsDTO2);
        boissonsDTO1.setId(null);
        assertThat(boissonsDTO1).isNotEqualTo(boissonsDTO2);
    }
}
