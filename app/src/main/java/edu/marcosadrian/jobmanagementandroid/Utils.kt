package edu.marcosadrian.jobmanagementandroid

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.LayoutInflater
import androidx.core.content.getSystemService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.marcosadrian.jobmanagementandroid.databinding.DialogLayoutBinding
import edu.marcosadrian.jobmanagementandroid.model.Job

//Lista temporal para hacer pruebas hasta que esten los modelos
val list = ArrayList<Job>()

fun jobDetailDialog(title: CharSequence, message: CharSequence, isFinished: Boolean, layoutInflater: LayoutInflater, context: Context) {
    val bindingCustom = DialogLayoutBinding.inflate(layoutInflater)
    // Se crea el AlertDialog.
    MaterialAlertDialogBuilder(context).apply {
        // Se asigna un título.
        setTitle(title)

        // Se asgina el cuerpo del mensaje.
        setMessage(message)

        if (!isFinished){
            setPositiveButton("Finalizar trabajo") { _, _ -> } //TODO: Insert finishing code
            setView(bindingCustom.root) //Para mostrar layout personalizado con input numerico
        }
        setNegativeButton("Cancelar") { _, _ -> }
    }.show() // Se muestra el AlertDialog.
}

/**
 * Comprueba si hay conexión a internet
 *
 * @param context Contexto
 * @return true si hay conexión, false en caso contrario
 */
fun checkConnection(context: Context): Boolean {
    //val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val cm = context.getSystemService<ConnectivityManager>()
    val networkInfo = cm!!.activeNetwork

    if (networkInfo != null) {
        val activeNetwork = cm.getNetworkCapabilities(networkInfo)
        if (activeNetwork != null) {
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    }
    return false
}