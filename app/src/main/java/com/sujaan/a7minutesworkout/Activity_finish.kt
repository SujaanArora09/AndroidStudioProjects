package com.sujaan.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sujaan.a7minutesworkout.databinding.ActivityFinishBinding

class Activity_finish : AppCompatActivity() {

    private var binding:ActivityFinishBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flFinish?.setOnClickListener(){
            finish()
        }
    }
}