package com.example.belsidriver.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentUtils {

    /**
     * Открывает маршрут в Яндекс.Навигаторе / Яндекс.Картах / браузере.
     * Приоритет: Навигатор → Карты → браузер.
     */
    fun openYandexMaps(context: Context, url: String) {
        val uri = Uri.parse(url)

        // 1. Try Yandex Navigator (best for driving routes)
        try {
            val naviIntent = Intent(Intent.ACTION_VIEW, uri)
            naviIntent.setPackage("ru.yandex.yandexnavi")
            context.startActivity(naviIntent)
            return
        } catch (_: ActivityNotFoundException) {}

        // 2. Fallback: Yandex Maps
        try {
            val mapsIntent = Intent(Intent.ACTION_VIEW, uri)
            mapsIntent.setPackage("ru.yandex.yandexmaps")
            context.startActivity(mapsIntent)
            return
        } catch (_: ActivityNotFoundException) {}

        // 3. Fallback: browser
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(browserIntent)
    }

    fun dialPhone(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        context.startActivity(intent)
    }
}
