package appfactory.uwp.edu.parksideapp2.API

import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.User.User
import appfactory.uwp.edu.parksideapp2.login.AuthToken
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface LoginCalls {

//    @FormUrlEncoded
//    @POST("auth/login")
//    fun loginUserAsync(@Field("email") email: String, @Field("password") password: String): Deferred<Response<AuthToken>>
//
//    @FormUrlEncoded
//    @POST("auth/register")
//    fun registerUserAsync(@Field("email") email: String,
//                          @Field("password") password: String,
//                          @Field("name") name: String,
//                          @Field("admin") admin: Boolean = false): Deferred<Response<AuthToken>>
//
//    @GET("refresh")
//    fun refreshAuthTokenAsync(@Header("x-api-key") apiKey: String): Deferred<Response<AuthToken>>
//
//    @GET("auth/me")
//    fun getUserInfoAsync(@Header("x-access-token") token: String?): Deferred<Response<User>>
//
//    companion object {
//        fun getCalls(): LoginCalls {
//            val retrofit = RangerWellnessRetroFit.create()
//            return retrofit.create(LoginCalls::class.java)
//        }
//    }
}