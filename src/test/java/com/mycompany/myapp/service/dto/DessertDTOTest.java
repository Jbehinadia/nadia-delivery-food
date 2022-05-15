package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DessertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DessertDTO.class);
        DessertDTO dessertDTO1 = new DessertDTO();
        dessertDTO1.setId(1L);
        DessertDTO dessertDTO2 = new DessertDTO();
        assertThat(dessertDTO1).isNotEqualTo(dessertDTO2);
        dessertDTO2.setId(dessertDTO1.getId());
        assertThat(dessertDTO1).isEqualTo(dessertDTO2);
        dessertDTO2.setId(2L);
        assertThat(dessertDTO1).isNotEqualTo(dessertDTO2);
        dessertDTO1.setId(null);
        assertThat(dessertDTO1).isNotEqualTo(dessertDTO2);
    }
}
