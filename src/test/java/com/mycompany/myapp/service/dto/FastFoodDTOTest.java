package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FastFoodDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FastFoodDTO.class);
        FastFoodDTO fastFoodDTO1 = new FastFoodDTO();
        fastFoodDTO1.setId(1L);
        FastFoodDTO fastFoodDTO2 = new FastFoodDTO();
        assertThat(fastFoodDTO1).isNotEqualTo(fastFoodDTO2);
        fastFoodDTO2.setId(fastFoodDTO1.getId());
        assertThat(fastFoodDTO1).isEqualTo(fastFoodDTO2);
        fastFoodDTO2.setId(2L);
        assertThat(fastFoodDTO1).isNotEqualTo(fastFoodDTO2);
        fastFoodDTO1.setId(null);
        assertThat(fastFoodDTO1).isNotEqualTo(fastFoodDTO2);
    }
}
