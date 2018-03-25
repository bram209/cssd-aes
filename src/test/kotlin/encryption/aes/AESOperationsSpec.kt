package encryption.aes

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertTrue


object AESOperationsSpec : Spek({
    given("AES Standard encryption") {

        val key = "11223344556677889900AABBCCDDEEFF" //Key as a hex string
        val input = "12345689abcdefgh"
//        val state = stateMatrix(input.toByteArray(Charsets.US_ASCII))

        on("state matrix") {
            it("creates a 4x4 matrix of bytes") {
                val stateMatrix = stateMatrix(input.toByteArray(Charsets.US_ASCII).map { it.toInt() }.toTypedArray())
                val expected = arrayOf(
                        arrayOf(49, 53, 97, 101),
                        arrayOf(50, 54, 98, 102),
                        arrayOf(51, 56, 99, 103),
                        arrayOf(52, 57, 100, 104)
                )

                assertTrue { Arrays.deepEquals(expected, stateMatrix) }
            }
        }

        on("subBytes") {
            val state = arrayOf(
                    arrayOf(49, 53, 97, 101),
                    arrayOf(50, 54, 98, 102),
                    arrayOf(51, 56, 99, 103),
                    arrayOf(52, 57, 100, 104)
            )

            val expected = arrayOf(
                    arrayOf(199, 150, 239, 77),
                    arrayOf(35, 5, 170, 51),
                    arrayOf(195, 7, 251, 133),
                    arrayOf(24, 18, 67, 69)
            )

            it("substituted the bytes correctly") {
                val newState = subBytes(state)
                assertTrue { Arrays.deepEquals(expected, newState) }
            }

            it("inverse substituted the bytes correctly") {
                val subBytes = subBytes(state)
                val inverse = subBytes(subBytes, inverse = true)
                assertTrue { Arrays.deepEquals(subBytes, inverse) }
            }
        }
        on("shiftRows") {
            val state = arrayOf(
                    arrayOf(1, 2, 3, 4),
                    arrayOf(1, 2, 3, 4),
                    arrayOf(1, 2, 3, 4),
                    arrayOf(1, 2, 3, 4)
            )

            val originalState = state.clone()

            val expected = arrayOf(
                    arrayOf(1, 2, 3, 4),
                    arrayOf(2, 3, 4, 1),
                    arrayOf(3, 4, 1, 2),
                    arrayOf(4, 1, 2, 3)
            )

            it("shifts the rows correctly") {
                val newState = shiftRows(state)

                assertTrue {
                    Arrays.deepEquals(expected, newState)
                }
            }

            it("inverse shifts the rows correctly") {
                val shiftedState = shiftRows(state)
                val inverseState = shiftRows(shiftedState, inverse = true)
                assertTrue { Arrays.deepEquals(originalState, inverseState) }
            }
        }

        on("mixColumns") {
            val state = arrayOf(
                    arrayOf(49, 53, 97, 101),
                    arrayOf(50, 54, 98, 102),
                    arrayOf(51, 56, 99, 103),
                    arrayOf(52, 57, 100, 104)
            )

            val origonalState = state.clone()

            it("mix the columns correctly") {
                val newState = mixColumns(state)
                val expected = arrayOf(
                        arrayOf(51, 49, 99, 111),
                        arrayOf(52, 40, 100, 104),
                        arrayOf(57, 56, 105, 117),
                        arrayOf(58, 35, 106, 126)
                )

                assertTrue { Arrays.deepEquals(expected, newState) }
            }

            it ("inverse mix the columns correctly") {
                val newState = mixColumns(state)
                val invState = mixColumns(newState, inverse = true)
                assertTrue { Arrays.deepEquals(origonalState, invState) }
            }
        }
//        on("addition") {
//            val sum = 4
//            it("should return the result of adding the first number to the second number") {
//                assertEquals(6, sum)
//            }
//        }
    }
})
