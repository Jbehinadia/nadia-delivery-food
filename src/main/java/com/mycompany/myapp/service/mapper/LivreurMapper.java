package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Livreur;
import com.mycompany.myapp.service.dto.LivreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livreur} and its DTO {@link LivreurDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivreurMapper extends EntityMapper<LivreurDTO, Livreur> {}
