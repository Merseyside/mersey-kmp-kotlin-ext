package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import kotlinx.coroutines.*

abstract class CoroutineUseCase<T, Params>(
    executionStrategy: ExecutionStrategy = ExecutionStrategy.CANCEL_PREV_JOB
) : BaseCoroutineUseCase<T, Params>(executionStrategy) {

    fun execute(
        coroutineScope: CoroutineScope = mainScope,
        onPreExecute: () -> Unit = {},
        onComplete: (T) -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onPostExecute: () -> Unit = {},
        params: Params? = null
    ) = coroutineScope.launch {
        onPreExecute()
        try {
            onComplete(executeAsync(params))
        } catch (exception: CancellationException) {
            Logger.logErr(this@CoroutineUseCase, "The coroutine had canceled")
        } catch (exception: NoParamsException) {
            throw exception
        } catch (throwable: Throwable) {
            Logger.logErr(throwable)
            onError(throwable)
            throwable.printStackTrace()
        }

        onPostExecute()
    }
}