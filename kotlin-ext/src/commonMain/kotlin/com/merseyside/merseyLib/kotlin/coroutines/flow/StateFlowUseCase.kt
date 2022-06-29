package com.merseyside.merseyLib.kotlin.coroutines.flow

import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.coroutines.utils.defaultDispatcher
import com.merseyside.merseyLib.kotlin.coroutines.utils.uiDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class StateFlowUseCase<T, Params> : CoroutineScope by CoroutineScope(uiDispatcher) {

    var coroutineScope = CoroutineScope(uiDispatcher)
    private lateinit var stateFlow: MutableStateFlow<T>
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

    suspend fun update(params: Params? = null): Boolean {
        val value = executeAsync(params) {
            getValue(params)
        }

        return stateFlow.compareAndSet(stateFlow.value, value)
    }

    fun update(
        params: Params? = null,
        onUpdated: ((updatedValue: T) -> Unit)? = null
    ) {
        launch {
            if (update(params)) onUpdated?.invoke(stateFlow.value)
        }
    }

    protected abstract suspend fun getValue(params: Params?): T
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

    fun init(initialValue: T): StateFlow<T> {
        if (this::stateFlow.isInitialized) {
            Logger.logErr("Already initialized!")
        } else {
            MutableStateFlow(initialValue)
                .also { stateFlow = it }
        }

        return stateFlow
    }

    suspend fun init(params: Params? = null): StateFlow<T> = coroutineScope {
        val initialValue = executeAsync(params) {
            getValue(params)
        }

        init(initialValue)
    }

    fun initAndUpdate(
        initialValue: T,
        params: Params? = null,
        onUpdated: ((updatedValue: T) -> Unit)? = null
    ): StateFlow<T> {
        return init(initialValue).also {
            update(params, onUpdated)
        }
    }

    operator fun invoke(initialValue: T): StateFlow<T> {
        return init(initialValue)
    }

    protected suspend fun doWorkDeferred(params: Params? = null, block: suspend (Params?) -> T)
            : Deferred<T> = coroutineScope {
        async(asyncJob + defaultDispatcher) {
            block(params)
        }.also { job = it }
    }

    protected suspend fun executeAsync(params: Params? = null, block: suspend (Params?) -> T): T {
        return doWorkDeferred(params, block).await()
    }
}

sealed class Result<T> {

    abstract val value: T?

    class Loading<T>(
        override val value: T? = null
    ) : Result<T>()

    class Error<T>(
        val throwable: Throwable? = null,
        override val value: T? = null,
    ) : Result<T>()

    class Success<T>(override val value: T) : Result<T>()
}

