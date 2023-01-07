package skeleton.infrastructure.form

import arrow.core.Tuple4
import arrow.core.flatMap
import arrow.core.zip
import org.springframework.data.jpa.repository.JpaRepository
import skeleton.infrastructure.entity.Identifiable

abstract class FormResolver4<
    E : Identifiable,
    out R,
    in CF,
    in MF : ModificationForm,
    P1 : Identifiable,
    P2 : Identifiable,
    P3 : Identifiable,
    P4 : Identifiable> {

    abstract fun CF.parentIds(): Result<Tuple4<Long, Long, Long, Long>>
    abstract fun MF.parentIds(): Result<Tuple4<Long?, Long?, Long?, Long?>>
    fun CF.parents(
        repo: Tuple4<JpaRepository<P1, Long>, JpaRepository<P2, Long>, JpaRepository<P3, Long>, JpaRepository<P4, Long>>
    ): Result<Tuple4<P1, P2, P3, P4>> = this.parentIds()
        .flatMap { (p1, p2, p3, p4) ->
            val (repo1, repo2, repo3, repo4) = repo
            runCatching { repo1.getReferenceById(p1) }
                .zip(
                    runCatching { repo2.getReferenceById(p2) },
                    runCatching { repo3.getReferenceById(p3) },
                    runCatching { repo4.getReferenceById(p4) }
                ) { a, b, c, d -> Tuple4(a, b, c, d) }
        }

    abstract fun CF.toEntity(parent: Tuple4<P1, P2, P3, P4>): Result<E>
    fun MF.toEntity(
        entity: E,
        repo: Tuple4<JpaRepository<P1, Long>, JpaRepository<P2, Long>, JpaRepository<P3, Long>, JpaRepository<P4, Long>>
    ): Result<E> = runCatching { require(this.id == entity.id) }
        .flatMap { this.parentIds() }
        .flatMap { (p1, p2, p3, p4) ->
            val (repo1, repo2, repo3, repo4) = repo
            runCatching { p1?.let { repo1.getReferenceById(it) } }
                .zip(
                    runCatching { p2?.let { repo2.getReferenceById(it) } },
                    runCatching { p3?.let { repo3.getReferenceById(it) } },
                    runCatching { p4?.let { repo4.getReferenceById(it) } }
                ) { a, b, c, d -> Tuple4(a, b, c, d) }
        }
        .flatMap { this.modify(entity, it) }
        .map { entity }

    abstract fun MF.modify(entity: E, parent: Tuple4<P1?, P2?, P3?, P4?>): Result<Unit>
}
