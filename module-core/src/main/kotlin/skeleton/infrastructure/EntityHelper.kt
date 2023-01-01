package skeleton.infrastructure

import java.util.Objects
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object EntityHelper {
    private fun <T : Any> extractGetter(t1: T) = t1::class
        .memberProperties
        .filter { it.getter.findAnnotation<IdentityColumn>() != null }
        .map { it.getter }

    fun <T : Any> transientEquals(t1: T, t2: T, vararg otherGetters: (T) -> Any?): Boolean {
        return extractGetter(t1)
            .map { getter -> getter.call(t1) == getter.call(t2) }
            .run { this + otherGetters.map { getter -> getter.invoke(t1) == getter.invoke(t2) } }
            .reduce { acc, b -> acc && b }
    }

    fun <T : Any> transientHashCode(t1: T, vararg otherGetters: (T) -> Any?): Int {
        val values = extractGetter(t1)
            .map { getter -> getter.call(t1) }
            .run { this + otherGetters.map { getter -> getter.invoke(t1) } }
        @Suppress("SpreadOperator")
        return Objects.hash(*values.toTypedArray())
    }

    fun compareTo(e1: Identifiable, e2: Identifiable): Result<Int> = runCatching {
        check(e1.isNew || e2.isNew) { "Can't compare entities" }
        checkNotNull(e1.id).compareTo(checkNotNull(e2.id))
    }
}
