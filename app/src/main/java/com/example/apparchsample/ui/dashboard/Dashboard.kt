package com.example.apparchsample.ui.dashboard

import android.Manifest
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apparchsample.R
import com.example.apparchsample.databinding.FragmentDashboardBinding
import com.example.apparchsample.domain.PlansModel
import com.example.apparchsample.ui.dashboard.adapter.PlansListAdapter
import java.io.File


class Dashboard : Fragment() {

    private val viewModel: DashboardViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            DashboardViewModel.Factory(activity.application)
        )[DashboardViewModel::class.java]
    }
    private var viewModelAdapter: PlansListAdapter? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentDashboardBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        sharedPreferences = context?.getSharedPreferences("Sunrise", Context.MODE_PRIVATE)!!
        verifyStoragePermissions()
        viewModelAdapter = PlansListAdapter {
            showFilePath(it)
        }
        binding.videoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        observePlansList()

        binding.btnDownload.setOnClickListener {
            viewModel.wasDownloaded = true
            getToken()?.let { viewModel.getProjects(it) }
            Log.d("#Download", "start")
            hideDownloadButton()
            showDownloadStatus()
        }

        requireActivity().registerReceiver(
            onComplete,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        return binding.root
    }

    private fun verifyStoragePermissions() {
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    private fun observePlansList() {
        with(viewModel) {
            plansList.observe(viewLifecycleOwner) { plans ->
                if (plans.isNotEmpty()) {
                    hideDownloadButton()
                    if (wasDownloaded) {
                        context?.let { it1 ->
                            observeDownload()
                            viewModel.downloadImageFile(it1, plans)
                        }
                    } else {
                        viewModel._uiBindDataPlanList.value = plans
                        observeUiBindDataPlansList()
                    }
                }
            }
        }
    }

    private fun observeDownload() {
        with(viewModel) {
            isDownloadFinished.observe(viewLifecycleOwner) {
                if (it) {
                    _uiBindDataPlanList.value = plansList.value
                    observeUiBindDataPlansList()
                    hideDownloadStatus()
                }
            }
        }
    }

    private fun observeUiBindDataPlansList() {
        with(viewModel) {
            uiBindDataPlansList.observe(viewLifecycleOwner) {
                if (viewModelAdapter != null) {
                    it?.let {
                        viewModelAdapter?.submitData(it)
                    }
                }
            }
        }
    }

    private fun hideDownloadButton() {
        binding.btnDownload.visibility = View.GONE
    }

    private fun hideDownloadStatus() {
        binding.llDownload.visibility = View.INVISIBLE
    }

    private fun showDownloadStatus() {
        binding.llDownload.visibility = View.VISIBLE
    }

    private fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            with(viewModel) {
                addDownloadCount()
                if (downloadCount.value!! >= totalFileSize.value!!) {
                    setDownloadFinished()
                }
            }
        }
    }

    private fun showFilePath(it: PlansModel) {
        val storageDir = "/storage/emulated/0/"
        val folderName = "/Sunrise/"
        val name = it.fileOriginalName?.substringAfterLast("/") ?: ""
        val file = File(storageDir + Environment.DIRECTORY_DOCUMENTS + folderName, name)
        Toast.makeText(context, file.absolutePath.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.dashboard_title)
    }

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

}