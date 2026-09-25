package com.kuroi.guardplant.core.presentation

import android.content.res.Resources
import androidx.annotation.StringRes
import com.kuroi.guardplant.R
import com.kuroi.guardplant.core.model.FertilizingGuidance
import com.kuroi.guardplant.core.model.FertilizingInterval
import com.kuroi.guardplant.core.model.FertilizingStrategy
import com.kuroi.guardplant.core.model.GrowthHabit
import com.kuroi.guardplant.core.model.GrowthRate
import com.kuroi.guardplant.core.model.HumidityLevel
import com.kuroi.guardplant.core.model.InactiveGrowthAction
import com.kuroi.guardplant.core.model.LightLevel
import com.kuroi.guardplant.core.model.MatureSize
import com.kuroi.guardplant.core.model.RepottingAction
import com.kuroi.guardplant.core.model.RepottingGuidance
import com.kuroi.guardplant.core.model.RepottingTiming
import com.kuroi.guardplant.core.model.SeasonalContext
import com.kuroi.guardplant.core.model.WateringGuidance
import com.kuroi.guardplant.core.model.WateringMethod
import com.kuroi.guardplant.core.model.WateringMode
import com.kuroi.guardplant.core.model.WateringTrigger

@StringRes
fun LightLevel.labelResource(): Int = when (this) {
    LightLevel.LOW -> R.string.light_low
    LightLevel.MEDIUM_INDIRECT -> R.string.light_medium_indirect
    LightLevel.BRIGHT_INDIRECT -> R.string.light_bright_indirect
    LightLevel.DIRECT -> R.string.light_direct
}

@StringRes
fun GrowthRate.labelResource(): Int = when (this) {
    GrowthRate.SLOW -> R.string.growth_rate_slow
    GrowthRate.MEDIUM -> R.string.growth_rate_medium
    GrowthRate.FAST -> R.string.growth_rate_fast
}

@StringRes
fun GrowthHabit.labelResource(): Int = when (this) {
    GrowthHabit.UPRIGHT -> R.string.growth_habit_upright
    GrowthHabit.TRAILING -> R.string.growth_habit_trailing
    GrowthHabit.CLIMBING -> R.string.growth_habit_climbing
    GrowthHabit.CLUMPING -> R.string.growth_habit_clumping
    GrowthHabit.ROSETTE -> R.string.growth_habit_rosette
    GrowthHabit.BUSHY -> R.string.growth_habit_bushy
}

@StringRes
fun HumidityLevel.labelResource(): Int = when (this) {
    HumidityLevel.AVERAGE -> R.string.humidity_average
    HumidityLevel.MODERATE -> R.string.humidity_moderate
    HumidityLevel.HIGH -> R.string.humidity_high
}

@StringRes
fun MatureSize.labelResource(): Int = when (this) {
    MatureSize.COMPACT -> R.string.mature_size_compact
    MatureSize.MEDIUM -> R.string.mature_size_medium
    MatureSize.LARGE -> R.string.mature_size_large
}

fun wateringText(resources: Resources, guidance: WateringGuidance): String {
    val schedule = when (guidance.mode) {
        WateringMode.SUBSTRATE_CHECK -> resources.getString(requireNotNull(guidance.trigger).labelResource())
        WateringMode.FIXED_INTERVAL -> resources.getString(
            R.string.watering_fixed_interval,
            requireNotNull(guidance.intervalDays),
        )
    }
    val parts = buildList {
        add(schedule)
        add(resources.getString(guidance.method.labelResource()))
        if (guidance.seasonalContext == SeasonalContext.ACTIVE_GROWTH) {
            add(resources.getString(R.string.watering_active_growth))
        }
        if (guidance.drainageRequired) add(resources.getString(R.string.watering_drainage_required))
    }
    return parts.joinToString(separator = ". ", postfix = ".")
}

@StringRes
private fun WateringTrigger.labelResource(): Int = when (this) {
    WateringTrigger.SURFACE_DRYING -> R.string.watering_surface_drying
    WateringTrigger.TOP_QUARTER_DRY -> R.string.watering_top_quarter_dry
    WateringTrigger.TOP_THIRD_DRY -> R.string.watering_top_third_dry
    WateringTrigger.TOP_HALF_DRY -> R.string.watering_top_half_dry
    WateringTrigger.ALMOST_COMPLETELY_DRY -> R.string.watering_almost_completely_dry
    WateringTrigger.COMPLETELY_DRY -> R.string.watering_completely_dry
    WateringTrigger.BARK_ALMOST_DRY -> R.string.watering_bark_almost_dry
    WateringTrigger.MOUNTING_MEDIUM_NEARLY_DRY -> R.string.watering_mounting_medium_nearly_dry
}

@StringRes
private fun WateringMethod.labelResource(): Int = when (this) {
    WateringMethod.THOROUGH -> R.string.watering_method_thorough
    WateringMethod.BOTTOM_WATER -> R.string.watering_method_bottom
    WateringMethod.SOAK_ROOTS_AND_DRAIN -> R.string.watering_method_soak_roots
    WateringMethod.SOAK_AND_DRAIN -> R.string.watering_method_soak_and_drain
    WateringMethod.SOAK_AND_DRY_WITHIN_4_HOURS -> R.string.watering_method_soak_and_dry
    WateringMethod.WATER_SOIL_AVOID_CROWN -> R.string.watering_method_avoid_crown
}

fun fertilizingText(resources: Resources, guidance: FertilizingGuidance): String {
    if (guidance.strategy == FertilizingStrategy.NONE) {
        return resources.getString(R.string.fertilizing_none)
    }

    val activeInterval = intervalText(resources, guidance.activeGrowth)
    val active = when (guidance.strategy) {
        FertilizingStrategy.SEASONAL -> resources.getString(R.string.fertilizing_seasonal, activeInterval)
        FertilizingStrategy.YEAR_ROUND -> resources.getString(R.string.fertilizing_year_round, activeInterval)
        FertilizingStrategy.NONE -> error("Handled above")
    }
    val inactive = when (guidance.inactiveGrowth.action) {
        InactiveGrowthAction.PAUSE -> resources.getString(R.string.fertilizing_pause)
        InactiveGrowthAction.REDUCE -> resources.getString(
            R.string.fertilizing_reduce,
            intervalText(
                resources,
                FertilizingInterval(
                    guidance.inactiveGrowth.minimumIntervalDays,
                    guidance.inactiveGrowth.maximumIntervalDays,
                ),
            ),
        )
        InactiveGrowthAction.SAME -> resources.getString(R.string.fertilizing_same)
    }
    return "$active. $inactive."
}

private fun intervalText(resources: Resources, interval: FertilizingInterval): String {
    val minimum = requireNotNull(interval.minimumIntervalDays)
    val maximum = requireNotNull(interval.maximumIntervalDays)
    return if (minimum == maximum) {
        resources.getString(R.string.interval_every_days, minimum)
    } else {
        resources.getString(R.string.interval_every_days_range, minimum, maximum)
    }
}

fun repottingText(resources: Resources, guidance: RepottingGuidance): String {
    if (guidance.action == RepottingAction.NONE) return resources.getString(R.string.repot_none)

    val minimum = requireNotNull(guidance.minimumIntervalMonths)
    val maximum = requireNotNull(guidance.maximumIntervalMonths)
    val interval = intervalMonthsText(resources, guidance.action, minimum, maximum)
    val timing = when (guidance.timing) {
        RepottingTiming.AFTER_FLOWERING -> resources.getString(R.string.repot_after_flowering)
        RepottingTiming.BEFORE_ACTIVE_GROWTH -> resources.getString(R.string.repot_before_active_growth)
        null -> null
    }
    return listOfNotNull(interval, timing).joinToString(separator = ". ", postfix = ".")
}

private fun intervalMonthsText(
    resources: Resources,
    action: RepottingAction,
    minimumMonths: Int,
    maximumMonths: Int,
): String {
    val wholeYears = minimumMonths % 12 == 0 && maximumMonths % 12 == 0
    val minimum = if (wholeYears) minimumMonths / 12 else minimumMonths
    val maximum = if (wholeYears) maximumMonths / 12 else maximumMonths
    return when {
        action == RepottingAction.REPOT && wholeYears && minimum == 1 && maximum == 1 ->
            resources.getString(R.string.repot_every_year)
        action == RepottingAction.REPOT && wholeYears && minimum == maximum ->
            resources.getString(R.string.repot_every_years, minimum)
        action == RepottingAction.REPOT && wholeYears ->
            resources.getString(R.string.repot_every_year_range, minimum, maximum)
        action == RepottingAction.REPOT && minimum == maximum ->
            resources.getString(R.string.repot_every_months, minimum)
        action == RepottingAction.REPOT ->
            resources.getString(R.string.repot_every_month_range, minimum, maximum)
        action == RepottingAction.REMOUNT && wholeYears && minimum == maximum ->
            resources.getString(R.string.remount_every_years, minimum)
        action == RepottingAction.REMOUNT && wholeYears ->
            resources.getString(R.string.remount_every_year_range, minimum, maximum)
        action == RepottingAction.REMOUNT && minimum == maximum ->
            resources.getString(R.string.remount_every_months, minimum)
        action == RepottingAction.REMOUNT ->
            resources.getString(R.string.remount_every_month_range, minimum, maximum)
        else -> error("Repotting action must have an interval")
    }
}

fun speciesAccent(speciesId: String): Long {
    val palette = longArrayOf(
        0xFF3F7D55,
        0xFF7C9A3D,
        0xFF4F6B3C,
        0xFF88A95A,
        0xFF5E8C78,
        0xFF2F6A54,
        0xFF6C8F65,
        0xFF8C6F4F,
    )
    return palette[(speciesId.hashCode() and Int.MAX_VALUE) % palette.size]
}
