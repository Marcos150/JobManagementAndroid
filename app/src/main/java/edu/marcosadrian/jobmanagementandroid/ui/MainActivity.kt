package edu.marcosadrian.jobmanagementandroid

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import edu.marcosadrian.jobmanagementandroid.data.WorkerRemoteDS
import edu.marcosadrian.jobmanagementandroid.data.WorkerRepository
import edu.marcosadrian.jobmanagementandroid.databinding.ActivityMainBinding
import edu.marcosadrian.jobmanagementandroid.ui.MainActivityViewModel
import edu.marcosadrian.jobmanagementandroid.ui.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private val vm: MainActivityViewModel by viewModels {
        val db = (application as WorkerApplication).workerDB
        val ds = WorkerRemoteDS()
        MainViewModelFactory(WorkerRepository(db, ds))
    }
    private lateinit var binding:ActivityMainBinding
    private val adapter by lazy {
        JobsAdapter(onClick = {job ->
            //En isFinished se tendria que poner si el trabajo se ha acabado, para mostrar la opt de acabarlo
            jobDetailDialog("Job detail", job.toString(), false, layoutInflater, this)
        })
    }

    private var ordered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mToolbar.inflateMenu(R.menu.menu)

        binding.recyclerView.adapter = adapter

        list.add(1)
        list.add(4)
        list.add(3)
        list
        adapter.submitList(list)
    }

    override fun onStart() {
        super.onStart()

        binding.mToolbar.setOnMenuItemClickListener { option ->
            when (option.itemId) {
                R.id.opSortPriority -> {
                    if (!ordered) adapter.submitList(adapter.currentList.sorted())
                    else adapter.submitList(list.filter {
                        adapter.currentList.contains(it)
                    })
                    ordered = !ordered
                    true
                }

                R.id.opPriority1 -> {
                    adapter.submitList(list.filter {
                        it == 1
                    })

                    true
                }

                R.id.opPriority2 -> {
                    adapter.submitList(list.filter {
                        it == 2
                    })

                    true
                }

                R.id.opPriority3 -> {
                    adapter.submitList(list.filter {
                        it == 3
                    })

                    true
                }

                R.id.opPriority4 -> {
                    adapter.submitList(list.filter {
                        it == 4
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