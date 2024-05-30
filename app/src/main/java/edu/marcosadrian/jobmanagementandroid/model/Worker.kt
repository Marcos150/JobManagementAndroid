package edu.marcosadrian.jobmanagementandroid.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName


@Entity(primaryKeys = ["idTrabajador"])
data class Worker(
    @SerializedName("apellidos")
    val apellidos: String? = "",
    @SerializedName("contrasenya")
    val contrasenya: String? = "",
    @SerializedName("dni")
    val dni: String? = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("especialidad")
    val especialidad: String? = "",
    @SerializedName("idTrabajador")
    val idTrabajador: String,
    @SerializedName("nombre")
    val nombre: String? = "",
    //@SerializedName("trabajos")
    //var trabajos: List<Job>? = null
)