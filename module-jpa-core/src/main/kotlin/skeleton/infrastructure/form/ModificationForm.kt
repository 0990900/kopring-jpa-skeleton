package skeleton.infrastructure.form

import skeleton.infrastructure.entity.Identifiable

interface ModificationForm {
    val id: Long

    fun <E : Identifiable> E?.modifyIfPossible(
        target: E?,
        setter: (E) -> Unit
    ): Result<Unit> = runCatching {
        if (target != null && (this == null || this.id != target.id)) {
            setter(target)
        }
    }

    fun <T> T?.modifyIfPossible(
        target: T?,
        setter: (T) -> Unit
    ): Result<Unit> = runCatching {
        if (target != null && this != target) {
            setter(target)
        }
    }

    fun <T, S> T?.modifyIfPossible(
        targetRaw: S?,
        targetSupplier: (S) -> T,
        setter: (T) -> Unit
    ): Result<Unit> = runCatching {
        if (targetRaw != null) {
            targetSupplier(targetRaw).also { target ->
                if (this != target) {
                    setter(target)
                }
            }
        }
    }
}
