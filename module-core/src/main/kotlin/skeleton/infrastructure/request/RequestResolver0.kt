package skeleton.infrastructure.request

import arrow.core.flatMap
import skeleton.infrastructure.entity.Identifiable

abstract class RequestResolver0<E : Identifiable, out R, in CR, in MR : ModifyRequest> {
    abstract fun E.toDto(): Result<R>
    abstract fun CR.toEntity(): Result<E>
    fun MR.toEntity(entity: E): Result<E> = runCatching { require(this.id == entity.id) }
        .flatMap { this.modify(entity) }
        .map { entity }
    abstract fun MR.modify(entity: E): Result<Unit>
}
