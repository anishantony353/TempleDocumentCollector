package com.saarit.templedocumentcollector.utils

import android.content.Context
import android.content.SharedPreferences
import com.saarit.templedocumentcollector.models.objects.LoginFeilds

object PrefManager {
    //Inject Context using DAGGER later
    private val TAG = PrefManager::class.java.simpleName

    private val LOGIN_PASSWORD = "login_password"
    private val LOGIN_ID =  "login_id"
    private val USER_ID =  "user_id"
    private val REMEMBER = "remember_me"

    lateinit var sharedPreference:SharedPreferences
    lateinit var editor:SharedPreferences.Editor


    fun init(context: Context){
        if(!::sharedPreference.isInitialized or !::editor.isInitialized ){
            sharedPreference = context.getSharedPreferences("pref",Context.MODE_PRIVATE)
            editor = sharedPreference.edit()

        }
    }

    fun setRemember(value: Boolean,context:Context) {
        init(context)
        editor.putBoolean(REMEMBER, value)
        editor.commit()

    }

    fun getRemember(context: Context): Boolean {
        init(context)
        return sharedPreference.getBoolean(REMEMBER, false)
    }

    fun setLoginId(loginId: String,context:Context) {
        init(context)
        editor.putString(LOGIN_ID, loginId)
        editor.commit()
    }

    fun getLoginId(context:Context): String {
        init(context)
        return sharedPreference.getString(LOGIN_ID, "")
    }

    fun setLoginPassword(loginPassword: String,context:Context) {
        init(context)
        editor.putString(LOGIN_PASSWORD, loginPassword)
        editor.commit()
    }

    fun getLoginPassword(context:Context): String {
        init(context)
        return sharedPreference.getString(LOGIN_PASSWORD, "")
    }

    fun setUserId(userId: Int,context:Context) {
        init(context)
        editor.putInt(USER_ID, userId)
        editor.commit()
    }

    fun getUserId(context:Context): Int {
        init(context)
        return sharedPreference.getInt(USER_ID, 0)
    }



}