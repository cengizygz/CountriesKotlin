package com.example.countrieskotlin.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.countrieskotlin.model.Country
import com.example.countrieskotlin.service.CountryAPIService
import com.example.countrieskotlin.service.CountryDatabase
import com.example.countrieskotlin.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FeedViewModel(application: Application) : BaseViewModel(application) {

    private val countryAPIService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())
    // 10 dakika nanosaniye cinsinden yazımı
    private var refreshTime = 10*60*1000*1000*1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()

    fun refreshData(){


        val updateTime = customPreferences.getTime()
        if(updateTime !=null && updateTime !=0L && System.nanoTime() - updateTime<refreshTime){
            getDataFromSQLite()
        }else {
            getDataFromAPI()
        }
    }
    fun refreshFromApi(){
        getDataFromAPI()
    }

    private fun getDataFromSQLite() {
        countryLoading.value=true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
        }
    }


    private fun getDataFromAPI() {
        countryLoading.value =true
        disposable.add(
            countryAPIService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){
                    override fun onSuccess(t: List<Country>) {

                        storeInSQLite(t)
                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }

                })
        )
    }
    private fun showCountries(countryList: List<Country>){
        countries.value= countryList
        countryError.value = false
        countryLoading.value = false
    }
    private fun storeInSQLite (list: List<Country>){
        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            // toTypedArray = listeyi tekil hale getirir --> individual
            val listLong = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i<list.size){
                list[i].uuid = listLong[i].toInt()
                i=i+1
            }
            showCountries(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}