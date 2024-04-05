package com.djamo.IntegrationApi.Controller

import com.djamo.IntegrationApi.DTO.Result
import com.djamo.IntegrationApi.Util.ResultFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

const val NESTED_EXCEPTION_DELIMITER = "; nested exception is"

abstract class AbstractController {
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(AsyncRequestTimeoutException::class)
    fun handleValidationExceptions(ex: AsyncRequestTimeoutException): Result<MutableMap<String, String?>> {
        val errors = mutableMapOf<String, String?>()
        ex.message
        return ResultFactory.getFailResult(msg=ex.message, data = errors)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleValidationExceptions(ex: MethodArgumentTypeMismatchException): Result<Map<String, String>> {
        val detailMessage = ex.message.orEmpty().substringBefore(NESTED_EXCEPTION_DELIMITER)
        val errors = mapOf(ex.name to detailMessage)
        return ResultFactory.getFailResult(msg="Validation of fields failed", data = errors)
    }
}