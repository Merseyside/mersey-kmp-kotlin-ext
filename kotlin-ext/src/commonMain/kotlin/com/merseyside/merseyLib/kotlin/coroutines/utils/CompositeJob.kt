package com.merseyside.merseyLib.kotlin.coroutines.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

class CompositeJob {

    private val jobList = mutableListOf<Job>()

    suspend fun <J : Job> add(block: suspend () -> J): J {
        val job = block()
        add(job)
        return job
    }

    fun add(vararg jobs: Job) {
        jobs.forEach { job ->
            job.invokeOnCompletion {
                jobList.remove(job)
            }
        }

        jobList.addAll(jobs)
    }

    val isActive: Boolean
        get() = jobList.isNotEmpty()

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