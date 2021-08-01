package me.randheer.covidstatsin.android.states

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.randheer.covidstatsin.data.repo.getCovidRepository
import me.randheer.covidstatsin.db.CovidStateStats

class CovidStatesViewModel : ViewModel() {
    private val repository = getCovidRepository()

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _items: MutableLiveData<List<CovidStateStats>> = MutableLiveData()
    val items: LiveData<List<CovidStateStats>> = _items

    private var job: Job? = null

    fun loadData() {
        _loading.postValue(true)
        viewModelScope.launch {
            val stateStats = repository.getStates()
            _items.postValue(stateStats)
            _loading.postValue(false)
        }
    }

    fun searchData(searchText: String = "") {
        job?.cancel()
        job = viewModelScope.launch {
            delay(500L)
            val stateStats = repository.searchStates(searchText)
            _items.postValue(stateStats)
        }
    }
}