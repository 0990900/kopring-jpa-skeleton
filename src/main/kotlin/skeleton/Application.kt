package skeleton

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KopringJpaSkeletonApplication

fun main(args: Array<String>) {
    runApplication<KopringJpaSkeletonApplication>(*args)
}
