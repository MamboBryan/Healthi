package com.mambo.template.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mambo.template.R
import com.mambo.template.databinding.ActivityMainBinding
import com.mambo.template.databinding.FragmentNewMealBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment

        navController = navHostFragment.navController

//        when (viewModel.targetCalories) {
//            -1 -> {
//                navController.navigate(R.id.action_mainFragment_to_setupFragment)
//            }
//        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.connection.observe(this) { isNetworkAvailable ->
            binding.layoutConnection.constraintLayoutNetworkStatus.isVisible = !isNetworkAvailable
        }

    }

    override fun onBackPressed() {

        when (getDestinationId()) {

            R.id.mainFragment, R.id.setupFragment -> {

                if (viewModel.backIsPressedOnce) {
                    finish()
                } else {
                    viewModel.backIsPressedOnce = true

                    Toast.makeText(this, "Press \"BACK\" again to exit", Toast.LENGTH_SHORT)
                        .show()

                    Handler(Looper.getMainLooper())
                        .postDelayed(
                            { viewModel.backIsPressedOnce = false }, 2000
                        )
                }

            }

            else -> {
                super.onBackPressed()
            }

        }

    }


    private fun getDestinationId(): Int? {
        return navController.currentDestination?.id
    }
}