package com.example.tast_4_permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tast_4_permissions.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissionsArray =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)

        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
                result.entries.forEach {
                    if (!it.value) {
                        Snackbar.make(
                            binding.layout,
                            "Both permissions are required",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    if (!it.value && !shouldShowRequestPermissionRationale(it.key)) {
                        sendToSettings()
                    }
                }
            }



        binding.requestButton.setOnClickListener {
            requestMultiplePermissions.launch(permissionsArray)
            checkPermission()


        }
    }


    private fun checkPermission() {

        val isCameraGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val isAudioGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED



        if (isCameraGranted) {
            binding.cameraPermissionTv.setText(R.string.camera_granted)
        } else {
            binding.cameraPermissionTv.setText(R.string.camera_denied)
        }

        if (isAudioGranted) {
            binding.audioPermissionTv.setText(R.string.audio_granted)
        } else {
            binding.audioPermissionTv.setText(R.string.audio_denied)
        }


    }


    private fun sendToSettings() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.apply {
            setTitle("Permissions are denied")
            setMessage("go to settings..")
            setPositiveButton("Settings") { dialog, id ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                startActivity(intent)
            }
        }
        alertDialogBuilder.show()
    }


}