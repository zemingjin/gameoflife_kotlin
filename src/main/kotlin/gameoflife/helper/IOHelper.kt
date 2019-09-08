package gameoflife.helper

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.streams.toList

fun loadSeeds(path: String): Array<String> =
        try {
            BufferedReader(FileReader(path)).use {
                it.lines()
                        .toList()
                        .toTypedArray()
            }
        } catch (ex: IOException) {
            throw RuntimeException(ex)

        }

fun format(time: Long) = String.format("%tM:%tS.%tL", time, time, time)
