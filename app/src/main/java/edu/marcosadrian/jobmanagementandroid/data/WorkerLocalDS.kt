package edu.marcosadrian.jobmanagementandroid.data

import edu.marcosadrian.jobmanagementandroid.model.Job

class WorkerLocalDS(private val db: WorkerDao) {
    suspend fun getFinishedJobs()=db.getFinishedJobs()
    suspend fun getFinishedJobsPrio(prio:Int)=db.getFinishedJobsPrio(prio)
    suspend fun insertJob(jobs:List<Job>){
        db.insertJob(jobs)
    }
}