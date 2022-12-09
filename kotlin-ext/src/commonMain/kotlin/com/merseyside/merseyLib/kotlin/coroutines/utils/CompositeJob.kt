package com.merseyside.merseyLib.kotlin.coroutines.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll

class CompositeJob {

    private val jobList = mutableListOf<Job>()

    val isActive: Boolean
        get() = jobList.isNotEmpty()

    suspend fun <J : Job> add(block: suspend () -> J): J {
        val job = block()
        add(job)
        return job
    }

    fun add(vararg jobs: Job) {
        jobList.addAll(jobs)
    }

    suspend fun joinAll() {
        jobList.joinAll()
    }

    /**
     * @return true if there is was at least one active job before it has been cancelled.
     */
    fun cancel(cause: CancellationException? = null): Boolean {
        val saved = isActive

        jobList.forEach { it.cancel(cause) }
        jobList.clear()

        return saved
    }
}