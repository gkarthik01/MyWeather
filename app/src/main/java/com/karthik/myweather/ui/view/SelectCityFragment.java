package com.karthik.myweather.ui.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.karthik.myweather.R;
import com.karthik.myweather.data.entities.LocationEntity;
import com.karthik.myweather.databinding.SelectCityFragmentBinding;
import com.karthik.myweather.ui.viewModel.SelectCityViewModel;

import java.util.List;

public class SelectCityFragment extends BaseFragment {

    private SelectCityViewModel mViewModel;
    private SelectCityFragmentBinding binding;
    private ItemsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.select_city_fragment, container, false);
        binding.setLifecycleOwner(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvItems.setLayoutManager(layoutManager);
        binding.rvItems.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        adapter = new ItemsAdapter(locationEntity -> {
            mViewModel.getWeatherData(locationEntity.getLocationId());
        });
        binding.rvItems.setAdapter(adapter);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SelectCityViewModel.class);
        registerForLoadingIndicator(mViewModel);
        registerForErrorDialog(mViewModel);
        registerLocations();
        registerConsolidatedWeather();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.loadLocations();
    }

    private void registerConsolidatedWeather() {
        mViewModel.locationId.observe(this, locationId -> {
            Bundle bundle = new Bundle();
            bundle.putInt(CityWeatherFragment.Extra_Location_Id, locationId);
            navController.navigate(R.id.action_selectLocationFragment_to_weatherFragment, bundle);
        });
    }

    private void registerLocations() {
        mViewModel.locations.observe(this, locationEntities -> {
            adapter.setLocationEntities(locationEntities);
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public View view;
        public ItemViewHolder(View v) {
            super(v);
            view = v;
            textView = v.findViewById(R.id.tv_item);;
        }

        public void bind(LocationEntity locationEntity, OnItemClickListener onClickListener){
            textView.setText(locationEntity.getTitle());
            itemView.setOnClickListener(v -> onClickListener.onItemClicked(locationEntity));
        }
    }

    interface OnItemClickListener{
        void onItemClicked(LocationEntity locationEntity);
    }

    class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder>{

        private OnItemClickListener clickListener;
        ItemsAdapter(OnItemClickListener onItemClickListener){
            clickListener = onItemClickListener;
        }

        public void setLocationEntities(List<LocationEntity> locationEntities) {
            this.locationEntities = locationEntities;
            notifyDataSetChanged();
        }

        private List<LocationEntity> locationEntities;

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.template_location_item, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.bind(locationEntities.get(position), clickListener);
        }

        @Override
        public int getItemCount() {
            return locationEntities==null?0:locationEntities.size();
        }
    }

}