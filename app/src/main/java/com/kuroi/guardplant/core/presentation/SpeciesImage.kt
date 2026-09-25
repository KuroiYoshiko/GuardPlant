package com.kuroi.guardplant.core.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes

// Drawable names intentionally mirror stable catalogue IDs, avoiding a duplicate 70-entry map.
@SuppressLint("DiscouragedApi")
@DrawableRes
fun speciesImageResource(context: Context, speciesId: String): Int =
    context.resources.getIdentifier(speciesId, "drawable", context.packageName)
