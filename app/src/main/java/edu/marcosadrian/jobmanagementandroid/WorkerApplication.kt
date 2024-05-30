package edu.marcosadrian.jobmanagementandroid

import android.app.Application
import androidx.room.Room
import edu.marcosadrian.jobmanagementandroid.data.WorkerDB

class WorkerApplication:Application() {
    lateinit var workerDB: WorkerDB
        private set

    override fun onCreate() {
        super.onCreate()
        workerDB= Room.databaseBuilder(
            this,
            WorkerDB::class.java,"worker.db"
        ).build()
    }
}