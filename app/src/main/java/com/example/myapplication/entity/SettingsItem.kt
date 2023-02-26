package com.example.myapplication.entity

/*
type
    0  normal
    1  select
    2  checkbox
*/
data class SettingsItem(val id: String, val title: String, var subtitle: String, val icon: Int)
