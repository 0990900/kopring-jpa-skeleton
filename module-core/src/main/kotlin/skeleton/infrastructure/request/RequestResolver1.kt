package skeleton.infrastructure.request

import arrow.core.flatMap
import org.springframework.data.jpa.repository.JpaRepository
import skeleton.infrastructure.entity.Identifiable

abstract class RequestResolver1<
    E : Identifiable,
    out R,
    in CR,
    in MR : ModifyRequest,
    P1 : Identifiable> {
    abstract fun E.toDto(): Result<R>

    abstract fun CR.parentId(): Result<Long>
    abstract fun MR.parentId(): Result<Long>
    fun CR.parent(repo: JpaRepository<P1, Long>): Result<P1> =
        this.parentId().flatMap { runCatching { repo.getReferenceById(it) } }

    abstract fun CR.toEntity(p1: P1): Result<E>
    fun MR.toEntity(entity: E, repo: JpaRepository<P1, Long>): Result<E> = runCatching { require(this.id == entity.id) }
        .flatMap { this.parentId() }
        .flatMap { runCatching { repo.getReferenceById(it) } }
        .flatMap { this.modify(entity, it) }
        .map { entity }

    abstract fun MR.modify(entity: E, parent: P1?): Result<Unit>
}
