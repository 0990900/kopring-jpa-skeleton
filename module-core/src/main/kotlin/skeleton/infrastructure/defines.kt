package skeleton.infrastructure

import java.time.LocalDateTime

interface Identifiable {
    var id: Long?
    val isNew: Boolean
}

interface Traceable {
    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?
    var createdBy: String?
    var updatedBy: String?

    companion object {
        object Columns {

            object CreatedAt {
                const val name = "created_at"
            }

            object CreatedBy {
                const val name = "created_by"
                const val length = 100
            }

            object UpdatedAt {
                const val name = "created_at"
            }

            object UpdatedBy {
                const val name = "updated_by"
                const val length = 100
            }
        }
    }
}

@MustBeDocumented
@Target(AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class IdentityColumn
//
//interface OptimisticLockSupport {
//    var versionNo: Long
//    fun versionUp(): Unit
//}
//
//interface RangeManaged<R : Comparable<R>, D : Comparable<D>> {
//    fun getRange(): Pair<R, R>
//
//    val rangeToString: (R) -> String
//
//    companion object {
//        fun <R : Comparable<R>, D : Comparable<D>> continuityCheck(
//            ranges: List<RangeManaged<R, D>>,
//            interval: D,
//            intervalError: String = "The interval between the two date is exceeded",
//            intervalChecker: (R, R) -> D
//        ): Result<Unit> = runCatching {
//            require(ranges.isNotEmpty()) { "Ranges is empty" }
//            val sorted = ranges.map(RangeManaged<R, D>::getRange).sortedBy(Pair<R, R>::first)
//            val toString = ranges.first().rangeToString
//            val first = sorted.first().apply {
//                check(first < second) { "${toString(first)} < ${toString(second)}" }
//            }
//            sorted.subList(1, sorted.size).fold(first) { a, b ->
//                check(a.first < a.second) { "${toString(a.first)} < ${toString(a.second)}" }
//                check(b.first < b.second) { "${toString(b.first)} < ${toString(b.second)}" }
//                check(a.second < b.first) { "${toString(a.second)} < ${toString(b.first)}" }
//                val currentInterval = intervalChecker(a.second, b.first)
//                check(currentInterval == interval) {
//                    intervalError + "(expected: $interval, but:$currentInterval) - ${a.second}, ${b.first}"
//                }
//                b
//            }
//        }
//    }
//}
//
//interface OrderManaged {
//    fun getManagedOrder(): Int
//
//    companion object {
//        fun continuityCheck(
//            orderManagedList: List<OrderManaged>,
//            interval: Int = 1,
//            intervalError: String = "The interval order is invalid",
//            intervalChecker: (Int, Int) -> Int = { a, b -> b - a }
//        ): Result<Unit> = runCatching {
//            if (orderManagedList.size > 1) {
//                val sorted = orderManagedList.sortedBy(OrderManaged::getManagedOrder)
//                val first = sorted.first()
//                sorted.subList(1, sorted.size).fold(first) { a, b ->
//                    check(
//                        intervalChecker(
//                            a.getManagedOrder(),
//                            b.getManagedOrder()
//                        ) == interval
//                    ) {
//                        intervalError + "${a.getManagedOrder()} < ${b.getManagedOrder()}"
//                    }
//                    b
//                }
//            }
//        }
//    }
//}
