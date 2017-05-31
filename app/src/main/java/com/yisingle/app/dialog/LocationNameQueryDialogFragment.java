package com.yisingle.app.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseDialogFragment;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.HisDestinationData;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/23.
 */

public class LocationNameQueryDialogFragment extends BaseDialogFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private RecyclerAdapter<HisDestinationData> adapter;

    private List<HisDestinationData> dataList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_fragment_location_name_query;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        testData();
        initRecyclerView();


        //   adapter.refreshWithNewData(dataList);

    }

    private void testData() {

        dataList.add(HisDestinationData.createHomeHisDestinationData("家", "家里"));
        dataList.add(HisDestinationData.createCompanyHisDestinationData("公司", "东方希望天祥国际广场"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("天府广场", "天府广场春熙路"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("双流机场", "双流机场618"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("时间飞逝", "是否"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("是否", "爽肤水"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("是方式", "发是"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("爽肤水", "爽肤水"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("是否是", "方式"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("爽肤水", "方式方法"));
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
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
        recyclerView.addItemDecoration(new ItemDecoration(getContext(), 1));
        recyclerView.setAdapter(adapter);

    }


    public class ItemDecoration extends RecyclerView.ItemDecoration {

        private int dividerHeight;
        private Paint paint;
        private int graycolor;

        public ItemDecoration(Context context, int dividerHeight) {
            paint = new Paint();
            graycolor = ContextCompat.getColor(context, R.color.hint_grey_text);
            paint.setColor(graycolor);
            this.dividerHeight = dividerHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            if (pos == 0) {
                outRect.set(0, 0, dividerHeight, 0);
            } else if (pos == 1) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, dividerHeight, 0, 0);
            }
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);

            float left;
            float right;
            float top;
            float bottom;
            int childCount = parent.getChildCount();
            View childView;
            for (int i = 0; i < childCount; i++) {
                childView = parent.getChildAt(i);
                if (i == 0) {
                    paint.setColor(graycolor);
                    top = childView.getTop();
                    bottom = childView.getBottom();
                    left = childView.getRight();
                    right = left + dividerHeight;
                    c.drawRect(left, top, right, bottom, paint);//绘制横向间隔
                } else if (i == 1) {

                } else {
                    top = childView.getTop()-dividerHeight;
                    bottom = childView.getTop();
                    paint.setColor(graycolor);
                    left = childView.getLeft() - childView.getPaddingStart();
                    right = childView.getRight() - childView.getPaddingEnd();
                    c.drawRect(left, top, right, bottom, paint);
                }
            }
        }
    }

}
