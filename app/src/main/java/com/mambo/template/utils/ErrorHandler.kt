package com.mambo.template.utils

import android.accounts.NetworkErrorException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandler {
//    companion object{
//        fun resolveError(e: Exception): Status.ERROR {
//            var error = e
//
//            when (e) {
//                is SocketTimeoutException -> {
//                    error = NetworkErrorException(message = "connection error!")
//                }
//                is ConnectException -> {
//                    error = NetworkErrorException(message = "no internet access!")
//                }
//                is UnknownHostException -> {
//                    error = NetworkErrorException(message = "no internet access!")
//                }
//            }
//
//            if(e is HttpException){
//                when(e.code()){
//                    502 -> {
//                        error = NetworkErrorException(e.code(),  "internal error!")
//                    }
//                    401 -> {
//                        throw AuthenticationException("authentication error!")
//                    }
//                    400 -> {
//                        error = NetworkErrorException.parseException(e)
//                    }
//                }
//            }
//
//
//            return State.ErrorState(error)
//        }
//    }
}