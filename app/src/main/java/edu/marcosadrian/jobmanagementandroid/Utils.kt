package edu.marcosadrian.jobmanagementandroid

import android.content.Context
import android.view.LayoutInflater
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