package com.example.countrieskotlin.service

import com.example.countrieskotlin.model.Country
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface CountryAPI {

    //GET,POST
    //https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json
    //BASE URL -> https://raw.githubusercontent.com/

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")
    // single tek sefer çeker , observable sürekli yenileyerek canlı veri çeker
    //fun  getCountries():Observable<List<Country>>
    fun  getCountries():Single<List<Country>>
}