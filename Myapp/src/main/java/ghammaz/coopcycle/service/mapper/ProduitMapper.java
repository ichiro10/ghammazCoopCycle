package ghammaz.coopcycle.service.mapper;

import ghammaz.coopcycle.domain.Client;
import ghammaz.coopcycle.domain.Livraison;
import ghammaz.coopcycle.domain.Produit;
import ghammaz.coopcycle.domain.Restaurant;
import ghammaz.coopcycle.service.dto.ClientDTO;
import ghammaz.coopcycle.service.dto.LivraisonDTO;
import ghammaz.coopcycle.service.dto.ProduitDTO;
import ghammaz.coopcycle.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Produit} and its DTO {@link ProduitDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProduitMapper extends EntityMapper<ProduitDTO, Produit> {
    @Mapping(target = "livraison", source = "livraison", qualifiedByName = "livraisonId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "restaurant", source = "restaurant", qualifiedByName = "restaurantId")
    ProduitDTO toDto(Produit s);

    @Named("livraisonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LivraisonDTO toDtoLivraisonId(Livraison livraison);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("restaurantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RestaurantDTO toDtoRestaurantId(Restaurant restaurant);
}
