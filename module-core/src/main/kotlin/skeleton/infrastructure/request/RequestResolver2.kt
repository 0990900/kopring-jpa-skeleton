package skeleton.infrastructure.request

import arrow.core.flatMap
import arrow.core.zip
import org.springframework.data.jpa.repository.JpaRepository
import skeleton.infrastructure.entity.Identifiable

abstract class RequestResolver2<
    E : Identifiable,
    out R,
    in CR,
    in MR : ModifyRequest,
    P1 : Identifiable,
    P2 : Identifiable> {
    abstract fun E.toDto(): Result<R>

    abstract fun CR.parentIds(): Result<Pair<Long, Long>>
    abstract fun MR.parentIds(): Result<Pair<Long?, Long?>>
    fun CR.parents(
        repo: Pair<JpaRepository<P1, Long>, JpaRepository<P2, Long>>
    ): Result<Pair<P1, P2>> = this.parentIds()
        .flatMap { (p1, p2) ->
            val (repo1, repo2) = repo
            runCatching { repo1.getReferenceById(p1) }
                .zip(runCatching { repo2.getReferenceById(p2) }) { a, b -> Pair(a, b) }
        }

    abstract fun CR.toEntity(parent: Pair<P1, P2>): Result<E>
    fun MR.toEntity(
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

    abstract fun MR.modify(entity: E, parent: Pair<P1?, P2?>): Result<Unit>
}
