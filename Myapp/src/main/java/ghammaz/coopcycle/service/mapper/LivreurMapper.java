package ghammaz.coopcycle.service.mapper;

import ghammaz.coopcycle.domain.Association;
import ghammaz.coopcycle.domain.Livreur;
import ghammaz.coopcycle.service.dto.AssociationDTO;
import ghammaz.coopcycle.service.dto.LivreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livreur} and its DTO {@link LivreurDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivreurMapper extends EntityMapper<LivreurDTO, Livreur> {
    @Mapping(target = "association", source = "association", qualifiedByName = "associationId")
    LivreurDTO toDto(Livreur s);

    @Named("associationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssociationDTO toDtoAssociationId(Association association);
}
