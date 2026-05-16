package com.demonlab.lune

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.demonlab.lune.tools.AudioThumbnailFetcher

class LuneApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(AudioThumbnailFetcher.Factory(this@LuneApplication))
            }
            .crossfade(true)
            .build()
    }
}
