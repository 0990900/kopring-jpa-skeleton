package skeleton.infrastructure

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Converter {
    private val defaultDateTimePattern = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT_STRING)
    fun LocalDateTime.toYmdHms(): String = this.format(defaultDateTimePattern)
    fun LocalDateTime.epochSecond(): Long = this.atZone(ZoneId.systemDefault()).toEpochSecond()
}
const val DEFAULT_DATETIME_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss"
