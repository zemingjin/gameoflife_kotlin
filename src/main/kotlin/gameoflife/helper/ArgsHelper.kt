package gameoflife.helper

import java.util.stream.Stream

private const val OPT_STEP = "-s"
private const val OPT_WAIT = "-w"
const val WAIT_TIME = 100

val Array<String>.waitTime: Int get() =
    Stream.of(*this)
            .filter { it.startsWith(OPT_WAIT) && it.length > OPT_WAIT.length }
            .map { it.substring(OPT_WAIT.length).toInt() }
            .findFirst()
            .orElse(WAIT_TIME)

val Array<String>.isAutomaton: Boolean get() = !contains(OPT_STEP)