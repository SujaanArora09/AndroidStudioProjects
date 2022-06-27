package com.sujaan.a7minutesworkout

import android.app.Dialog
import android.app.people.ConversationStatus
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.service.autofill.ImageTransformation
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sujaan.a7minutesworkout.databinding.ActivityExerciseBinding
import com.sujaan.a7minutesworkout.databinding.DialogCustomBackConformationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding :ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration:Long =1

    private var excerciseTimer: CountDownTimer? = null

    private var excerciseProgress = 0
    private var exerciseTimerDuration:Long =1
    private var excersiseList : ArrayList<ExcersiseModel>? = null
    private var currentExcersisePosition = -1

    private var tts : TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExcercise)

        if (supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        excersiseList = Constants.defaultExcerciseList()

        tts = TextToSpeech(this,this)

        binding?.toolbarExcercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
        //binding?.flProgressBar?.visibility = View.INVISIBLE
        setUpRestView()
        setUpExerciseStatusRecyclerView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun customDialogForBackButton() {
        val customDialog =Dialog(this)
        val dialogBinding = DialogCustomBackConformationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setUpExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL , false)

        exerciseAdapter = ExerciseStatusAdapter(excersiseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setUpRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.sujaan.a7minutesworkout/" + R.raw.starttimer)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExcerciseName?.visibility = View.INVISIBLE
        binding?.flExcersiseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvUpcomingExerciseName?.text = excersiseList!![currentExcersisePosition+1].getName()

        setRestProgressBar()
    }

    private fun setupExcerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExcerciseName?.visibility = View.VISIBLE
        binding?.flExcersiseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        if (excerciseTimer != null){
            excerciseTimer?.cancel()
            excerciseProgress = 0
        }

        speakOut(excersiseList!![currentExcersisePosition].getName())

        binding?.ivImage?.setImageResource(excersiseList!![currentExcersisePosition].getImage())
        binding?.tvExcerciseName?.text = excersiseList!![currentExcersisePosition].getName()

        setExcerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress
        restTimer = object: CountDownTimer(restTimerDuration*5000,1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10-restProgress).toString()
            }

            override fun onFinish() {
                currentExcersisePosition++

                excersiseList!![currentExcersisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExcerciseView()

            }
        }.start()
    }

    private fun setExcerciseProgressBar(){
        binding?.progressBarExcersice?.progress = excerciseProgress
        excerciseTimer = object: CountDownTimer(exerciseTimerDuration*5000,1000){
            override fun onTick(p0: Long) {
                excerciseProgress++
                binding?.progressBarExcersice?.progress = 60 - excerciseProgress
                binding?.tvTimerExcercise?.text = (60-excerciseProgress).toString()
            }

            override fun onFinish() {



                if (currentExcersisePosition < excersiseList?.size!! -1){
                    excersiseList!![currentExcersisePosition].setIsSelected(false)
                    excersiseList!![currentExcersisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,Activity_finish::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if (excerciseTimer != null){
            excerciseTimer?.cancel()
            excerciseProgress = 0
        }

        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null){
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "This Language is not supported" )
            }else{
                Log.e("TTS","Inisialisation Failed")
            }

        }
    }
    private fun speakOut(text:String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}