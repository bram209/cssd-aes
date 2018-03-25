package encryption.aes

import kotlin.math.exp
import kotlin.math.pow

object KeySchedule {

    fun expandKey(key: Array<Int>): Array<Array<Int>> {

        val rounds = 10
        val expandedKeys = ArrayList<Int>((rounds + 1) * 16)
        expandedKeys.addAll(key.take(16))

        var rconIteration = 1

        for (rnd in 0 until rounds*4) {
            val temp = expandedKeys.takeLast(4).toIntArray()

            if (expandedKeys.size % 16 == 0) {
                rotateLeft(temp)
                for (i in 0 until 4) {
                    temp[i] = Reandal.sbox[temp[i]]
                }
                temp[0] = temp[0] xor Reandal.rcon[rconIteration]
                rconIteration++
            }

            for (i in 0 until 4) {
                expandedKeys.add(expandedKeys[expandedKeys.size - 16] xor temp[i])
            }
        }

        return expandedKeys.chunked(16).map { it.toTypedArray() }.toTypedArray()
    }

//    fun rotateLeft(word: Int) = word shr 8 or (word and 0xFF shl 24)

    fun rotateLeft(word: IntArray) {
        val t = word[0]
        word[0] = word[1]
        word[1] = word[2]
        word[2] = word[3]
        word[3] = t
    }
}