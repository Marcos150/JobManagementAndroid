package edu.marcosadrian.jobmanagementandroid.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.marcosadrian.jobmanagementandroid.model.Job
import edu.marcosadrian.jobmanagementandroid.model.Worker
import edu.marcosadrian.jobmanagementandroid.model.WorkerConverter


@TypeConverters(WorkerConverter::class)
@Database(entities = [Job::class, Worker::class], version = 1)
abstract class WorkerDB:RoomDatabase() {
    abstract fun workerDao():WorkerDao
}
@Dao
interface WorkerDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: List<Job>)
    @Query("SELECT * FROM Job")
    suspend fun getFinishedJobs(): List<Job>
    @Query("SELECT * FROM Job WHERE prioridad=:prio ORDER BY prioridad DESC")
    suspend fun getFinishedJobsPrio(prio:Int):List<Job>
    @Delete
    suspend fun removeJob(job:Job)
}