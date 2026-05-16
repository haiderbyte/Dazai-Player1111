package com.demonlab.lune.tools

import android.content.Context
import android.net.Uri
import android.util.Log
import com.demonlab.lune.data.MusicDatabase
import com.demonlab.lune.data.SongOverride
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MetadataManager(private val context: Context) {
    private val database = MusicDatabase.getDatabase(context)

    suspend fun updateSongMetadata(
        songId: Long,
        title: String,
        artist: String,
        album: String,
        genre: String?,
        coverUri: String?
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            var finalCoverUri = coverUri
            if (coverUri != null && !coverUri.startsWith("file://${context.filesDir}")) {
                val localUri = saveCustomCover(songId, Uri.parse(coverUri))
                if (localUri != null) {
                    finalCoverUri = localUri.toString()
                }
            }

            val existing = database.songOverrideDao().getOverrideForSong(songId)
            val override = SongOverride(
                songId = songId,
                title = title,
                artist = artist,
                album = album,
                genre = genre,
                coverUri = finalCoverUri,
                isFavorite = existing?.isFavorite ?: false
            )
            database.songOverrideDao().insertOverride(override)
            true
        } catch (e: Exception) {
            Log.e("MetadataManager", "Error saving metadata to Room", e)
            false
        }
    }

    suspend fun updateFavoriteStatus(songId: Long, isFavorite: Boolean): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val existing = database.songOverrideDao().getOverrideForSong(songId)
            if (existing != null) {
                database.songOverrideDao().insertOverride(existing.copy(isFavorite = isFavorite))
            } else {
                database.songOverrideDao().insertOverride(SongOverride(songId = songId, isFavorite = isFavorite))
            }
            true
        } catch (e: Exception) {
            Log.e("MetadataManager", "Error updating favorite status", e)
            false
        }
    }

    suspend fun clearMetadataOverride(songId: Long): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val existing = database.songOverrideDao().getOverrideForSong(songId)
            if (existing != null) {
                // Delete custom cover if it exists
                val coversDir = java.io.File(context.filesDir, "covers")
                val coverFile = java.io.File(coversDir, "cover_$songId.jpg")
                if (coverFile.exists()) {
                    coverFile.delete()
                }

                // Keep only the songId and isFavorite status, clear other metadata
                val clearedOverride = SongOverride(
                    songId = songId,
                    isFavorite = existing.isFavorite
                )
                database.songOverrideDao().insertOverride(clearedOverride)
            }
            true
        } catch (e: Exception) {
            Log.e("MetadataManager", "Error clearing metadata override", e)
            false
        }
    }

    private fun saveCustomCover(songId: Long, imageUri: Uri): Uri? {
        return try {
            val coversDir = java.io.File(context.filesDir, "covers")
            if (!coversDir.exists()) {
                coversDir.mkdirs()
            }
            val coverFile = java.io.File(coversDir, "cover_$songId.jpg")
            context.contentResolver.openInputStream(imageUri)?.use { input ->
                java.io.FileOutputStream(coverFile).use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(coverFile)
        } catch (e: Exception) {
            Log.e("MetadataManager", "Error saving custom cover", e)
            null
        }
    }
}
