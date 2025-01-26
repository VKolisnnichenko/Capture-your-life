package com.vkolisnichenko.core.domain.usecases.base

import com.vkolisnichenko.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseUseCase<out Type, in Params>  {

    abstract suspend fun run(params: Params): Either<Type>

    operator fun invoke(
        params: Params,
        scope: CoroutineScope = CoroutineScope(Job()),
        onResult: (Either<Type>) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                run(params)
            }
            onResult(deferred.await())
        }
    }
}