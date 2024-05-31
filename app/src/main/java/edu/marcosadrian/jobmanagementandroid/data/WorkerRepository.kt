package edu.marcosadrian.jobmanagementandroid.data

import android.util.Log
import edu.marcosadrian.jobmanagementandroid.model.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkerRepository(db: WorkerDB, val ds: WorkerRemoteDS) {
    private val localDataSource = WorkerLocalDS(db.workerDao())
    fun fetchFinishedJobs(id:String,pass:String): Flow<List<Job>> {
        return flow {
            var resultDB = emptyList<Job>()
            try {
                resultDB = localDataSource.getFinishedJobs()
                val resultAPI = ds.getFinishedJobs(id,pass)
                if (resultDB.isNotEmpty() && resultDB.containsAll(resultAPI)) {
                    emit(resultDB.filter { it.fecFin != null })
                } else {
                    localDataSource.insertJob(resultAPI)
                }
                resultDB = localDataSource.getFinishedJobs().filter { it.fecFin != null }
            } catch (e: Exception) {
                Log.e("DEBUG", "fetchFJobs: ${e.message}")
            } finally {
                emit(resultDB)
            }
        }
    }
    fun fetchFinishedJobsPrio(id:String,pass:String,prio:Int):Flow<List<Job>>{
        return flow {
            var resultDB = emptyList<Job>()
            try {
                resultDB = localDataSource.getFinishedJobsPrio(prio)
                val resultAPI = ds.getFinishedJobsByPrio(id,prio,pass)
                if (resultDB.isNotEmpty() && resultDB.containsAll(resultAPI)) {
                    emit(resultDB)
                } else {
                    localDataSource.insertJob(resultAPI)
                }
                resultDB = localDataSource.getFinishedJobsPrio(prio)
            } catch (e: Exception) {
                Log.e("DEBUG", "fetchFJobsPrio: ${e.message}")
            } finally {
                emit(resultDB)
            }
        }
    }
    fun fetchUnfinishedJobs(id:String,pass:String):Flow<List<Job>>{
        return flow{
            var result= emptyList<Job>()
            try{
                result=ds.getUnfinishedJobs(id,pass)
            }catch (e:Exception){
                Log.e("DEBUG", "fetchUJobs: ${e.message}")
                throw e //Para poder saber si aha fallado en el login
            }finally {
                emit(result)
            }
        }
    }
    suspend fun finishJob(id:String,job: Job,time:Double){
        ds.endJob(id,job,time)
    }

    suspend fun removeFinishedJobs() {
        localDataSource.removeFinishedJobs()
    }
}