package skeleton.infrastructure.request

import arrow.core.flatMap
import arrow.core.zip
import org.springframework.data.jpa.repository.JpaRepository
import skeleton.infrastructure.entity.Identifiable

abstract class RequestResolver3<
    E : Identifiable,
    out R,
    in CR,
    in MR : ModifyRequest,
    P1 : Identifiable,
    P2 : Identifiable,
    P3 : Identifiable> {
    abstract fun E.toDto(): Result<R>

    abstract fun CR.parentIds(): Result<Triple<Long, Long, Long>>
    abstract fun MR.parentIds(): Result<Triple<Long?, Long?, Long?>>
    fun CR.parents(
        repo: Triple<JpaRepository<P1, Long>, JpaRepository<P2, Long>, JpaRepository<P3, Long>>
    ): Result<Triple<P1, P2, P3>> = this.parentIds()
        .flatMap { (p1, p2, p3) ->
            val (repo1, repo2, repo3) = repo
            runCatching { repo1.getReferenceById(p1) }
                .zip(
                    runCatching { repo2.getReferenceById(p2) },
                    runCatching { repo3.getReferenceById(p3) }
                ) { a, b, c -> Triple(a, b, c) }
        }

    abstract fun CR.toEntity(parent: Triple<P1, P2, P3>): Result<E>
    fun MR.toEntity(
        entity: E,
        repo: Triple<JpaRepository<P1, Long>, JpaRepository<P2, Long>, JpaRepository<P3, Long>>
    ): Result<E> = runCatching { require(this.id == entity.id) }
        .flatMap { this.parentIds() }
        .flatMap { (p1, p2, p3) ->
            val (repo1, repo2, repo3) = repo
            runCatching { p1?.let { repo1.getReferenceById(it) } }
                .zip(
                    runCatching { p2?.let { repo2.getReferenceById(it) } },
                    runCatching { p3?.let { repo3.getReferenceById(it) } }
                ) { a, b, c -> Triple(a, b, c) }
        }
        .flatMap { this.modify(entity, it) }
        .map { entity }

    abstract fun MR.modify(entity: E, parent: Triple<P1?, P2?, P3?>): Result<Unit>
}
