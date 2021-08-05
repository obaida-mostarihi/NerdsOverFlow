/*
 *
 * Created by Obaida Al Mostarihi on 8/1/21, 9:34 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/1/21, 9:34 AM
 *
 */

package com.yoron.nerdsoverflow.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.models.ProgrammingLanguageModel
import com.yoron.nerdsoverflow.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(application: Application, val repo: MainRepository) :
    AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _languages = MutableLiveData<ArrayList<ProgrammingLanguageModel>>()
    val languages: LiveData<ArrayList<ProgrammingLanguageModel>> = _languages


    private val _username = MutableLiveData<String>()
    private val username: LiveData<String> = _username

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _dataOrException = MutableLiveData<DataOrException<Boolean, Exception>>()
    val dataOrException: LiveData<DataOrException<Boolean, Exception>> = _dataOrException


    private val _selectedLanguages = MutableLiveData<ArrayList<ProgrammingLanguageModel>>()
    private val selectedLanguages: LiveData<ArrayList<ProgrammingLanguageModel>> =
        _selectedLanguages


    init {

        viewModelScope.launch {
            getLanguages()

        }


    }


    fun registerDetails() {
        viewModelScope.launch {
            if (username.value != null && selectedLanguages.value != null) {
                _loading.postValue(true)
                _dataOrException.value =
                    repo.setDetails(username.value!!, selectedLanguages.value!!)
                _loading.postValue(false)

            } else {
                if (username.value == null)
                    _dataOrException.value =
                        DataOrException(false, Exception("Please don't leave the username empty."))
                else
                    _dataOrException.value =
                        DataOrException(
                            false,
                            Exception("Please select one or more programming languages.")
                        )

            }
        }
    }


    fun setUsername(username: String) {
        _username.postValue(username)
    }

    fun setSelectedLanguages(selectedLanguages: ArrayList<ProgrammingLanguageModel>) =
        _selectedLanguages.postValue(selectedLanguages)


    /**
     * Get all the programming languages and store them into an array list
     */
    private fun getLanguages() {
        try {
            loadJSONFromAsset()?.let {
                val languagesJson: String = it
                val jsonArray = JSONArray(languagesJson)
                val languagesList: ArrayList<ProgrammingLanguageModel> = ArrayList()
                for (i in 0 until jsonArray.length()) {
                    val language = jsonArray[i] as String
                    languagesList.add(ProgrammingLanguageModel(language = language))
                }
                _languages.postValue(languagesList)
            }

        } catch (e: IOException) {
            Log.v("loool" , e.localizedMessage)
            e.printStackTrace()
        } catch (e: JSONException) {
            Log.v("loool" , e.localizedMessage)

            e.printStackTrace()
        }
    }


    /**
     * Load the json file from the assets folder
     */
    private fun loadJSONFromAsset(): String? {

        var json: String? = null
        json = try {
            val `is`: InputStream = context.assets.open("languages.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

}