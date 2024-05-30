package edu.marcosadrian.jobmanagementandroid.data

class WorkerRemoteDS {
    private val api=WorkerAPI.getRetrofit2Api()

    suspend fun getUnfinishedJobs(id:String,password:String)=api.getUnfinishedJobsByWorker(id,password)
    suspend fun getFinishedJobs(id:String,password: String)=api.getJobsFinishedByWorker(id,password)

    suspend fun getFinishedJobsByPrio(id:String,prio:Int,password:String)=api.getJobsByWorkerPrio(id,prio,password)

    suspend fun getJobById(id:String)=api.getJobById(id)
}