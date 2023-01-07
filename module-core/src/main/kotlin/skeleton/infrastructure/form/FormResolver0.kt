package skeleton.infrastructure.form

import arrow.core.flatMap
import skeleton.infrastructure.entity.Identifiable

abstract class FormResolver0<E : Identifiable, in CF, in MF : ModificationForm> {
    abstract fun CF.toEntity(): Result<E>
    fun MF.toEntity(entity: E): Result<E> = runCatching { require(this.id == entity.id) }
        .flatMap { this.modify(entity) }
        .map { entity }
    abstract fun MF.modify(entity: E): Result<Unit>
}
