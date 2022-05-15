package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Boissons;
import com.mycompany.myapp.domain.Menu;
import com.mycompany.myapp.service.dto.BoissonsDTO;
import com.mycompany.myapp.service.dto.MenuDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Boissons} and its DTO {@link BoissonsDTO}.
 */
@Mapper(componentModel = "spring")
public interface BoissonsMapper extends EntityMapper<BoissonsDTO, Boissons> {
    @Mapping(target = "menu", source = "menu", qualifiedByName = "menuId")
    BoissonsDTO toDto(Boissons s);

    @Named("menuId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuDTO toDtoMenuId(Menu menu);
}
