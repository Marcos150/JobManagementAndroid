package edu.marcosadrian.jobmanagementandroid

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import edu.marcosadrian.jobmanagementandroid.data.Preferences
import edu.marcosadrian.jobmanagementandroid.data.WorkerDB

class WorkerApplication:Application() {
    lateinit var workerDB: WorkerDB
        private set

    companion object {
        lateinit var preferences: Preferences
            private set
    }

    override fun onCreate() {
        super.onCreate()
        workerDB= Room.databaseBuilder(
            this,
            WorkerDB::class.java,"worker.db"
        ).build()
        preferences = Preferences(applicationContext)
    }
}