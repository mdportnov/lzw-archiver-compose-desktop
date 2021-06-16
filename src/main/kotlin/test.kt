import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

fun main() {
    val fileInput = "C:\\Users\\Mi\\IdeaProjects\\lzw-archiver\\src\\main\\kotlin\\text.txt"
    val bitLength = 16.0

//    createLZWFile(fileInput, getEncodeString(getDataFromFile(fileInput), bitLength))
//    createLZWFile(fileInput, getEncodeString(stringToBase64(getDataFromFile(fileInput)), bitLength))
    createLZWFile(fileInput, getEncodeString(encoder(fileInput), bitLength))
//    createDecodedFile(fileInput, decodeString(fileInput, bitLength))
}

fun encoder(filePath: String): String {
    var base64File = ""
    val file = File(filePath)
    try {
        FileInputStream(file).use { r ->
            val fileData = ByteArray(file.length().toInt())
            r.read(fileData)
            base64File = Base64.getEncoder().encodeToString(fileData)
        }
    } catch (e: FileNotFoundException) {
        println("File not found$e")
    } catch (ioe: IOException) {
        println("Exception while reading the file $ioe")
    }
    return base64File
}

fun fileToBase64(inputFilePath: String): String = Base64
    .getEncoder()
    .encodeToString(FileUtils.readFileToByteArray(File(inputFilePath))).also {
//        println("with base64 ${it.length}")
    }

fun base64ToString(base64String: String) = String(
    Base64
        .getDecoder()
        .decode(base64String), Charsets.UTF_8
)
