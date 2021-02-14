package com.tistory.mybstory.firechat.ui.auth.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import kotlin.reflect.full.createInstance

@FlowPreview
@ExperimentalCoroutinesApi
class CountrySelectViewModel : ViewModel() {

    private val countryList = Country::class.nestedClasses.map { it.createInstance() as Country }
    private val queryChannel = ConflatedBroadcastChannel("")
    val filteredCountriesFlow = MutableStateFlow(countryList)

    init {
        initQueryChannelFlow()
    }

    private fun initQueryChannelFlow() {
        queryChannel.asFlow()
            .debounce(500L)
            .onEach { query ->
                Timber.e("query: $query")
                val result = countryList.filter { country ->
                    val cap = country.displayCountry.capitalize(Locale.getDefault())
                    cap.startsWith(query.capitalize(Locale.getDefault()))
                }
                filteredCountriesFlow.emit(result)
            }.launchIn(viewModelScope)
    }

    val queryByKeyword = fun(query: String) {
        queryChannel.offer(query)
    }

}
