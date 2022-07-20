package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.coroutines.utils.defaultDispatcher
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow

abstract class StateFlowUseCase<T, Params>(
    val coroutineScope: CoroutineScope = CoroutineScope(uiDispatcher)
) {

    private lateinit var stateFlow: StateFlow<T>
    private val asyncJob = SupervisorJob()

    var job: Job? = null
        set(value) {
            field?.let {
                if (it.isActive) {
                    it.cancel()
                }
            }
            field = value
        }

    val isActive: Boolean
        get() {
            return job?.isActive ?: false
        }

//
//    fun toStateFlow(
//        params: Params?,
//        scope: CoroutineScope = this,
//        started: SharingStarted = SharingStarted.WhileSubscribed(),
//        initialValue: T
//    ): StateFlow<Result<T>> {
//        return flow {
//            val result = try {
//                Result.Success(doWork(params))
//            } catch (e: Exception) {
//                Result.Error(e)
//            }
//
//            emit(result)
//        }.stateIn(
//            scope,
//            started,
//            Result.Loading(initialValue)
//        )
//    }


//    suspend fun init(params: Params? = null): StateFlow<T> = coroutineScope {
//        val initialValue = executeAsync(params) {
//            getValue(params)
//        }
//
//        init(initialValue)
//    }

    fun isInitialized(): Boolean {
        return this::stateFlow.isInitialized
    }

    protected fun init(initialValue: T): StateFlow<T> {
        if (isInitialized()) {
            Logger.logErr("Already initialized!")
        } else {
            stateFlow = provideStateFlow(initialValue)
        }

        return stateFlow
    }

    fun get(): StateFlow<T> {
        return if (isInitialized()) {
            stateFlow
        } else throw IllegalStateException("Use case not initialized!")
    }

    protected abstract fun provideStateFlow(initialValue: T): StateFlow<T>

    protected suspend fun doWorkDeferred(
        params: Params? = null,
        block: suspend (Params?) -> T
    ): Deferred<T> = coroutineScope {
        async(asyncJob + defaultDispatcher) {
            block(params)
        }.also { job = it }
    }

    protected suspend fun executeAsync(params: Params? = null, block: suspend (Params?) -> T): T {
        return doWorkDeferred(params, block).await()
    }
}
