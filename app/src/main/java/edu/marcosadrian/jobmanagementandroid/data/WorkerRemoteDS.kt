package edu.marcosadrian.jobmanagementandroid.data

import edu.marcosadrian.jobmanagementandroid.model.Job

class WorkerRemoteDS {
    private val api=WorkerAPI.getRetrofit2Api()

    suspend fun getUnfinishedJobs(id:String,password:String)=api.getUnfinishedJobsByWorker(id,password)
    suspend fun getFinishedJobs(id:String,password: String)=api.getJobsFinishedByWorker(id,password)

    suspend fun getFinishedJobsByPrio(id:String,prio:Int,password:String)=api.getJobsByWorkerPrio(id,prio,password)

    suspend fun getJobById(id:String)=api.getJobById(id)

    suspend fun endJob(id:String,job:Job,time:Double){
        api.updateJob(id,job.copy(tiempo = time))
        api.endJobById(id)
    }
}