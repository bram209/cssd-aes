package encryption.aes

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object AESSpec: Spek({
  given("a AES implementation") {
      on ("encryption") {
          it("encrypts the block correctly") {
              val key = "000102030405060708090a0b0c0d0e0f"
              val input = "00112233445566778899aabbccddeeff"
              val expectedOutput = "69c4e0d86a7b0430d8cdb78070b4c55a"

              val encrypted = AES.encryptBlock(hexToByteArray(input), hexToByteArray(key))

              assertEquals(expectedOutput, encrypted.toHex().toLowerCase())
          }
      }

      on("decryption") {
          it("decrypts the block correctly") {
              val key = "000102030405060708090a0b0c0d0e0f"
              val encrypted = "69c4e0d86a7b0430d8cdb78070b4c55a"
              val expected = "00112233445566778899aabbccddeeff"

              val decrypted = AES.decryptBlock(hexToByteArray(encrypted), hexToByteArray(key))

              assertEquals(expected, decrypted.toHex().toLowerCase())
          }
      }
  }
})