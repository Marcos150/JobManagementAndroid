package edu.marcosadrian.jobmanagementandroid.data

import edu.marcosadrian.jobmanagementandroid.model.Job
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

class WorkerAPI {
    companion object{
        private const val BASE_URL="http://217.182.60.210:9000/api/"
        fun getRetrofit2Api():WorkerAPIInterface{
            return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build().create(WorkerAPIInterface::class.java)
        }
    }
}
interface WorkerAPIInterface{
    //@Headers("password")
    @POST("trabajador/login")
    suspend fun getUnfinishedJobsByWorker(@Query("idTrabajador")id:String,@Header("password")password:String):List<Job>
    //@Headers("password")
    @POST("trabajador/trabajos-finalizados")
    suspend fun getJobsFinishedByWorker(@Query("idTrabajador")id:String,@Header("password")password:String): List<Job>
    //@Headers("password")
    @GET("trabajo/workerprio")
    suspend fun getJobsByWorkerPrio(@Query("id")id:String,@Query("prio")prio:Int,@Header("password")password:String):List<Job>
    @GET("trabajo/{id}")
    suspend fun getJobById(@Path("id")id:String):Job
    @PATCH("trabajo/finalizar/{id}")
    suspend fun endJobById(@Path("id")id:String)
    @PUT("trabajo/{id}")
    suspend fun updateJob(@Path("id")id:String,@Body job:Job)

}