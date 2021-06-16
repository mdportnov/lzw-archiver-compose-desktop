import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.pow

var defaultEncoding = Charsets.UTF_16BE

private var MAX_TABLE_SIZE = 0.0
private lateinit var LZWFilename: String

@Throws(IOException::class)
fun getEncodeString(inputString: String, bitLength: Double): MutableList<Int?> {
    MAX_TABLE_SIZE = 2.0.pow(bitLength)
    var tableSize = 255.0
    val map: MutableMap<String, Int> = HashMap()
    for (i in 0..254) map["" + i.toChar()] = i
    var initString = ""
    val encodedValues: MutableList<Int?> = ArrayList()
    for (symbol in inputString.toCharArray()) {
        val strSymbol = initString + symbol
        if (map.containsKey(strSymbol)) initString = strSymbol else {
            encodedValues.add(map[initString])
            if (tableSize < MAX_TABLE_SIZE) map[strSymbol] = tableSize++.toInt()
            initString = "" + symbol
        }
    }
    if (initString != "") encodedValues.add(map[initString])
    return encodedValues
}

@Throws(IOException::class)
fun createLZWFile(fileName: String, encodedValues: List<Int?>): Int {
    lateinit var out: BufferedWriter
    LZWFilename = fileName.substring(0, fileName.indexOf(".")) + ".lzw"
    try {
        out = BufferedWriter(
            OutputStreamWriter(
                FileOutputStream(LZWFilename),
                defaultEncoding
            )
        )
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        val iterator = encodedValues.iterator()
        while (iterator.hasNext()) {
            out.write(iterator.next()!!)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    out.flush()
    out.close()
    return File(LZWFilename).length().toInt()
}

fun getDataFromFile(fileInput: String): String {
    val fileString = StringBuffer()

    try {
        Files.newBufferedReader(Paths.get(fileInput), StandardCharsets.UTF_8).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                fileString.append(line)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return fileString.toString()
}
