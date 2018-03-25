package encryption.aes

import kotlin.experimental.and

private val hexArray = "0123456789ABCDEF".toCharArray()

fun Array<Int>.toHex() : String{
    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(hexArray[firstIndex])
        result.append(hexArray[secondIndex])
    }

    return result.toString()
}

fun hexToByteArray(hex: String) : Array<Int> {
    val hex = hex.toUpperCase()
    val result = Array(hex.length / 2, {0})

    for (i in 0 until hex.length step 2) {
        val firstIndex = hexArray.indexOf(hex[i]);
        val secondIndex = hexArray.indexOf(hex[i + 1]);

        val octet = firstIndex.shl(4).or(secondIndex)
        result[i.shr(1)] = octet
    }

    return result
}


