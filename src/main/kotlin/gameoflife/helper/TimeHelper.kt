package gameoflife.helper

import java.time.LocalDateTime
import java.time.ZoneId

private const val MINUTE_MILLIS = 1000.toLong()
private const val HOUR_MILLIS = 60 * 60 * MINUTE_MILLIS

val Long.toTime: String get() = (this + timeOffset).formatTime

private val Long.formatTime: String get() = "%tH:%tM:%tS.%tL".format(this, this, this, this)

private val timeOffset: Long get() = HOUR_MILLIS - localOffset

private val localOffset: Long get() =
    LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .offset.totalSeconds * MINUTE_MILLIS