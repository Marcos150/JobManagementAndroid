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
import edu.marcosadrian.jobmanagementandroid.model.Job
import edu.marcosadrian.jobmanagementandroid.model.Worker
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
                this,
                finishJob = {
                    job.idTrabajador = Worker(idTrabajador = idUsuario!!)
                    vm.finishJob(job.codTrabajo, job, it, this)
                },
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

    private val listPriority = ArrayList<Job>()
    private val listFinished = ArrayList<Job>()
    private val listUnfinished = ArrayList<Job>()

    private var prioJob: kotlinx.coroutines.Job? = null
    private var finishedJob: kotlinx.coroutines.Job? = null
    private var unfinishedJob: kotlinx.coroutines.Job? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mToolbar.inflateMenu(R.menu.menu)
        binding.recyclerView.adapter = adapter

        println(WorkerApplication.preferences.idTrabajador)
        println(WorkerApplication.preferences.password)
        if (WorkerApplication.preferences.idTrabajador == "") loginDialog()
        else {
            idUsuario = WorkerApplication.preferences.idTrabajador
            password = WorkerApplication.preferences.password
            initialized = true
            unfinishedJob = lifecycleScope.launch {
                vm.getUnfinishedJobsByWorker(idUsuario!!, password!!).collect {
                    listUnfinished.addAll(it)
                    adapter.submitList(listUnfinished)
                }
            }

            finishedJob = lifecycleScope.launch {
                vm.getFinishedJobsByWorker(idUsuario!!, password!!).collect {
                    listFinished.addAll(it)
                }
            }

            prioJob = lifecycleScope.launch {
                vm.getFinishedJobsByWorkerPrio(idUsuario!!, password!!, 1).collect {
                    listPriority.addAll(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.mToolbar.setOnMenuItemClickListener { option ->
            when (option.itemId) {
                R.id.opSortPriority -> {
                    if (finished) {
                        if (!ordered) adapter.submitList(listFinished.sortedBy { it.prioridad })
                        else adapter.submitList(listFinished)
                        adapter.notifyItemRangeChanged(0, listFinished.size)
                    }

                    else {
                        if (!ordered) adapter.submitList(listUnfinished.sortedBy { it.prioridad })
                        else adapter.submitList(listUnfinished)
                        adapter.notifyItemRangeChanged(0, listUnfinished.size)
                    }

                    ordered = !ordered

                    true
                }

                R.id.opPriority1 -> {
                    prioJob!!.cancel()
                    prioJob = lifecycleScope.launch {
                        vm.getFinishedJobsByWorkerPrio(idUsuario!!, password!!, 1).collect {
                            listPriority.clear()
                            listPriority.addAll(it)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    adapter.submitList(listPriority)

                    true
                }

                R.id.opPriority2 -> {
                    prioJob!!.cancel()
                    prioJob = lifecycleScope.launch {
                        vm.getFinishedJobsByWorkerPrio(idUsuario!!, password!!, 2).collect {
                            listPriority.clear()
                            listPriority.addAll(it)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    adapter.submitList(listPriority)

                    true
                }

                R.id.opPriority3 -> {
                    prioJob!!.cancel()
                    prioJob = lifecycleScope.launch {
                        vm.getFinishedJobsByWorkerPrio(idUsuario!!, password!!, 3).collect {
                            listPriority.clear()
                            listPriority.addAll(it)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    adapter.submitList(listPriority)

                    true
                }

                R.id.opPriority4 -> {
                    prioJob!!.cancel()
                    prioJob = lifecycleScope.launch {
                        vm.getFinishedJobsByWorkerPrio(idUsuario!!, password!!, 4).collect {
                            listPriority.clear()
                            listPriority.addAll(it)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    adapter.submitList(listPriority)

                    true
                }

                R.id.opSortFinished -> {
                    if (!finished) adapter.submitList(listFinished) //createFinishedList()
                    else adapter.submitList(listUnfinished) //createList()
                    finished = !finished

                    true
                }

                R.id.opLogout -> {
                    val prvListSize = list.size
                    list.clear()
                    adapter.submitList(emptyList())
                    adapter.notifyItemRangeRemoved(0, prvListSize)
                    initialized = false
                    WorkerApplication.preferences.deletePrefs()
                    loginDialog()

                    true
                }

                else -> false
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            if (checkConnection(this)) {
                unfinishedJob!!.cancel()
                finishedJob!!.cancel()
                prioJob!!.cancel()
                initJobs()
                if (finished)  adapter.submitList(listFinished) //createFinishedList()
                else adapter.submitList(listUnfinished) //createList()
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

                    WorkerApplication.preferences.idTrabajador = idUsuario!!
                    WorkerApplication.preferences.password = password!!

                    initJobs()
                    adapter.submitList(listUnfinished)
                    dialog.dismiss()

                    //createList()
                }
            }
        }

        dialog.show()
    }

    fun initJobs() {
        initialized = true
        unfinishedJob = lifecycleScope.launch {
            vm.getUnfinishedJobsByWorker(idUsuario!!, password!!).catch {
                if (it.message?.contains("HTTP 401") == true) {
                    initialized = false
                    Toast.makeText(
                        this@MainActivity,
                        "Las credenciales no son correctas",
                        Toast.LENGTH_SHORT
                    ).show()
                    WorkerApplication.preferences.deletePrefs()
                    dialog.show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al mostrar la lista",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.collect {
                listUnfinished.clear()
                listUnfinished.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        finishedJob = lifecycleScope.launch {
            vm.getFinishedJobsByWorker(idUsuario!!, password!!).collect {
                listFinished.clear()
                listFinished.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        prioJob = lifecycleScope.launch {
            vm.getFinishedJobsByWorkerPrio(idUsuario!!, password!!, 2).collect {
                listPriority.clear()
                listPriority.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        binding.swipeRefresh.isRefreshing = false
    }
}