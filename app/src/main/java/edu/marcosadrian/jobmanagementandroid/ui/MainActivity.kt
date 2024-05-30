package edu.marcosadrian.jobmanagementandroid.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.marcosadrian.jobmanagementandroid.WorkerApplication
import edu.marcosadrian.jobmanagementandroid.data.WorkerRemoteDS
import edu.marcosadrian.jobmanagementandroid.data.WorkerRepository
import edu.marcosadrian.jobmanagementandroid.databinding.ActivityMainBinding
import edu.marcosadrian.jobmanagementandroid.JobsAdapter
import edu.marcosadrian.jobmanagementandroid.R
import edu.marcosadrian.jobmanagementandroid.checkConnection
import edu.marcosadrian.jobmanagementandroid.databinding.LoginDialogLayoutBinding
import edu.marcosadrian.jobmanagementandroid.jobDetailDialog
import edu.marcosadrian.jobmanagementandroid.list
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val vm: MainActivityViewModel by viewModels {
        val db = (application as WorkerApplication).workerDB
        val ds = WorkerRemoteDS()
        MainViewModelFactory(WorkerRepository(db, ds))
    }
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        JobsAdapter(onClick = { job ->
            //En isFinished se tendria que poner si el trabajo se ha acabado, para mostrar la opt de acabarlo
            jobDetailDialog(
                "Job detail",
                """Descripción: ${job.descripcion}
Categoría: ${job.categoria}
Prioridad: ${job.prioridad}
Fecha de inicio: ${job.fecIni}
Fecha de fin: ${job.fecFin ?: "Sin finalizar"}
${if (job.fecFin != null) "Tiempo empleado: " + job.tiempo else ""}""",
                job.fecFin != null,
                layoutInflater,
                this
            )
        })
    }

    private var ordered = false
    private var finished = false
    private var idUsuario: String? = null
    private var password: String? = null
    private var initialized = false

    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mToolbar.inflateMenu(R.menu.menu)
        binding.recyclerView.adapter = adapter
        loginDialog()
    }

    override fun onStart() {
        super.onStart()

        binding.mToolbar.setOnMenuItemClickListener { option ->
            when (option.itemId) {
                R.id.opSortPriority -> {
                    if (!ordered) adapter.submitList(adapter.currentList.sortedBy { it.prioridad })
                    else adapter.submitList(list.filter {
                        adapter.currentList.contains(it)
                    })
                    ordered = !ordered
                    true
                }

                R.id.opPriority1 -> {
                    /*adapter.submitList(list.filter {
                        it.prioridad == 1
                    })*/
                    createList(1)

                    true
                }

                R.id.opPriority2 -> {
                    createList(2)

                    true
                }

                R.id.opPriority3 -> {
                    createList(3)

                    true
                }

                R.id.opPriority4 -> {
                    createList(4)

                    true
                }

                R.id.opSortFinished -> {
                    if (!finished) createFinishedList()
                    else createList()
                    finished = !finished

                    true
                }

                R.id.opLogout -> {
                    val prvListSize = list.size
                    list.clear()
                    adapter.submitList(emptyList())
                    adapter.notifyItemRangeRemoved(0, prvListSize)
                    initialized = false
                    loginDialog()

                    true
                }

                else -> false
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            if (checkConnection(this)) {
                if (finished) createFinishedList()
                else createList()
            } else {
                Toast.makeText(this, getString(R.string.txt_noConnection), Toast.LENGTH_SHORT)
                    .show()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun loginDialog() {
        val bindingCustom = LoginDialogLayoutBinding.inflate(layoutInflater)

        dialog = MaterialAlertDialogBuilder(this).apply {
            setTitle("Introduce tus credenciales")

            setPositiveButton("Enviar") { _, _ -> }
            setOnDismissListener {
                if (!initialized) loginDialog()
            }
            setView(bindingCustom.root)
        }.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                if (bindingCustom.editTextUserId.text.isNullOrBlank())
                    bindingCustom.txtInputLayoutIdUser.error = getString(R.string.warning_username)
                if (bindingCustom.editTextPassword.text.isNullOrBlank())
                    bindingCustom.txtInputLayoutPassword.error =
                        getString(R.string.warning_username)
                else {
                    bindingCustom.txtInputLayoutIdUser.error = ""
                    bindingCustom.txtInputLayoutPassword.error = ""
                    idUsuario = bindingCustom.editTextUserId.text.toString()
                    password = bindingCustom.editTextPassword.text.toString()

                    createList()
                }
            }
        }

        dialog.show()
    }

    private fun createList(prio: Int = -1) {
        lifecycleScope.launch {
            val previousListSize = list.size
            if (prio in 1..4)
                vm.getFinishedJobsByWorkerPrio(idUsuario!!, password!!, prio).collect {
                    list.clear()
                    list.addAll(it)
                    adapter.submitList(list)
                    adapter.notifyItemRangeChanged(0, previousListSize)
                    binding.swipeRefresh.isRefreshing = false
                }
            else
                vm.getUnfinishedJobsByWorker(idUsuario!!, password!!).catch {
                    if (it.message?.contains("HTTP 401") == true) {
                        initialized = false
                        Toast.makeText(
                            this@MainActivity,
                            "Las credenciales no son correctas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else {
                        Toast.makeText(
                            this@MainActivity,
                            "Error al mostrar la lista",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.collect {
                    list.clear()
                    list.addAll(it)
                    adapter.submitList(list)
                    adapter.notifyItemRangeChanged(0, previousListSize)
                    binding.swipeRefresh.isRefreshing = false

                    if (!initialized) {
                        initialized = true
                        dialog.dismiss()
                    }
                }

        }
    }

    private fun createFinishedList() {
        lifecycleScope.launch {
            val previousListSize = list.size
            vm.getFinishedJobsByWorker(idUsuario!!, password!!).collect {
                list.clear()
                list.addAll(it)
                adapter.submitList(list)
                adapter.notifyItemRangeChanged(0, previousListSize)
                binding.swipeRefresh.isRefreshing = false
            }

        }
    }
}