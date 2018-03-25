package encryption.aes

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object KeyScheduleSpec: Spek({
    given("a key scheduler") {
        on("expanding a key") {
            val keyHex = "11223344556677889900AABBCCDDEEFF"
            val key = hexToByteArray(keyHex)

            it("expands a 128-bit key") {
                val expandedKeys = KeySchedule.expandKey(key)
                assertEquals(11, expandedKeys.size)
                assertTrue { Arrays.equals(key, expandedKeys[0]) }
            }
        }
    }
})