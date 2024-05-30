package edu.marcosadrian.jobmanagementandroid.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import edu.marcosadrian.jobmanagementandroid.WorkerApplication
import edu.marcosadrian.jobmanagementandroid.data.WorkerRemoteDS
import edu.marcosadrian.jobmanagementandroid.data.WorkerRepository
import edu.marcosadrian.jobmanagementandroid.databinding.ActivityMainBinding
import edu.marcosadrian.jobmanagementandroid.JobsAdapter
import edu.marcosadrian.jobmanagementandroid.R
import edu.marcosadrian.jobmanagementandroid.jobDetailDialog
import edu.marcosadrian.jobmanagementandroid.list
import edu.marcosadrian.jobmanagementandroid.model.Job
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
Fecha de fin: ${job.fecFin}""", false, layoutInflater, this
            )
        })
    }

    private var ordered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mToolbar.inflateMenu(R.menu.menu)

        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            vm.getUnfinishedJobsByWorker("2", "password1234").collect{
                list.addAll(it)
                adapter.submitList(list)
            }
        }
        //list.add(Job("Informática", "1", "Arreglar los peceses", "rewf", "ref", 3, 4.3))
        //list.add(Job("Jardinería", "2", "Cortar el cesped", "rewf", "ref", 2, 4.3))
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
                    adapter.submitList(list.filter {
                        it.prioridad == 1
                    })

                    true
                }

                R.id.opPriority2 -> {
                    adapter.submitList(list.filter {
                        it.prioridad == 2
                    })

                    true
                }

                R.id.opPriority3 -> {
                    adapter.submitList(list.filter {
                        it.prioridad == 3
                    })

                    true
                }

                R.id.opPriority4 -> {
                    adapter.submitList(list.filter {
                        it.prioridad == 4
                    })

                    true
                }

                R.id.opPriorityAll -> {
                    adapter.submitList(list)

                    true
                }

                else -> false
            }
        }
    }
}