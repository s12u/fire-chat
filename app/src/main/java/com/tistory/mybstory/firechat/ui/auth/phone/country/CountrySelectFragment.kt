package com.tistory.mybstory.firechat.ui.auth.phone.country

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentCountrySelectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class CountrySelectFragment :
    BaseFragment<FragmentCountrySelectBinding>(R.layout.fragment_country_select) {

    private val viewModel: CountrySelectViewModel by viewModels()
    private lateinit var countryListAdapter: CountryListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeFilteredResult()
    }

    private fun initUI() = with(binding) {
        vm = viewModel
        countryListAdapter = CountryListAdapter(itemClickCallback)
        rvCountryList.adapter = countryListAdapter
    }

    private fun observeFilteredResult() {
        Timber.e("called")
        viewModel.filteredCountriesFlow
            .onEach {
                Timber.e("size : ${it.size}")
                countryListAdapter.putData(it)
            }.launchIn(this@CountrySelectFragment)
    }

    private val itemClickCallback = fun(country: Country) {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(
                BUNDLE_KEY_COUNTRY to country.name,
            )
        )
        findNavController().navigateUp()
    }

    companion object {
        const val REQUEST_KEY = "COUNTRY_SELECT_REQUEST"
        const val BUNDLE_KEY_COUNTRY = "COUNTRY"
    }
}


