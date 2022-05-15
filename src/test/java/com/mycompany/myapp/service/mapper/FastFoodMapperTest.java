package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FastFoodMapperTest {

    private FastFoodMapper fastFoodMapper;

    @BeforeEach
    public void setUp() {
        fastFoodMapper = new FastFoodMapperImpl();
    }
}
