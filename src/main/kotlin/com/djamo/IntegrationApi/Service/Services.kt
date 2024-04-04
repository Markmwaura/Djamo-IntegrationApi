package com.djamo.IntegrationApi.Service

import com.djamo.IntegrationApi.DTO.ThirdPartyResponse

interface ITransactionService{
    suspend fun sendTransaction(id:String, webhookUrl:String?): ThirdPartyResponse
    suspend fun receiveWebhookRequest(id:String, status:String?): Result<Boolean>
}