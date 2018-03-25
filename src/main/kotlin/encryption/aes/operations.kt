package encryption.aes

// Returns IntArray because kotlin's bytes are signed :/
fun stateMatrix(bytes: Array<Int>): Array<Array<Int>> {
    assert(bytes.size == 16)
    val state = Array(4, { Array(4, { 0 }) })
    (0 until 16).forEach {
        state[it % 4][it / 4] = bytes[it]
    }

    return state
}

fun subBytes(state: Array<Array<Int>>, inverse: Boolean = false): Array<Array<Int>> {
    for (i in 0 until 4)
        for (j in 0 until 4)
            state[i][j] = if (inverse) {
                Reandal.invSbox[state[i][j]]
            } else {
                Reandal.sbox[state[i][j]]
            }

    return state
}

fun shiftRows(state: Array<Array<Int>>, inverse: Boolean = false): Array<Array<Int>> {
    val temp = Array(4, { Array(4, { 0 }) })
    for (r in 0 until 4)
        for (c in 0 until 4)
            if (inverse) {
                temp[r][(c + r) % 4] = state[r][c]
            } else {
                temp[r][c] = state[r][(c + r) % 4]
            }
    return temp
}

fun addRoundKey(state: Array<Array<Int>>, expandedKey: Array<Int>): Array<Array<Int>> {
    (0 until 16).forEach {
        val r = it % 4
        val c = it / 4
        state[r][c] = state[r][c] xor expandedKey[it]
    }
    return state
}

fun mixColumns(state: Array<Array<Int>>, inverse: Boolean = false): Array<Array<Int>> {

    // 14*a0 + 11*a1 + 13*a2 + 9*a3
    // 9*a0 + 14*a1 + 11*a2 + 13*a3
    // 13*a0 + 9*a1 + 14*a2 + 11*a3
    // 11*a0 + 13*a1 + 9*a2 + 14*a3

    for (c in 0 until 4) {
        val t = IntArray(4)
        for (r in 0 until 4) {
            t[r] = state[r][c]
        }

        if (inverse) {
            state[0][c] = Reandal.mul14[t[0]] xor Reandal.mul11[t[1]] xor Reandal.mul13[t[2]] xor Reandal.mul9[t[3]]
            state[1][c] = Reandal.mul9[t[0]] xor Reandal.mul14[t[1]] xor Reandal.mul11[t[2]] xor Reandal.mul13[t[3]]
            state[2][c] = Reandal.mul13[t[0]] xor Reandal.mul9[t[1]] xor Reandal.mul14[t[2]] xor Reandal.mul11[t[3]]
            state[3][c] = Reandal.mul11[t[0]] xor Reandal.mul13[t[1]] xor Reandal.mul9[t[2]] xor Reandal.mul14[t[3]]
        } else {
            state[0][c] = Reandal.mul2[t[0]] xor Reandal.mul3[t[1]] xor t[2] xor t[3]
            state[1][c] = t[0] xor Reandal.mul2[t[1]] xor Reandal.mul3[t[2]] xor t[3]
            state[2][c] = t[0] xor t[1] xor Reandal.mul2[t[2]] xor Reandal.mul3[t[3]]
            state[3][c] = Reandal.mul3[t[0]] xor t[1] xor t[2] xor Reandal.mul2[t[3]]
        }
    }

    return state
}