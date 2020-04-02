package com.saarit.templedocumentcollector.models.dto

import com.saarit.templedocumentcollector.models.objects.BaseResponse
import com.saarit.templedocumentcollector.models.objects.Temple_master
import com.saarit.templedocumentcollector.models.objects.UserDetail

class Temple_master_DTO : BaseResponse(0,"Fail"){

    lateinit var temple_details:List<Temple_master>

    lateinit var user_detail: UserDetail

    /*fun getTemplesDetails()=temple_details*/

}