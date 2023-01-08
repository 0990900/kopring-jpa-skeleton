package skeleton.infrastructure.form

import arrow.core.flatMap
import org.springframework.data.jpa.repository.JpaRepository
import skeleton.infrastructure.entity.Identifiable

abstract class FormResolver1<
    E : Identifiable,
    in CF,
    in MF : ModificationForm,
    P1 : Identifiable> {

    abstract fun CF.parentId(): Result<Long>
    abstract fun MF.parentId(): Result<Long>
    fun CF.parent(repo: JpaRepository<P1, Long>): Result<P1> =
        this.parentId().flatMap { runCatching { repo.getReferenceById(it) } }

    abstract fun CF.toEntity(p1: P1): Result<E>
    fun MF.toEntity(entity: E, repo: JpaRepository<P1, Long>): Result<E> = runCatching { require(this.id == entity.id) }
        .flatMap { this.parentId() }
        .flatMap { runCatching { repo.getReferenceById(it) } }
        .flatMap { this.modify(entity, it) }
        .map { entity }

    abstract fun MF.modify(entity: E, parent: P1?): Result<Unit>
}
