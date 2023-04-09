package ghammaz.coopcycle.service.mapper;

import ghammaz.coopcycle.domain.Client;
import ghammaz.coopcycle.domain.Livraison;
import ghammaz.coopcycle.domain.Livreur;
import ghammaz.coopcycle.service.dto.ClientDTO;
import ghammaz.coopcycle.service.dto.LivraisonDTO;
import ghammaz.coopcycle.service.dto.LivreurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livraison} and its DTO {@link LivraisonDTO}.
 */
@Mapper(componentModel = "spring")
public interface LivraisonMapper extends EntityMapper<LivraisonDTO, Livraison> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "livreur", source = "livreur", qualifiedByName = "livreurId")
    LivraisonDTO toDto(Livraison s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("livreurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivreurDTO toDtoLivreurId(Livreur livreur);
}
