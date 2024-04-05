package com.djamo.IntegrationApi.Service

import com.djamo.IntegrationApi.DTO.Result
import com.djamo.IntegrationApi.DTO.ThirdPartyResponse

interface IThirdPartyTransactionService{
    suspend fun sendDataToThirdParty(id:String, webhookUrl:String?): ThirdPartyResponse
    suspend fun receiveWebhookRequest(id:String, status:String?): Result<Boolean>

}