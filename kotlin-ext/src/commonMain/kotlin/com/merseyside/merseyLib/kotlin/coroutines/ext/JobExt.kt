package com.merseyside.merseyLib.kotlin.coroutines.ext

import kotlinx.coroutines.Job


fun Job.isLaunched(): Boolean {
    return isActive || isCompleted
}