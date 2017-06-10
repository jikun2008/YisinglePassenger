package com.yisingle.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.yisingle.app.R;
import com.yisingle.app.base.BaseFrament;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.HisDestinationData;
import com.yisingle.app.decoration.HisDestinationItemDecoration;
import com.yisingle.app.dialog.LocationNameQueryDialogFragment;
import com.yisingle.app.map.MapRxManager;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by jikun on 17/6/1.
 */

public class HisDestinationFragment extends BaseFrament {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private LocationNameQueryDialogFragment.OnChoosePlaceListener<HisDestinationData> choosePlaceListener;

    private RecyclerAdapter<HisDestinationData> adapter;

    private List<HisDestinationData> dataList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hisdestination;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        testData();
        initRecyclerView();
    }

    public static HisDestinationFragment newInstance() {

        Bundle args = new Bundle();

        HisDestinationFragment fragment = new HisDestinationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void testData() {

        dataList.add(HisDestinationData.createHomeHisDestinationData("家", "家里", new LatLng(30.627856, 103.997414)));
        dataList.add(HisDestinationData.createCompanyHisDestinationData("公司", "东方希望天祥国际广场", new LatLng(30.552963, 104.0679)));
        dataList.add(HisDestinationData.createNormalHisDestinationData("天府广场", "天府广场春熙路", new LatLng(30.657349, 104.065837)));
        dataList.add(HisDestinationData.createNormalHisDestinationData("双流机场", "双流机场", new LatLng(30.559105, 103.951572)));
    }


    private void initRecyclerView() {
        adapter = new RecyclerAdapter<HisDestinationData>(dataList, R.layout.adapter_history_destination) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, HisDestinationData item) {
                holder.setText(R.id.tv_destination_name, item.getName());
                holder.setText(R.id.tv_destination_allname, item.getAllName());
                holder.setImageResource(R.id.iv_icon_head, item.getIcon());

            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size;
                if (position == 0 || position == 1) {
                    size = 1;
                } else {
                    size = 2;
                }
                return size;
            }
        };
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new HisDestinationItemDecoration(getContext(), 1));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((position, item) -> {
            if (null != choosePlaceListener) {
                choosePlaceListener.onchoose(dataList.get(position));
            }
        });

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    public void setChoosePlaceListener(LocationNameQueryDialogFragment.OnChoosePlaceListener<HisDestinationData> choosePlaceListener) {
        this.choosePlaceListener = choosePlaceListener;
    }

    public void reshdata(String city, String key) {

        MapRxManager.getGeocodeAddressList(getContext(), key, city)
                .subscribe(new Subscriber<List<Tip>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("测试代码", "测试代码hisDestinationFragment--onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("测试代码", "测试代码hisDestinationFragment--onError");
                    }

                    @Override
                    public void onNext(List<Tip> tipList) {
                        Log.e("测试代码", "测试代码hisDestinationFragment--onNext");

                        dataList.clear();
                        dataList.add(HisDestinationData.createHomeHisDestinationData("家", "家里", new LatLng(30.627856, 103.997414)));
                        dataList.add(HisDestinationData.createCompanyHisDestinationData("公司", "东方希望天祥国际广场", new LatLng(30.552963, 104.0679)));
                        for (Tip tip : tipList) {
                            LatLonPoint latLonPoint = tip.getPoint();
                            LatLng latlng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
                            dataList.add(HisDestinationData.createNormalHisDestinationData(tip.getName(), tip.getAddress(), latlng));
                        }

                        adapter.notifyDataSetChanged();

                    }
                });


    }


}
