package com.example.contactlist.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.contactlist.AppState
import com.example.contactlist.R
import com.example.contactlist.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    private var _binding: MainFragmentBinding? = null
    private val binding get() =_binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapter: ContactAdapter by lazy {
        ContactAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        binding.contactListList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contacts.observe(viewLifecycleOwner){
            renderData(it)
        }
        checkPermission()
    }

    private fun renderData(data: AppState?) {
        when (data) {
            is AppState.Success -> {
                binding.contactListList.show()
                binding.includedLoadingLayout.loadingLayout.hide()
                adapter.contacts = data.data
            }
            is AppState.Loading -> {
                binding.contactListList.hide()
                binding.includedLoadingLayout.loadingLayout.show()
            }
        }
    }


    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Объяснение")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                        .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    val REQUEST_CODE = 42

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getContacts()
            } else {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Объяснение")
                        .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }
        }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when(requestCode){
//            REQUEST_CODE ->{
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    getContacts()
//                } else {
//                    context?.let {
//                        AlertDialog.Builder(it)
//                            .setTitle("Доступ к контактам")
//                            .setMessage("Объяснение")
//                            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
//                            .create()
//                            .show()
//                    }
//                    return
//                }
//            }
//        }
//    }

    private fun getContacts() {
        viewModel.getContacts()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun View.show(): View{
        if (visibility != View.VISIBLE){
            visibility = View.VISIBLE
        }
        return this
    }

    private fun View.hide(): View{
        if (visibility != View.GONE){
            visibility = View.GONE
        }
        return this
    }

}