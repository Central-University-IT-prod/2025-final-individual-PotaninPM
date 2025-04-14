package com.potaninpm.finaltour.di

import com.potaninpm.finaltour.di.modules.chatModule
import com.potaninpm.finaltour.di.modules.financesRepositoryModule
import com.potaninpm.finaltour.di.modules.financeRoomModule
import com.potaninpm.finaltour.di.modules.financesViewModelModule
import com.potaninpm.finaltour.di.modules.homeNetworkModule
import com.potaninpm.finaltour.di.modules.homeRepositoryModule
import com.potaninpm.finaltour.di.modules.homeViewModelModule
import com.potaninpm.finaltour.di.modules.newsRoomModule
import com.potaninpm.finaltour.di.modules.postsNetworkModule
import com.potaninpm.finaltour.di.modules.postsRepositoryModule
import com.potaninpm.finaltour.di.modules.postsRoomModule
import com.potaninpm.finaltour.di.modules.postsViewModelModule

val coreModules = listOf(
    homeNetworkModule,
    postsNetworkModule,

    financeRoomModule,
    newsRoomModule,
    postsRoomModule,

    chatModule,

    homeRepositoryModule,
    postsRepositoryModule,
    financesRepositoryModule,

    homeViewModelModule,
    financesViewModelModule,
    postsViewModelModule
)