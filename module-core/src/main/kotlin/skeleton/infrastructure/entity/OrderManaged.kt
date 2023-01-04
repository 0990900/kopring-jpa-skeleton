package skeleton.infrastructure.entity

interface OrderManaged {
    fun order(): Int

    companion object {
        fun continuityCheck(
            orderManagedList: List<OrderManaged>,
            interval: Int = 1,
            intervalError: String = "The interval order is invalid",
            intervalChecker: (Int, Int) -> Int = { a, b -> b - a }
        ): Result<Unit> = runCatching {
            if (orderManagedList.size > 1) {
                val sorted = orderManagedList.sortedBy(OrderManaged::order)
                val first = sorted.first()
                sorted.subList(1, sorted.size).fold(first) { a, b ->
                    check(
                        intervalChecker(
                            a.order(),
                            b.order()
                        ) == interval
                    ) {
                        intervalError + "${a.order()} < ${b.order()}"
                    }
                    b
                }
            }
        }
    }
}
