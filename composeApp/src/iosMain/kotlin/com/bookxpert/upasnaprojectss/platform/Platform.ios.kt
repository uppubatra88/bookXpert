package com.bookxpert.upasnaprojectss.platform

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

actual fun formatDate(millis: Long): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "dd MMM yyyy"
    return formatter.stringFromDate(NSDate(millis / 1000.0))
}
