package com.potaninpm.finaltour.di.modules

import com.potaninpm.feature_feed.presentation.viewModels.CommentsViewModel
import com.potaninpm.feature_feed.presentation.viewModels.PostsViewModel
import com.potaninpm.feature_finances.presentation.viewModels.FinancesViewModel
import com.potaninpm.feature_home.presentation.viewModels.HomeViewModel
import com.potaninpm.feature_home.presentation.viewModels.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
}

val financesViewModelModule = module {
    single { FinancesViewModel(get(), get()) }
}

val postsViewModelModule = module {
    viewModel { PostsViewModel(get()) }

    viewModel { (postId: Long) ->
        CommentsViewModel(get(), postId)
    }
}