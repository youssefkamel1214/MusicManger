package com.example.musicmanger

fun Long.toMMSS(): String {
    val tmp=this/1000
    val minutes = tmp / 60
    val seconds = tmp % 60
    return "%02d:%02d".format(minutes, seconds)
}