package com.bnyro.trivia.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bnyro.trivia.R
import com.bnyro.trivia.obj.Quiz
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * documentation can be found here: https://developer.android.com/training/data-storage/shared/documents-files#create-file
 */

class BackupHelper(
    private val activity: AppCompatActivity
) {
    fun backupQuizzes() {
        createFile()
    }

    /**
     * create a new file
     */
    fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        getCreatedFileUri.launch(intent)
    }

    /**
     * listen for a file to be created
     */
    private val getCreatedFileUri =
        activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data
                writeToDocument(uri!!)
            }
        }

    /**
     * write the text to the document
     */
    fun writeToDocument(uri: Uri) {
        try {
            val mapper = ObjectMapper()
            val data = mapper.writeValueAsBytes(PreferenceHelper.getQuizzes())
            activity.contentResolver.openFileDescriptor(uri, "w")?.use {
                FileOutputStream(it.fileDescriptor).use {
                    it.write(
                        data
                    )
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun restoreQuizzes() {
        openFile()
    }

    /**
     * open a file from the storage
     */
    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/plain"
        }
        getFileData.launch(intent)
    }

    /**
     * listen for a file being selected
     */
    val getFileData =
        activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                val text = readTextFromUri(uri)
                try {
                    // parse the raw json to a list of quizzes
                    val mapper = ObjectMapper()
                    val type = object : TypeReference<List<Quiz>>() {}
                    val quizzes = mapper.readValue(text, type)
                    quizzes.forEach {
                        // add all the quizzes to the library
                        PreferenceHelper.saveQuiz(it)
                    }
                    Toast.makeText(activity, R.string.restore_success, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(activity, R.string.restore_error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    /**
     * get the text inside the file
     */
    fun readTextFromUri(uri: Uri): String {
        val stringBuilder = StringBuilder()
        activity.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    stringBuilder.append(line)
                    line = reader.readLine()
                }
            }
        }
        return stringBuilder.toString()
    }
}
