package com.example.apparchsample.ui.dashboard

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apparchsample.R
import com.example.apparchsample.databinding.FragmentDashboardBinding
import com.example.apparchsample.services.ScreenTimeService
import com.example.apparchsample.ui.dashboard.adapter.RealEstateAdapter
import com.example.apparchsample.ui.dashboard.adapter.VideoAdapter

class Dashboard : Fragment() {

    private val viewModel: DashboardViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            DashboardViewModel.Factory(activity.application)
        )[DashboardViewModel::class.java]
    }

    private var viewModelAdapter: VideoAdapter? = null

    private var realEstateAdapter: RealEstateAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observePlayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModelAdapter = VideoAdapter {
            Toast.makeText(context, it.title, Toast.LENGTH_LONG).show()
        }
        realEstateAdapter = RealEstateAdapter {
            Toast.makeText(context, it.img_src, Toast.LENGTH_LONG).show()

        }
        binding.videoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
        binding.realEstateRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = realEstateAdapter
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
        startService()
        return binding.root
    }

    private fun observePlayList() {
        with(viewModel) {
            playlist.observe(viewLifecycleOwner) { videos ->
                if (viewModelAdapter != null) {
                    videos?.let {
                        viewModelAdapter?.submitData(it)
                    }
                }
            }
            realEstateList.observe(viewLifecycleOwner) {
                if (realEstateAdapter != null) {
                    it?.let {
                        realEstateAdapter?.submitData(it)
                    }
                }
            }
        }
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun startService() {
        if (isMyServiceRunning(ScreenTimeService::class.java)) return
        val startIntent = Intent(context, ScreenTimeService::class.java)
        startIntent.action = START_COMMAND
        activity?.startService(startIntent)
        Log.d("startService", "start")
    }

    private fun stopService() {
        if (!isMyServiceRunning(ScreenTimeService::class.java)) return
        val stopIntent = Intent(context, ScreenTimeService::class.java)
        stopIntent.action = STOP_COMMAND
        activity?.startService(stopIntent)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }

    companion object {
        const val START_COMMAND = "start"
        const val STOP_COMMAND = "stop"
    }
}