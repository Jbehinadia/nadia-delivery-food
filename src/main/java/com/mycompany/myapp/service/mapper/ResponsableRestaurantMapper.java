package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.ResponsableRestaurant;
import com.mycompany.myapp.domain.Restaurant;
import com.mycompany.myapp.service.dto.ResponsableRestaurantDTO;
import com.mycompany.myapp.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResponsableRestaurant} and its DTO {@link ResponsableRestaurantDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResponsableRestaurantMapper extends EntityMapper<ResponsableRestaurantDTO, ResponsableRestaurant> {
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    ResponsableRestaurantDTO toDto(ResponsableRestaurant s);

    @Named("restaurantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoRestaurantId(Restaurant restaurant);
}
