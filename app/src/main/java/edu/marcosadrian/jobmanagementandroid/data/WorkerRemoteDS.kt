package edu.marcosadrian.jobmanagementandroid.data

class WorkerRemoteDS {
    private val api=WorkerAPI.getRetrofit2Api()

    fun getUnfinishedJobs(id:String,password:String)=api.getUnfinishedJobsByWorker(id,password)
    fun getFinishedJobs(id:String,password: String)=api.getJobsFinishedByWorker(id,password)

    fun getFinishedJobsByPrio(id:String,prio:String,password:String)=api.getJobsByWorkerPrio(id,prio,password)

    suspend fun getJobById(id:String)=api.getJobById(id)
}