import java.io.*
import kotlin.math.pow

private var MAX_TABLE_SIZE = 0.0
private var LZWFilename: String? = null

@Throws(IOException::class)
fun decodeString(fileInput: String, bitLength: Double): String {
    MAX_TABLE_SIZE = 2.0.pow(bitLength)
    val getCompressValues: MutableList<Int> = ArrayList()
    var tableSize = 255

    val inputStream: InputStream = FileInputStream(fileInput)
    val inputStreamReader: Reader = InputStreamReader(
        inputStream,
        defaultEncoding
    )

    val br: BufferedReader?
    br = BufferedReader(inputStreamReader)
    var value: Double

    while (br.read().also { value = it.toDouble() } != -1) {
        getCompressValues.add(value.toInt())
    }

    br.close()
    val map: MutableMap<Int, String> = HashMap()
    for (i in 0..254) map[i] = "" + i.toChar()
    var encodeValues: String? = "" + getCompressValues.removeAt(0).toChar()
    val decodedValues = StringBuffer(encodeValues)
    var getValueFromTable = ""
    for (check_key in getCompressValues) {
        if (map.containsKey(check_key))
            getValueFromTable = map[check_key]!!
        else if (check_key == tableSize)
            getValueFromTable = encodeValues + encodeValues!![0]
        decodedValues.append(getValueFromTable)
        if (tableSize < MAX_TABLE_SIZE)
            map[tableSize++] = encodeValues + getValueFromTable[0]
        encodeValues = getValueFromTable
    }
    return decodedValues.toString()
}

@Throws(IOException::class)
fun createDecodedFile(fileName: String, decodedValues: String) {
    LZWFilename = fileName.substring(0, fileName.indexOf(".")) + "_decoded.txt"
    val writer = FileWriter(LZWFilename, false)
    val bufferedWriter = BufferedWriter(writer)
    try {
        bufferedWriter.write(decodedValues)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    bufferedWriter.flush()
    bufferedWriter.close()
}