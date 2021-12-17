package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.Logger
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class FlowUseCase<T, Params> : CoroutineScope by CoroutineScope(applicationContext) {
    var job: Job? = null
        set(value) {
            field?.let {
                if (it.isActive) {
                    it.cancel()
                }
            }

            field = value
        }

    var backgroundContext: CoroutineContext = computationContext

    @ExperimentalCoroutinesApi
    protected abstract fun executeOnBackground(params: Params?): Flow<T>

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observe(
        coroutineScope: CoroutineScope = this,
        params: Params? = null,
        onEmit: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ): Job {
        val flow = executeOnBackground(params)
            .flowOn(backgroundContext)

        return coroutineScope.launch {
            try {
                flow.collect { data ->
                    onEmit.invoke(data)
                }
            } catch (e: CancellationException) {
                Logger.log(this@FlowUseCase, "Coroutine had canceled")
            } catch (exception: NoParamsException) {
                throw exception
            }
            catch (e: Throwable) {
                Logger.log(e)
                onError.invoke(e)
            }
        }.also { job = it }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(params: Params? = null): Flow<T> {
        return executeOnBackground(params)
    }
}