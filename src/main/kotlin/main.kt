import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.io.File

fun main() {
    Window(
        centered = true
    ) {
        var text by remember { mutableStateOf("") }
        var filePath by remember { mutableStateOf("") }
        val window = LocalAppWindow.current
        val fileDialog = java.awt.FileDialog(window.window)
        val bitLength = 16.0
        window.setSize(500, 400)

        Column {
            Text(
                text, modifier = Modifier.padding(5.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                onClick = {
                    fileDialog.isVisible = true
                    fileDialog.files?.let {
                        if (it.isNotEmpty())
                            filePath = it[0].toString()
                    }
                }) {
                Text("Tap to select file!")
            }

            Row(
                modifier = Modifier.padding(5.dp)
            ) {

                if (filePath.isNotEmpty())
                    Button(
                        modifier = Modifier.padding(5.dp),
                        onClick = {
                            var newSize: Int
                            createLZWFile(filePath, getEncodeString(encoder(filePath), bitLength)).also {
                                newSize = it
                            }
                            text =
                                "Successfully Compressed,\ncompression ratio: ${File(filePath).length() / newSize.toDouble()}"
                        }) {
                        Text("Compress!")
                    }

                if (filePath.isNotEmpty())
                    Button(
                        modifier = Modifier.padding(5.dp),
                        onClick = {
                            createDecodedFile(
                                filePath,
                                base64ToString(decodeString(filePath, bitLength)).also { println(it) })
                            text = "Successfully decompressed"
                        }) {
                        Text("Decompress!")
                    }
            }
            Text(filePath, textAlign = TextAlign.Center, modifier = Modifier.padding(5.dp))
        }
    }
}
