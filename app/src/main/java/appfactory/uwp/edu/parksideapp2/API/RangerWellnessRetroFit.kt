package appfactory.uwp.edu.parksideapp2.API

import appfactory.uwp.edu.parksideapp2.utils.RW_BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RangerWellnessRetroFit {
    @Volatile
    private var INSTANCE: Retrofit? = null

    fun create(): Retrofit {
        return INSTANCE ?: Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                // Mock Database emulator
//                    .baseUrl("http://10.0.2.2:3001/api/")
                // Mock Database, Marshall's Desktop local ip
//                    .baseUrl("http://192.168.11.118:3001/api/")
                // Actual Database
                .baseUrl(RW_BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().also {  INSTANCE = it }
    }
}

