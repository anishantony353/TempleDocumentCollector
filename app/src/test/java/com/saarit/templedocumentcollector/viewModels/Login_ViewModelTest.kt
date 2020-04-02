package com.saarit.templedocumentcollector.viewModels

import android.app.Application
import android.content.SharedPreferences
import androidx.databinding.ObservableField
import com.saarit.templedocumentcollector.models.objects.LoginFeilds
import com.saarit.templedocumentcollector.utils.PrefManager
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.exceptions.base.MockitoInitializationException
import org.mockito.junit.MockitoJUnitRunner
import kotlin.reflect.KClass

@RunWith(MockitoJUnitRunner::class)
abstract class Login_ViewModelTest {

    @Mock
    var appContext = Application()
    @Mock
    var prefManager = PrefManager

     var sharedPref = Mockito.mock(SharedPreferences::class.java)

    var viewmodel = Login_ViewModel(appContext)


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

    }

    @After
    fun tearDown() {
    }

    @Test
    fun `check if set remember me sets correct values`(){
        Mockito.`when`(appContext.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPref)
        Mockito.`when`(prefManager.getRemember(appContext)).thenReturn(true)
        Mockito.`when`(prefManager.getLoginId(appContext)).thenReturn("id_a")
        Mockito.`when`(prefManager.getLoginPassword(appContext)).thenReturn("password_a")



        viewmodel.setRememberMeCheckBox()

        assertEquals("id_a",(viewmodel.loginFeildsObservable.get()!!).id)

    }

    /*@Test
    fun init_Test(){
        @Mock
        var loginFeildsObservable = ObservableField<LoginFeilds>()
        viewmodel.loginFeildsObservable = loginFeildsObservable

        viewmodel.init()
        Mockito.verify(loginFeildsObservable).set(any())
        *//*assertEquals("Id",(viewmodel.loginFeildsObservable.get()!!).id)*//*
    }*/
}