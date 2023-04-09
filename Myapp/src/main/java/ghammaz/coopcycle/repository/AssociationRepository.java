package ghammaz.coopcycle.repository;

import ghammaz.coopcycle.domain.Association;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Association entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssociationRepository extends ReactiveCrudRepository<Association, Long>, AssociationRepositoryInternal {
    @Override
    <S extends Association> Mono<S> save(S entity);

    @Override
    Flux<Association> findAll();

    @Override
    Mono<Association> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AssociationRepositoryInternal {
    <S extends Association> Mono<S> save(S entity);

    Flux<Association> findAllBy(Pageable pageable);

    Flux<Association> findAll();

    Mono<Association> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Association> findAllBy(Pageable pageable, Criteria criteria);

}
