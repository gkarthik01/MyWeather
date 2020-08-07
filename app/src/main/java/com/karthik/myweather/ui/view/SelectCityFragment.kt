package com.karthik.myweather.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karthik.myweather.R
import com.karthik.myweather.data.entities.LocationEntity
import com.karthik.myweather.databinding.SelectCityFragmentBinding
import com.karthik.myweather.ui.viewModel.SelectCityViewModel
import kotlinx.android.synthetic.main.template_location_item.view.*

class SelectCityFragment : BaseFragment() {
    private lateinit var mViewModel: SelectCityViewModel
    private lateinit var binding: SelectCityFragmentBinding
    private lateinit var adapter: ItemsAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.select_city_fragment, container, false)
        binding.lifecycleOwner = this
        val layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false)
        binding.rvItems.layoutManager = layoutManager
        binding.rvItems.addItemDecoration(DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL))
        adapter = ItemsAdapter(object : OnItemClickListener {
            override fun onItemClicked(locationEntity: LocationEntity) {
                mViewModel.getWeatherData(locationEntity.locationId)
            }
        })
        binding.rvItems.adapter = adapter
        binding.viewModel = mViewModel
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SelectCityViewModel::class.java)
        registerForLoadingIndicator(mViewModel)
        registerForErrorDialog(mViewModel)
        registerLocations()
        registerConsolidatedWeather()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.loadLocations()
    }

    private fun registerConsolidatedWeather() {
        mViewModel.locationId.observe(this, Observer { locationId: Int? ->
            val bundle = Bundle()
            bundle.putInt(CityWeatherFragment.Extra_Location_Id, locationId!!)
            navController!!.navigate(R.id.action_selectLocationFragment_to_weatherFragment, bundle)
        })
    }

    private fun registerLocations() {
        mViewModel.locations.observe(this, Observer { locationEntities: List<LocationEntity>? -> adapter.setLocationEntities(locationEntities) })
    }

    internal inner class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bind(locationEntity: LocationEntity, onClickListener: OnItemClickListener) {
            itemView.tv_item.text = locationEntity.title
            itemView.setOnClickListener { onClickListener.onItemClicked(locationEntity) }
        }

    }

    internal interface OnItemClickListener {
        fun onItemClicked(locationEntity: LocationEntity)
    }

    internal inner class ItemsAdapter(private val clickListener: OnItemClickListener) : RecyclerView.Adapter<ItemViewHolder>() {
        fun setLocationEntities(locationEntities: List<LocationEntity>?) {
            this.locationEntities = locationEntities
            notifyDataSetChanged()
        }

        private var locationEntities: List<LocationEntity>? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.template_location_item, parent, false)
            return ItemViewHolder(v)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(locationEntities!![position], clickListener)
        }

        override fun getItemCount(): Int {
            return locationEntities?.size ?: 0
        }

    }
}