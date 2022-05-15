package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Dessert;
import com.mycompany.myapp.domain.Menu;
import com.mycompany.myapp.service.dto.DessertDTO;
import com.mycompany.myapp.service.dto.MenuDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dessert} and its DTO {@link DessertDTO}.
 */
@Mapper(componentModel = "spring")
public interface DessertMapper extends EntityMapper<DessertDTO, Dessert> {
    @Mapping(target = "menu", source = "menu", qualifiedByName = "menuId")
    DessertDTO toDto(Dessert s);

    @Named("menuId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuDTO toDtoMenuId(Menu menu);
}
