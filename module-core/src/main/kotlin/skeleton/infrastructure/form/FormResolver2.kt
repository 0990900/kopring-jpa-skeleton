package skeleton.infrastructure.form

import arrow.core.flatMap
import arrow.core.zip
import org.springframework.data.jpa.repository.JpaRepository
import skeleton.infrastructure.entity.Identifiable

abstract class FormResolver2<
    E : Identifiable,
    in CF,
    in MF : ModificationForm,
    P1 : Identifiable,
    P2 : Identifiable> {

    abstract fun CF.parentIds(): Result<Pair<Long, Long>>
    abstract fun MF.parentIds(): Result<Pair<Long?, Long?>>
    fun CF.parents(
        repo: Pair<JpaRepository<P1, Long>, JpaRepository<P2, Long>>
    ): Result<Pair<P1, P2>> = this.parentIds()
        .flatMap { (p1, p2) ->
            val (repo1, repo2) = repo
            runCatching { repo1.getReferenceById(p1) }
                .zip(runCatching { repo2.getReferenceById(p2) }) { a, b -> Pair(a, b) }
        }

    abstract fun CF.toEntity(parent: Pair<P1, P2>): Result<E>
    fun MF.toEntity(
        entity: E,
        repo: Pair<JpaRepository<P1, Long>, JpaRepository<P2, Long>>
    ): Result<E> = runCatching { require(this.id == entity.id) }
        .flatMap { this.parentIds() }
        .flatMap { (p1, p2) ->
            val (repo1, repo2) = repo
            runCatching { p1?.let { repo1.getReferenceById(it) } }
                .zip(runCatching { p2?.let { repo2.getReferenceById(it) } }) { a, b -> Pair(a, b) }
        }
        .flatMap { this.modify(entity, it) }
        .map { entity }

    abstract fun MF.modify(entity: E, parent: Pair<P1?, P2?>): Result<Unit>
}
