package skeleton.infrastructure

import java.util.Objects
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

object EntityHelper {
    private fun <T : Any> extractGetter(t1: T) = t1::class
        .memberProperties
        .filter { it.getter.findAnnotation<IdentityColumn>() != null }
        .map { it.getter }

    /**
     * 비영속성(transient) 엔티티의 동등성을 비교하는 함수.
     *
     * 엔티티의 하위에 다른 엔티티가 있다면 영속성 여부를 고려하여 동등성과 동일성을 선택하여 중첩하여 비교한다.
     *
     * @param t1 비영속성 엔티티
     * @param t2 엔티티 (영속성 여부는 알 수 없음)
     * @param otherGetters 추가적으로 동등성을 비교해야 할 필드의 getter
     */
    fun <T : Any> transientEquals(t1: T, t2: T, vararg otherGetters: (T) -> Any?): Boolean {
        return (
            extractGetter(t1).map { getter ->
                getter.call(t1) == getter.call(t2)
            } + otherGetters.map { getter ->
                getter.invoke(t1) == getter.invoke(t2)
            }
            ).reduce { acc, b -> acc && b }
    }

    fun <T : Any> transientHashCode(t1: T, vararg otherGetters: (T) -> Any?): Int {
        val values = extractGetter(t1).map { getter ->
            getter.call(t1)
        } + otherGetters.map { getter ->
            getter.invoke(t1)
        }
        @Suppress("SpreadOperator")
        return Objects.hash(*values.toTypedArray())
    }

    fun compareTo(e1: Identifiable, e2: Identifiable): Result<Int> = runCatching {
        check(e1.isNew || e2.isNew) { "Can't compare entities" }
        checkNotNull(e1.id).compareTo(checkNotNull(e2.id))
    }
}
