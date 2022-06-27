package com.sujaan.a7minutesworkout

object Constants {

    fun defaultExcerciseList():ArrayList<ExcersiseModel>{
        val excersiseList = ArrayList<ExcersiseModel>()

        val lunges = ExcersiseModel(
            1, "Lunges",R.drawable.lunges,false,false
        )
        excersiseList.add(lunges)
        val pushUps = ExcersiseModel(
            2, "Push Ups",R.drawable.pushups,false,false
        )
        excersiseList.add(pushUps)
        val squats = ExcersiseModel(
            3, "Squats",R.drawable.squats,false,false
        )
        excersiseList.add(squats)
        val overHeadDumbell = ExcersiseModel(
            4, "Overhead Dumbbell",R.drawable.foverheaddumbbell,false,false
        )
        excersiseList.add(overHeadDumbell)
        val gluteBridge = ExcersiseModel(
            5, "Glute Bridge",R.drawable.glutebridge,false,false
        )
        excersiseList.add(gluteBridge)
        val Clamshells = ExcersiseModel(
            6, "Clamshells",R.drawable.clamshells,false,false
        )
        excersiseList.add(Clamshells)
        val floorhipflexors = ExcersiseModel(
            7, "Floor hip flexors",R.drawable.floorhipflexors,false,false
        )
        excersiseList.add(floorhipflexors)
        return excersiseList
    }
}