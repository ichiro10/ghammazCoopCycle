package ghammaz.coopcycle.service;

import ghammaz.coopcycle.domain.Association;
import ghammaz.coopcycle.repository.AssociationRepository;
import ghammaz.coopcycle.service.dto.AssociationDTO;
import ghammaz.coopcycle.service.mapper.AssociationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Association}.
 */
@Service
@Transactional
public class AssociationService {

    private final Logger log = LoggerFactory.getLogger(AssociationService.class);

    private final AssociationRepository associationRepository;

    private final AssociationMapper associationMapper;

    public AssociationService(AssociationRepository associationRepository, AssociationMapper associationMapper) {
        this.associationRepository = associationRepository;
        this.associationMapper = associationMapper;
    }

    /**
     * Save a association.
     *
     * @param associationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssociationDTO> save(AssociationDTO associationDTO) {
        log.debug("Request to save Association : {}", associationDTO);
        return associationRepository.save(associationMapper.toEntity(associationDTO)).map(associationMapper::toDto);
    }

    /**
     * Update a association.
     *
     * @param associationDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AssociationDTO> update(AssociationDTO associationDTO) {
        log.debug("Request to update Association : {}", associationDTO);
        return associationRepository.save(associationMapper.toEntity(associationDTO)).map(associationMapper::toDto);
    }

    /**
     * Partially update a association.
     *
     * @param associationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AssociationDTO> partialUpdate(AssociationDTO associationDTO) {
        log.debug("Request to partially update Association : {}", associationDTO);

        return associationRepository
            .findById(associationDTO.getId())
            .map(existingAssociation -> {
                associationMapper.partialUpdate(existingAssociation, associationDTO);

                return existingAssociation;
            })
            .flatMap(associationRepository::save)
            .map(associationMapper::toDto);
    }

    /**
     * Get all the associations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AssociationDTO> findAll() {
        log.debug("Request to get all Associations");
        return associationRepository.findAll().map(associationMapper::toDto);
    }

    /**
     * Returns the number of associations available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return associationRepository.count();
    }

    /**
     * Get one association by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AssociationDTO> findOne(Long id) {
        log.debug("Request to get Association : {}", id);
        return associationRepository.findById(id).map(associationMapper::toDto);
    }

    /**
     * Delete the association by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Association : {}", id);
        return associationRepository.deleteById(id);
    }
}
