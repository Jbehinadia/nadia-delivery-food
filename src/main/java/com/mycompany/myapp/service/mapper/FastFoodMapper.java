package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.FastFood;
import com.mycompany.myapp.domain.Menu;
import com.mycompany.myapp.service.dto.FastFoodDTO;
import com.mycompany.myapp.service.dto.MenuDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FastFood} and its DTO {@link FastFoodDTO}.
 */
@Mapper(componentModel = "spring")
public interface FastFoodMapper extends EntityMapper<FastFoodDTO, FastFood> {
    @Mapping(target = "menu", source = "menu", qualifiedByName = "menuId")
    FastFoodDTO toDto(FastFood s);

    @Named("menuId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuDTO toDtoMenuId(Menu menu);
}
