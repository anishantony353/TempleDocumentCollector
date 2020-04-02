package com.saarit.templedocumentcollector.models.repositories.server

import com.saarit.templedocumentcollector.models.repositories.server.ApiClient
import com.saarit.templedocumentcollector.models.repositories.server.ApiProvider

object Repo_server {

    var apiClient:ApiClient = ApiProvider.getClient()

}