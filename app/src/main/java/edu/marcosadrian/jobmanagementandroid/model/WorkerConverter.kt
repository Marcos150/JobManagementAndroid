package edu.marcosadrian.jobmanagementandroid.model

import androidx.room.TypeConverter

class WorkerConverter {
    @TypeConverter
    fun fromWorker(worker: Worker) : String {
        return worker.idTrabajador
    }

    @TypeConverter
    fun toWorker(codTrabajador: String) : Worker {
        return Worker(idTrabajador = codTrabajador)
    }
}