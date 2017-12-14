package de.codehat.ircserver

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object TestSpec: Spek({

    given("a number") {

        var number = 1

        it("should be that 1 + 1 = 2") {
            number += 1

            assertEquals(2, number)
        }

    }

})