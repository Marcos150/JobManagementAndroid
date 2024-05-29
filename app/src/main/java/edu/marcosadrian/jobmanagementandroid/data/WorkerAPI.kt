package edu.marcosadrian.jobmanagementandroid.data

import edu.marcosadrian.jobmanagementandroid.model.Job
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

class WorkerAPI {
    companion object{
        private const val BASE_URL="http://217.182.60.210:9000/api"
        fun getRetrofit2Api():WorkerAPIInterface{
            return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build().create(WorkerAPIInterface::class.java)
        }
    }
}
interface WorkerAPIInterface{
    @Headers("password")
    @GET("trabajador/login")
    fun getUnfinishedJobsByWorker(@Query("idTrabajador")id:String,@Header("password")password:String):Flow<List<Job>>
    @Headers("password")
    @GET("trabajador/trabajos-finalizados")
    fun getJobsFinishedByWorker(@Query("idTrabajador")id:String,@Header("password")password:String): Flow<List<Job>>
    @Headers("password")
    @GET("trabajo/workerprio")
    fun getJobsByWorkerPrio(@Query("id")id:String,@Query("prio")prio:String,@Header("password")password:String):Flow<List<Job>>
    @GET("trabajo/{id}")
    suspend fun getJobById(@Path("id")id:String):Job
}