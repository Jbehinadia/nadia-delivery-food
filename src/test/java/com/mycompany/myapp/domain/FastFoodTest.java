package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FastFoodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FastFood.class);
        FastFood fastFood1 = new FastFood();
        fastFood1.setId(1L);
        FastFood fastFood2 = new FastFood();
        fastFood2.setId(fastFood1.getId());
        assertThat(fastFood1).isEqualTo(fastFood2);
        fastFood2.setId(2L);
        assertThat(fastFood1).isNotEqualTo(fastFood2);
        fastFood1.setId(null);
        assertThat(fastFood1).isNotEqualTo(fastFood2);
    }
}
