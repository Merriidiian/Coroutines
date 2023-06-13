@file:OptIn(DelicateCoroutinesApi::class)

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.*


var count:Int = 0

suspend fun main() {
    val count:Int = 0
    val urls: MutableList<String> = mutableListOf()
    for (i in 0..100)
    {
        urls.add("https://download.samplelib.com/jpeg/sample-clouds-400x300.jpg")
    }
    //С оригинальным названием
    val jobsOriginal = urls.map { url ->
        GlobalScope.async {
            val fileName= url.substringAfterLast("/")
            downloadAndSaveImage(url, if (fileName.endsWith(".jpg")) fileName else "$fileName.jpg")
        }
    }
    jobsOriginal.awaitAll()
    //С вводимым названием
    val jobsRename = urls.map { url ->
        GlobalScope.async {
            val fileName= "ТипоНазвание"
            downloadAndSaveImage(url, if (fileName.endsWith(".jpg")) fileName else "$fileName.jpg")
        }
    }
    jobsRename.awaitAll()
}
suspend fun downloadAndSaveImage(url: String, fileName: String) {
    val client = HttpClient(CIO)
    val imageData= client.get(url).body<ByteArray>()
    client.close()
    saveImageToFile(imageData, "Photo/$count$fileName")
    count++
}
suspend fun saveImageToFile(imageData: ByteArray, fileName: String) {
    val file = java.io.File(fileName)
    file.writeBytes(imageData)
}