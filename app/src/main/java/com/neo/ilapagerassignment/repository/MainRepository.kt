package com.neo.ilapagerassignment.repository

import com.neo.ilapagerassignment.data.LocalData
import com.neo.ilapagerassignment.model.Gadgets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository
@Inject
constructor() {
    fun getMainData(): Flow<List<Gadgets>> = flow {
        emit(LocalData.getAllData())
    }.flowOn(Dispatchers.IO)
}