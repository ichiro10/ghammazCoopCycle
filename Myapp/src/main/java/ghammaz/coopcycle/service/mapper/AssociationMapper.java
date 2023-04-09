package ghammaz.coopcycle.service.mapper;

import ghammaz.coopcycle.domain.Association;
import ghammaz.coopcycle.service.dto.AssociationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Association} and its DTO {@link AssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssociationMapper extends EntityMapper<AssociationDTO, Association> {}
