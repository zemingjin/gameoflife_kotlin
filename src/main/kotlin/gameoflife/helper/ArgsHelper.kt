package gameoflife.helper

private const val OPT_STEP = "-s"
private const val OPT_WAIT = "-w"
const val WAIT_TIME = 100

val Array<String>.waitTime: Int
    get() {
        return toList()
            .firstOrNull { it.startsWith(OPT_WAIT) && it.length > OPT_WAIT.length }
            .let { it?.substring(OPT_WAIT.length)?.toInt() }
            ?: WAIT_TIME
    }

val Array<String>.isAutomaton: Boolean get() = !contains(OPT_STEP)