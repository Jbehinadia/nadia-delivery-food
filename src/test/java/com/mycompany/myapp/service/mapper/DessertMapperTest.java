package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DessertMapperTest {

    private DessertMapper dessertMapper;

    @BeforeEach
    public void setUp() {
        dessertMapper = new DessertMapperImpl();
    }
}
