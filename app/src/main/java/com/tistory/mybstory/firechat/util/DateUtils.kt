package com.tistory.mybstory.firechat.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateUtils {
    companion object {

        @JvmStatic
        fun now() = Instant.now().atZone(ZoneId.systemDefault())

        @JvmStatic
        fun formatDateToLocalDate(dateTime: String): LocalDate =
            LocalDate.parse(dateTime, DateTimeFormatter.ofPattern(DATE_FORMAT))

        @JvmStatic
        fun formatLocalDateToString(localDate: LocalDate) =
            localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

        const val DATE_FORMAT = "yyyy-MM-dd"
    }
}
