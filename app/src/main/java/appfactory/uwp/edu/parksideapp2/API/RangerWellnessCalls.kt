package appfactory.uwp.edu.parksideapp2.API

import appfactory.uwp.edu.parksideapp2.Models.Data
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.User.User
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.event.Event
import appfactory.uwp.edu.parksideapp2.ranger_wellness.data.workout.WorkoutType
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RangerWellnessCalls {

//    // TODO remove this, it does nothing
//    @GET("workout")
//    fun getUserWorkoutsAsync(@Header("x-access-token") token: String?): Deferred<Response<Array<Workout>>>

    @GET("workouts")
    fun getWorkoutTypesAsync(@Header("x-access-token") token: String): Deferred<Response<Data<Array<WorkoutType>>>>

    @GET("events")
    fun getEventsAsync(@Header("x-access-token") token: String): Deferred<Response<Data<Array<Event>>>>

    @GET("events/id")
    fun getEventByIdAsync(@Header("x-access-token") token: String,
                          @Header("id")id: String): Deferred<Response<Event>>

    @GET("auth/me")
    fun getUserInfoAsync(@Header("x-access-token") token: String?): Deferred<Response<User>>

    @POST("events/addeflag")
    fun userSubscribeToEventAsync(@Header("x-access-token") token: String,
                                  @Header("flag") subEventId: String): Deferred<Response<User>>

    // not actually daily, but overall.  This is a bad name.
    @GET("steps/dailygoal")
    fun getStepGoalAsync(): Deferred<Response<String>>

    @GET("steps/getallstep")
    fun getAllStepAsync(@Header("x-access-token") token: String?): Deferred<Response<List<String>>>

    @POST("workouts/addwflag")
    fun setSubWorkoutAsync(@Header("x-access-token") token: String,
                           @Header("id") userId: String,
                           @Header("flag") subWorkoutId: String): Deferred<Response<WorkoutType>>


    @POST("steps/adddailysteps")
    fun setDailyStepsAsync(@Header("x-access-token") token: String,
                           @Header("step") steps: String): Deferred<Response<String>>

    @POST("workouts/removewflag")
    fun removeSubWorkout(@Header("x-access-token") token: String,
                          @Header("id") userId: String,
                          @Header("flag") workout: String): Deferred<Response<String>>


    companion object {
        fun getCalls(): RangerWellnessCalls {
            val retrofit = RangerWellnessRetroFit.create()
            return retrofit.create(RangerWellnessCalls::class.java)
        }
    }
}