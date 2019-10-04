package gameoflife.helper

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

val Long.toTime: String get() = (this + timeOffset).formatTime

private val Long.formatTime: String get() = "%tk:%tM:%tS.%tL".format(this, this, this, this)

private const val SECOND_MILLIS = 1000L
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS

private val timeOffset: Long get() = HOUR_MILLIS - localOffsetMillis

private val localOffsetMillis: Long get() =
    LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .offset
            .totalMillis

private val ZoneOffset.totalMillis: Long get() = totalSeconds * SECOND_MILLIS