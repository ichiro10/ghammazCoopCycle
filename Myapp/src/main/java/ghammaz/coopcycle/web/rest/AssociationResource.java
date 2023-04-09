package ghammaz.coopcycle.web.rest;

import ghammaz.coopcycle.repository.AssociationRepository;
import ghammaz.coopcycle.service.AssociationService;
import ghammaz.coopcycle.service.dto.AssociationDTO;
import ghammaz.coopcycle.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ghammaz.coopcycle.domain.Association}.
 */
@RestController
@RequestMapping("/api")
public class AssociationResource {

    private final Logger log = LoggerFactory.getLogger(AssociationResource.class);

    private static final String ENTITY_NAME = "association";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssociationService associationService;

    private final AssociationRepository associationRepository;

    public AssociationResource(AssociationService associationService, AssociationRepository associationRepository) {
        this.associationService = associationService;
        this.associationRepository = associationRepository;
    }

    /**
     * {@code POST  /associations} : Create a new association.
     *
     * @param associationDTO the associationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new associationDTO, or with status {@code 400 (Bad Request)} if the association has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/associations")
    public Mono<ResponseEntity<AssociationDTO>> createAssociation(@Valid @RequestBody AssociationDTO associationDTO)
        throws URISyntaxException {
        log.debug("REST request to save Association : {}", associationDTO);
        if (associationDTO.getId() != null) {
            throw new BadRequestAlertException("A new association cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return associationService
            .save(associationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/associations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /associations/:id} : Updates an existing association.
     *
     * @param id the id of the associationDTO to save.
     * @param associationDTO the associationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associationDTO,
     * or with status {@code 400 (Bad Request)} if the associationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the associationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/associations/{id}")
    public Mono<ResponseEntity<AssociationDTO>> updateAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssociationDTO associationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Association : {}, {}", id, associationDTO);
        if (associationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return associationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return associationService
                    .update(associationDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /associations/:id} : Partial updates given fields of an existing association, field will ignore if it is null
     *
     * @param id the id of the associationDTO to save.
     * @param associationDTO the associationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated associationDTO,
     * or with status {@code 400 (Bad Request)} if the associationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the associationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the associationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/associations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AssociationDTO>> partialUpdateAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssociationDTO associationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Association partially : {}, {}", id, associationDTO);
        if (associationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, associationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return associationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AssociationDTO> result = associationService.partialUpdate(associationDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /associations} : get all the associations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of associations in body.
     */
    @GetMapping("/associations")
    public Mono<List<AssociationDTO>> getAllAssociations() {
        log.debug("REST request to get all Associations");
        return associationService.findAll().collectList();
    }

    /**
     * {@code GET  /associations} : get all the associations as a stream.
     * @return the {@link Flux} of associations.
     */
    @GetMapping(value = "/associations", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AssociationDTO> getAllAssociationsAsStream() {
        log.debug("REST request to get all Associations as a stream");
        return associationService.findAll();
    }

    /**
     * {@code GET  /associations/:id} : get the "id" association.
     *
     * @param id the id of the associationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the associationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/associations/{id}")
    public Mono<ResponseEntity<AssociationDTO>> getAssociation(@PathVariable Long id) {
        log.debug("REST request to get Association : {}", id);
        Mono<AssociationDTO> associationDTO = associationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(associationDTO);
    }

    /**
     * {@code DELETE  /associations/:id} : delete the "id" association.
     *
     * @param id the id of the associationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/associations/{id}")
    public Mono<ResponseEntity<Void>> deleteAssociation(@PathVariable Long id) {
        log.debug("REST request to delete Association : {}", id);
        return associationService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
