package com.saarit.templedocumentcollector.models.objects


class Temple_master {

     var temple_id: Int = 0

     lateinit var temple_name: String

    var village_id: Int = 0

    lateinit var village_name: String

    var taluka_id: Int = 0

    lateinit var taluka_name: String

    var district_id: Int = 0

    lateinit var district_name: String

    override fun toString():String{
        return temple_name
    }

    /*override fun toString(): String {
        return "Temple Id:$temple_id"+"\n"+"Temple Name:"+temple_name+"\n"+
                "Village Id:"+village_id+"\n"+"Village Name:"+village_name+"\n"+
                "Taluka Id:"+taluka_id+"\n"+"Taluka Name:"+taluka_name+"\n"+
                "District Id:"+district_id+"\n"+"District Name:"+district_name
    }*/
}