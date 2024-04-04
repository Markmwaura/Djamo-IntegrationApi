package com.djamo.IntegrationApi.Controller

import com.djamo.IntegrationApi.DTO.ThirdPartyResponse
import com.djamo.IntegrationApi.DTO.TransactionRequest
import com.djamo.IntegrationApi.DTO.WebhookDTO
import com.djamo.IntegrationApi.Service.ITransactionService
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/integration")
class IntegrationController: AbstractController() {

    @Autowired
    lateinit var transactionService: ITransactionService

    @PostMapping(value = ["/transaction"], produces = ["application/json"])
    @Operation(summary = "Receive transaction and send to ThirdParty APi")
    suspend fun transactionApi(@RequestBody dto: TransactionRequest): ThirdPartyResponse {
       val beneficiariesCoroutine = CoroutineScope(Dispatchers.IO).async { transactionService.sendTransaction(id = dto.id, webhookUrl = "") }
        val beneficiaries = beneficiariesCoroutine.await()
        return beneficiaries
    }

    @PostMapping(value = ["/transaction/webhook"], produces = ["application/json"])
    @Operation(summary = "Receive Webhook request")
    suspend fun transactionWebhookApi(@RequestBody dto: WebhookDTO): Result<Boolean> {
        return transactionService.receiveWebhookRequest(id = dto.id, status = dto.status)
    }


}