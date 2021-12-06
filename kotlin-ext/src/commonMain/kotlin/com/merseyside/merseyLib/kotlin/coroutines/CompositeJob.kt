package com.merseyside.merseyLib.kotlin.coroutines

import kotlinx.coroutines.Job

class CompositeJob {

    private val jobs = mutableListOf<Job>()

    fun add(vararg job: Job) {
        jobs.addAll(job)
    }

    fun cancel() {
        jobs.forEach { it.cancel() }
        jobs.clear()
    }
}