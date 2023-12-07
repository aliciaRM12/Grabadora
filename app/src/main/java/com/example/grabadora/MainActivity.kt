package com.example.grabadora

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.grabadora.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var grabadora: MediaRecorder
    var ruta: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //permisos
        if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO),1000)
        }

        binding.botonGrabar.setOnClickListener {
            grabar()
        }
        binding.botonDetener.setOnClickListener {
            detener()
        }
        binding.botonReproducir.setOnClickListener {
            reproducir()
        }
    }

    fun grabar(){
        binding.botonGrabar.setImageResource(R.drawable.grabando)
        binding.botonGrabar.isEnabled = false
        binding.botonReproducir.isEnabled = false
        ruta= "/data/data/com.example.grabadora/grabacion.mp3"
        grabadora = MediaRecorder()
        grabadora?.setAudioSource(MediaRecorder.AudioSource.MIC)
        grabadora?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        grabadora?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        grabadora?.setOutputFile(ruta)
        try{
            grabadora?.prepare()
            grabadora?.start()
        }catch(e: IOException){
            Snackbar.make(binding.root, "Error al iniciar la grabacion", Snackbar.LENGTH_SHORT).show()
        }
        Snackbar.make(binding.root, "Inicio de la grabacion", Snackbar.LENGTH_SHORT).show()
    }

    fun detener(){
        binding.botonGrabar.setImageResource(R.drawable.micro)
            if(grabadora != null){
                grabadora.stop()
                grabadora.release()
                grabadora == null
            }
        Snackbar.make(binding.root, "Fin de la grabacion", Snackbar.LENGTH_SHORT).show()
        binding.botonGrabar.isEnabled = true
        binding.botonReproducir.isEnabled = true
    }

    fun reproducir(){
        binding.botonGrabar.isEnabled = false
        binding.botonDetener.isEnabled = false
        var mediaPlayer = MediaPlayer()
        try{
            mediaPlayer.setDataSource(ruta)
            mediaPlayer.prepare()
        }catch (e: IOException){
            Snackbar.make(binding.root, "No se pudo reproducir el audio", Snackbar.LENGTH_SHORT).show()
        }
        mediaPlayer.start()
        Snackbar.make(binding.root, "Reproduciendo audio", Snackbar.LENGTH_SHORT).show()
        binding.botonGrabar.isEnabled = true
        binding.botonDetener.isEnabled = true
    }
}