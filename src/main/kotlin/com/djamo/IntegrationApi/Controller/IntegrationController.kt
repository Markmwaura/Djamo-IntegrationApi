package com.djamo.IntegrationApi.Controller

import com.djamo.IntegrationApi.DTO.Result
import com.djamo.IntegrationApi.DTO.ThirdPartyResponse
import com.djamo.IntegrationApi.DTO.TransactionRequest
import com.djamo.IntegrationApi.DTO.WebhookDTO
import com.djamo.IntegrationApi.Service.IThirdPartyTransactionService
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
    lateinit var iThirdPartyTransactionService: IThirdPartyTransactionService

    @PostMapping(value = ["/transaction"], produces = ["application/json"])
    @Operation(summary = "Receive payload and send to ThirdParty API")
    suspend fun sendDataToThirdPartyApi(@RequestBody dto: TransactionRequest): ThirdPartyResponse {
       val thirdPartyRes = CoroutineScope(Dispatchers.IO).async { iThirdPartyTransactionService.sendDataToThirdParty(id = dto.id, webhookUrl = "") }
        return thirdPartyRes.await()
    }

    @PostMapping(value = ["/transaction/webhook"], produces = ["application/json"])
    @Operation(summary = "Receive Webhook request")
    suspend fun receiveWebhook(@RequestBody dto: WebhookDTO): Result<Boolean> {
        return iThirdPartyTransactionService.receiveWebhookRequest(id = dto.id, status = dto.status)
    }

}