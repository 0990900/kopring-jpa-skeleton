package skeleton.infrastructure.entity

interface RangeManaged<R : Comparable<R>, D : Comparable<D>> {
    fun range(): Pair<R, R>

    val rangeToString: (R) -> String

    companion object {
        fun <R : Comparable<R>, D : Comparable<D>> continuityCheck(
            ranges: List<RangeManaged<R, D>>,
            interval: D,
            intervalError: String = "The interval between the two date is exceeded",
            intervalChecker: (R, R) -> D
        ): Result<Unit> = runCatching {
            require(ranges.isNotEmpty()) { "Ranges is empty" }
            val sorted = ranges.map(RangeManaged<R, D>::range).sortedBy(Pair<R, R>::first)
            val toString = ranges.first().rangeToString
            val first = sorted.first().apply {
                check(first < second) { "${toString(first)} < ${toString(second)}" }
            }
            sorted.subList(1, sorted.size).fold(first) { a, b ->
                check(a.first < a.second) { "${toString(a.first)} < ${toString(a.second)}" }
                check(b.first < b.second) { "${toString(b.first)} < ${toString(b.second)}" }
                check(a.second < b.first) { "${toString(a.second)} < ${toString(b.first)}" }
                val currentInterval = intervalChecker(a.second, b.first)
                check(currentInterval == interval) {
                    intervalError + "(expected: $interval, but:$currentInterval) - ${a.second}, ${b.first}"
                }
                b
            }
        }
    }
}
