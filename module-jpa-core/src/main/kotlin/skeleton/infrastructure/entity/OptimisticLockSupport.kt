package skeleton.infrastructure.entity

interface OptimisticLockSupport {
    var versionNo: Long
    fun versionUp(): Unit
}
