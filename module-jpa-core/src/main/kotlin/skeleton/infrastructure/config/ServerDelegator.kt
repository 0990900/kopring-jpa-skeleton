package skeleton.infrastructure.config

interface ServerDelegator {
    fun start()

    fun stop()

    companion object {
        val empty = object : ServerDelegator {
            override fun start() {}
            override fun stop() {}
        }
    }
}
