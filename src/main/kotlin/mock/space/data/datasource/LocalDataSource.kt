package mock.space.data.datasource

import mock.space.data.utils.Constants
import mock.space.data.utils.Constants.Companion.NOTES_FOLDER_SUFIX
import mock.space.data.utils.Constants.Companion.PREFIX_FOLDER
import mock.space.data.utils.Constants.Companion.RULES_URL_FOLDER_SUFIX
import mock.space.data.model.ResponseType
import mock.space.domain.model.CustomResponse
import mock.space.domain.model.ListenModeModel
import io.ktor.http.*
import mock.space.data.utils.Constants.Companion.RULES_BODY_FOLDER_SUFIX
import java.io.File
import java.io.FileWriter

class LocalDataSource {

    var listenModeModel = ListenModeModel()

    private fun getNotesFile(id: Int): File{
        return File(PREFIX_FOLDER+id.toString()+NOTES_FOLDER_SUFIX)
    }


    private fun getBodyRulesFile(): File{
        return File(PREFIX_FOLDER+RULES_BODY_FOLDER_SUFIX)
    }

    private fun getUrlRulesFile(): File{
        return File(PREFIX_FOLDER+RULES_URL_FOLDER_SUFIX)
    }

    fun saveBodyRules(text: String){
        val file = getBodyRulesFile()
        val writer = FileWriter(file)
        writer.write(text)
        writer.close()
    }

    fun saveUrlRules(text: String){
        val file = getUrlRulesFile()
        val writer = FileWriter(file)
        writer.write(text)
        writer.close()
    }

    fun getBodyRules(): List<String>{
        val lines = mutableListOf<String>()
        try {
            getBodyRulesFile().readText().lines().forEach { line ->
                lines.add(line)
            }
            return lines
        } catch (e:Exception){
            System.out.println(e.printStackTrace())
            return lines
        }
    }

    fun getUrlRules(): List<String>{
        val lines = mutableListOf<String>()
        try {
            getUrlRulesFile().readText().lines().forEach { line ->
                lines.add(line)
            }
            return lines
        } catch (e:Exception){
            System.out.println(e.printStackTrace())
            return lines
        }
    }

    fun saveNote(text: String, id: Int){
        val file = getNotesFile(id)
        val writer = FileWriter(file)
        writer.write(text)
        writer.close()
    }

    fun getNote(id: Int): String{
        try {
            return getNotesFile(id).readText()
        } catch (e:Exception){
            System.out.println(e.printStackTrace())
            return ""
        }
    }

    fun readResponse(path: String) = CustomResponse(
            readMockedBodyResponse(path),
            readMockedStatusResponse(path),
            readMockedHeadersResponse(path)
        )

    private fun readMockedBodyResponse(path: String): String {
        return File(path + ResponseType.BODY.sufix).readText()
    }

    private fun readMockedStatusResponse(path: String): HttpStatusCode {
        var i: Int = 0
        var code = 0
        var msg = ""
        File(path + ResponseType.STATUS.sufix).forEachLine { text ->
            when(i){
                0 -> code = text.toInt()
                1 -> msg = text
            }
            i++
        }
        return HttpStatusCode(code, msg)
    }

    private fun readMockedHeadersResponse(path: String): Headers {
        val text = File(path + ResponseType.HEADER.sufix).readText()
        val map = hashMapOf<String, List<String>>()

        text.lines().forEach { line ->
            if (line.contains(Regex("[\\[\\]:]"))) {
                val splitLine = line.split(":", "[", "]")
                val key = splitLine[0]
                val values = splitLine[2].split(", ")

                map[key] = values
            }
        }
        val headers = Headers.build {
            for (entry in map) {
                appendAll(entry.key, entry.value)
            }
        }
        return headers
    }

    fun saveResponse(path: String, body: String?, headers: Headers, status: HttpStatusCode) {
        println("Path is " + path)

        if (!path.contains("/")) {
            println("Path should contains a slash.")
            return
        }
        println("Path " + path)
        val folders = path.split("/")
        var currentPath = ""
        for ((index, folder) in folders.withIndex()) {
            currentPath += "$folder/"
            val file = File(currentPath)
            System.out.println("Folder size " + folders.size)
            System.out.println("Folder name " + currentPath)
            if (folders.get(index).isNotEmpty()) {
                if (index < folders.size - 1) {
                    file.mkdir()
                    System.out.println(index.toString() + "- Created dir" + file.absolutePath)
                } else {
                    if (currentPath.last() == '/') {
                        currentPath = currentPath.dropLast(1)
                    }
                    System.out.println("Is the last one. " + currentPath)
                    body?.let {
                        createBodyFile(currentPath, body)
                    }
                    createHeadersFile(currentPath, headers)
                    createStatusFile(currentPath, status)
                }
            }
        }
        // val fileName = folders.last()

    }

    fun createBodyFile(currentPath: String, content: String) {
        val pathname = currentPath + ResponseType.BODY.sufix
        System.out.println("Creating file " + pathname)
        val file = File(pathname)
        file.delete()
        val writer = FileWriter(file)
        writer.write(content)
        writer.close()
    }

    fun createStatusFile(currentPath: String, content: HttpStatusCode) {
        val file = File(currentPath + ResponseType.STATUS.sufix)
        file.delete()
        val writer = FileWriter(file, true)
        writer.write(content.value.toString())
        writer.write("\n")
        writer.write(content.description)
        writer.close()
    }

    fun createHeadersFile(currentPath: String, content: Headers) {
        val file = File(currentPath + ResponseType.HEADER.sufix)
        file.delete()
        val writer = FileWriter(file, true)
        content.forEach { key, value ->
                writer.write("$key:$value")
                writer.write("\n")
        }
        writer.close()
    }

    fun getLastFolderNumber(): Int {
        var lastNumber = 0
        val rootFile = File(Constants.PREFIX_FOLDER)
        rootFile.walk()
            .filterNot { it.absolutePath == rootFile.absolutePath }
            .filter { it.isDirectory }
            .forEach {
                if (it.name.all { it.isDigit() } && it.name.toInt() > lastNumber) {
                    lastNumber = it.name.toInt()
                }
            }
        return lastNumber
    }

}