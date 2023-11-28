package com.merseyside.merseyLib.kotlin.coroutines

import com.merseyside.merseyLib.kotlin.logger.Logger
import com.merseyside.merseyLib.kotlin.coroutines.exception.NoParamsException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class CoroutineNoResultUseCase<Params>(
    mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    executionStrategy: ExecutionStrategy = ExecutionStrategy.CANCEL_PREV_JOB
) : BaseCoroutineUseCase<Unit, Params>(mainScope, executionStrategy) {

    fun execute(
        coroutineScope: CoroutineScope = observingScope,
        onPreExecute: () -> Unit = {},
        onComplete: () -> Unit = {},
        onError: (Throwable) -> Unit = {},
        onPostExecute: () -> Unit = {},
        params: Params? = null
    ) = coroutineScope.launch {
        onPreExecute()

        try {
            executeAsync(params)
            onComplete()
        } catch (exception: CancellationException) {
            Logger.log(this@CoroutineNoResultUseCase, "The coroutine had canceled")
        } catch (exception: NoParamsException) {
            throw exception
        } catch (throwable: Throwable) {
            Logger.logErr(throwable)
            onError(throwable)
        }

        onPostExecute()
    }
}