package encryption.aes

object AES {

    fun encryptBlock(input: Array<Int>, key: Array<Int>): Array<Int> =
            process(input, key, false)

    fun decryptBlock(input: Array<Int>, key: Array<Int>): Array<Int> =
            process(input, key, true)


    private fun process(input: Array<Int>, key: Array<Int>, inverse: Boolean): Array<Int> {
        assert(input.size == 128)
        assert(key.size == 128)

        val keys = KeySchedule.expandKey(key)
        val initialState = stateMatrix(input)
        val rounds = 10

        val steps = ArrayList<(Array<Array<Int>>) -> Array<Array<Int>>>()
        steps.add { addRoundKey(it, keys[0]) }
        for (round in 1 until rounds) {
            steps.add { subBytes(it, inverse) }
            steps.add { shiftRows(it, inverse) }
            steps.add { mixColumns(it, inverse) }
            steps.add { addRoundKey(it, keys[round]) }
        }

        steps.add { subBytes(it, inverse) }
        steps.add { shiftRows(it, inverse) }
        steps.add { addRoundKey(it, keys[rounds]) }

        val processedState = if (inverse) {
            steps.reversed().fold(initialState) { acc, step -> step(acc) }
        } else {
            steps.fold(initialState) { acc, step -> step(acc) }
        }

        return (0 until 16).map {
            processedState[it % 4][it / 4]
        }.toTypedArray()
    }
}