package edu.marcosadrian.jobmanagementandroid.model


import com.google.gson.annotations.SerializedName

data class Job(
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("codTrabajo")
    val codTrabajo: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("fecFin")
    val fecFin: String,
    @SerializedName("fecIni")
    val fecIni: String,
    @SerializedName("prioridad")
    val prioridad: Int,
    @SerializedName("tiempo")
    val tiempo: Double
)