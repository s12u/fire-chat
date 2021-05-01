package com.tistory.mybstory.firechat.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.databinding.ActivityMainBinding
import com.tistory.mybstory.firechat.util.Event
import com.tistory.mybstory.firechat.util.EventBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var eventBus: EventBus
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initNavigation()
        observeEvent()
    }

    private fun observeEvent() {
        eventBus.events
            .onEach { handleEvent(it) }
            .launchIn(lifecycleScope)
    }

    private fun initNavigation() = with(binding) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val isTopLevel = destination.id in topLevelItems
            val isNoBottomNavigation = destination.id in noBottomNavItems

            val bottomNavVisibility = if (!isTopLevel || isNoBottomNavigation) View.GONE else View.VISIBLE

            bottomNavigationView.visibility = bottomNavVisibility
        }

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.ShowProgress -> {
                Timber.e("show progress")
                binding.layoutProgress.visibility = View.VISIBLE
            }
            is Event.HideProgress -> {
                Timber.e("hide progress")
                binding.layoutProgress.visibility = View.GONE
            }
        }
    }

    companion object {
        private val topLevelItems = setOf(
            R.id.homeFragment,
            R.id.chatListFragment,
            R.id.settingsFragment
        )

        private val noBottomNavItems = setOf(
            R.id.splashFragment,
            R.id.firstRunFragment,
            R.id.phoneAuthFragment,
            R.id.countrySelectFragment,
            R.id.verifyCodeFragment,
            R.id.newProfileFragment
        )
    }
}
