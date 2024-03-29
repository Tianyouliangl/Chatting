package com.senyint.im.utils

import com.google.gson.Gson
import java.util.*

/**
 * Author:Ljb
 * Time:2018/12/28
 * There is a lot of misery in life
 **/
object JsonParser {

    private val mGson by lazy { Gson() }
    private val googleJsonParser by lazy { com.google.gson.JsonParser() }


    fun <T> fromJsonObj(json: String, clazz: Class<T>): T = mGson.fromJson(json, clazz)

    fun <T> fromJsonArr(json: String, clazz: Class<T>): MutableList<T> {
        val result = ArrayList<T>()
        val jsonArray = googleJsonParser.parse(json).asJsonArray
        jsonArray.mapTo(result) { mGson.fromJson(it, clazz) }
        return result
    }

    fun toJson(obj: Any): String {
        return mGson.toJson(obj)
    }

}