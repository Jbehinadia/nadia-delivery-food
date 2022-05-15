package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Boissons;
import com.mycompany.myapp.domain.Commande;
import com.mycompany.myapp.domain.CommandeDetails;
import com.mycompany.myapp.domain.Dessert;
import com.mycompany.myapp.domain.FastFood;
import com.mycompany.myapp.domain.Plat;
import com.mycompany.myapp.service.dto.BoissonsDTO;
import com.mycompany.myapp.service.dto.CommandeDTO;
import com.mycompany.myapp.service.dto.CommandeDetailsDTO;
import com.mycompany.myapp.service.dto.DessertDTO;
import com.mycompany.myapp.service.dto.FastFoodDTO;
import com.mycompany.myapp.service.dto.PlatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommandeDetails} and its DTO {@link CommandeDetailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommandeDetailsMapper extends EntityMapper<CommandeDetailsDTO, CommandeDetails> {
    @Mapping(target = "commande", source = "commande", qualifiedByName = "commandeId")
    @Mapping(target = "fastFood", source = "fastFood", qualifiedByName = "fastFoodId")
    @Mapping(target = "plat", source = "plat", qualifiedByName = "platId")
    @Mapping(target = "boissons", source = "boissons", qualifiedByName = "boissonsId")
    @Mapping(target = "dessert", source = "dessert", qualifiedByName = "dessertId")
    CommandeDetailsDTO toDto(CommandeDetails s);

    @Named("commandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandeDTO toDtoCommandeId(Commande commande);

    @Named("fastFoodId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FastFoodDTO toDtoFastFoodId(FastFood fastFood);

    @Named("platId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlatDTO toDtoPlatId(Plat plat);

    @Named("boissonsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BoissonsDTO toDtoBoissonsId(Boissons boissons);

    @Named("dessertId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DessertDTO toDtoDessertId(Dessert dessert);
}
