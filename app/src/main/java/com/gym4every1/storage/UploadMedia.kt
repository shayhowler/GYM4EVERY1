package com.gym4every1.storage

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow

suspend fun uploadMedia(supabaseClient: SupabaseClient, bucketName: String, filename: String, fileBytes: ByteArray): String? {
    val user = supabaseClient.auth.currentUserOrNull()
    if (user == null) {
        println("User not authenticated. Cannot upload file.")
        return null
    }

    val userId = user.id
    val folderName = "$userId/"

    val bucket = supabaseClient.storage.from(bucketName)

    // First, check if the file already exists and delete it if so
    try {
        val existingFiles = bucket.list(folderName) // List all files in the folder
        if (existingFiles.any { it.name == filename }) {
            bucket.delete("$folderName$filename")
            println("Deleted existing file: $filename")
        }
    } catch (e: Exception) {
        println("Error checking or deleting existing file: $e")
    }

    var publicUrl: String? = null
    try {
        // Upload the file
        bucket.uploadAsFlow("$folderName$filename", fileBytes).collect { status ->
            when (status) {
                is UploadStatus.Progress -> {
                    val progress = (status.totalBytesSend.toFloat() / status.contentLength) * 100
                    println("Upload Progress: $progress%")
                }
                is UploadStatus.Success -> {
                    println("Upload Successful!")
                    // Get the public URL after successful upload
                    publicUrl = bucket.publicUrl("$folderName$filename")
                }
            }
        }
    } catch (e: Exception) {
        println("Error uploading file: $e")
    }

    return publicUrl
}