package ghammaz.coopcycle.service.mapper;

import ghammaz.coopcycle.domain.Client;
import ghammaz.coopcycle.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
