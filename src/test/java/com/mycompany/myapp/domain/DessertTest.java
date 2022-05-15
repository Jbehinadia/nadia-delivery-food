package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DessertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dessert.class);
        Dessert dessert1 = new Dessert();
        dessert1.setId(1L);
        Dessert dessert2 = new Dessert();
        dessert2.setId(dessert1.getId());
        assertThat(dessert1).isEqualTo(dessert2);
        dessert2.setId(2L);
        assertThat(dessert1).isNotEqualTo(dessert2);
        dessert1.setId(null);
        assertThat(dessert1).isNotEqualTo(dessert2);
    }
}
