package edu.marcosadrian.jobmanagementandroid

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import edu.marcosadrian.jobmanagementandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val adapter by lazy {
        JobsAdapter()
    }

    private var ordered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mToolbar.inflateMenu(R.menu.menu)

        binding.recyclerView.adapter = adapter

        list.add(1)
        list.add(4)
        list.add(3)
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