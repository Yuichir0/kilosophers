package ru.spbstu.kilosophers.sample

import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind.*
import ru.spbstu.kilosophers.Fork
import ru.spbstu.kilosophers.sample.SampleKilosopher.State.*

class SampleKilosopher(left: Fork, right: Fork, private val index: Int, max: Int) : AbstractKilosopher(left, right) {

    internal enum class State {
        WAITS_BOTH,
        WAITS_RIGHT,
        WAITS_LEFT,
        EATS,
        HOLDS_BOTH,
        HOLDS_RIGHT,
        HOLDS_LEFT,
        THINKS
    }

    private var state = WAITS_BOTH
    private var leftFork = index
    private var rightFork = (index + 1) % max

    override fun nextAction(): Action {
        return if (leftFork < rightFork) when (state) {
            WAITS_BOTH -> TAKE_LEFT(10)
            WAITS_RIGHT -> TAKE_RIGHT(10)
            EATS -> EAT(50)
            HOLDS_BOTH -> DROP_RIGHT(10)
            HOLDS_LEFT -> DROP_LEFT(10)
            else -> THINK(100)
        } else when (state) {
            WAITS_BOTH -> TAKE_RIGHT(10)
            WAITS_LEFT -> TAKE_LEFT(10)
            EATS -> EAT(50)
            HOLDS_BOTH -> DROP_LEFT(10)
            HOLDS_RIGHT -> DROP_RIGHT(10)
            else -> THINK(100)
        }
    }

    override fun handleResult(action: Action, result: Boolean) {
        if (leftFork < rightFork) state = when (action.kind) {
            TAKE_LEFT -> if (result) WAITS_RIGHT else WAITS_BOTH
            TAKE_RIGHT -> if (result) EATS else WAITS_RIGHT
            EAT -> HOLDS_BOTH
            DROP_RIGHT -> if (result) HOLDS_LEFT else HOLDS_BOTH
            DROP_LEFT -> if (result) THINKS else HOLDS_LEFT
            THINK -> WAITS_BOTH
        } else
            state = when (action.kind) {
                TAKE_RIGHT -> if (result) WAITS_LEFT else WAITS_BOTH
                TAKE_LEFT -> if (result) EATS else WAITS_LEFT
                EAT -> HOLDS_BOTH
                DROP_LEFT -> if (result) HOLDS_RIGHT else HOLDS_BOTH
                DROP_RIGHT -> if (result) THINKS else HOLDS_RIGHT
                THINK -> WAITS_BOTH
            }
    }

    override fun toString(): String {
        return "Kilosopher #$index"
    }
}