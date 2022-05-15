package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoissonsMapperTest {

    private BoissonsMapper boissonsMapper;

    @BeforeEach
    public void setUp() {
        boissonsMapper = new BoissonsMapperImpl();
    }
}
