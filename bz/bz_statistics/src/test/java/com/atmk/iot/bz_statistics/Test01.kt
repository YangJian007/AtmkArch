package com.atmk.iot.bz_statistics

import kotlinx.coroutines.channels.Channel

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * @author YJ
 * @date 2022-09-16
 */
class Test01 {

    @Test
    fun test01()= runBlocking {
         val channel= Channel<Int>()
        launch {
            (1..3).forEach {
                channel.send(it)
                println("send $it")
            }
            channel.close()
        }

        launch {
            for (i in channel) {
                println("receive $i")
            }
        }
        println("end")
    }



}