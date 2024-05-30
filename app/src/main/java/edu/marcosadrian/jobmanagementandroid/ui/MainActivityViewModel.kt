package edu.marcosadrian.jobmanagementandroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.marcosadrian.jobmanagementandroid.data.WorkerRepository
import edu.marcosadrian.jobmanagementandroid.model.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivityViewModel(val repository: WorkerRepository) : ViewModel() {
    fun getFinishedJobsByWorker(id:String,pass:String): Flow<List<Job>> {
        return repository.fetchFinishedJobs(id,pass)
    }
    fun getFinishedJobsByWorkerPrio(id:String,pass:String,prio:Int):Flow<List<Job>>{
        return repository.fetchFinishedJobsPrio(id,pass,prio)
    }
    fun getUnfinishedJobsByWorker(id:String,pass:String)=repository.fetchUnfinishedJobs(id,pass)
    fun finishJob(id:String,job:Job,time:Double, mainActivity: MainActivity){
        viewModelScope.launch {
            repository.finishJob(id,job,time)
            mainActivity.initJobs()
        }
    }
}
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val repository: WorkerRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}