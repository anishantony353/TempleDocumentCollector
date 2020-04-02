package com.saarit.templedocumentcollector.viewModels

import com.saarit.templedocumentcollector.ui.adapters.ListAdapter
import com.saarit.templedocumentcollector.utils.Util
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.runners.MockitoJUnitRunner
import java.util.*
import kotlin.collections.ArrayList

@RunWith(MockitoJUnitRunner::class)
class List_ViewModelTest {
    val TAG = List_ViewModelTest::class.java.simpleName
    @Mock
    lateinit var listAdapter:ListAdapter

    var list = ArrayList<String>(Arrays.asList("1675","6754","1463","9734"))
    var obj = List_ViewModel()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

    }

    @After
    fun tearDown() {

    }

    @Test
    fun checkIf_init_throws_exception(){

       /*listAdapter.setData(list)
        Mockito.verify(listAdapter).setData(list)
        assertThat(2,`is`(2))*/

    }

}