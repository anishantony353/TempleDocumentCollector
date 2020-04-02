package com.saarit.templedocumentcollector.utils

import com.saarit.templedocumentcollector.models.dto.Temple_master_DTO

object Constant {

        val APP_URL = "http://103.233.79.142:90/temple_management2.0/portal/tm/index.php/api/"
        /*val APP_URL = "http://172.17.47.166/temple_management1.0/portal/tm/index.php/api/"*/
        lateinit var dto_temples:Temple_master_DTO

        @JvmStatic
        val TYPE_8A:String = "8A"
        @JvmStatic
        val TYPE_3A:String = "3A"
        @JvmStatic
        val TYPE_7_12:String = "7/12"
}