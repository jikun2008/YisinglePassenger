package com.yisingle.app.widget.IndexBar.helper;

import com.yisingle.app.widget.IndexBar.bean.BaseIndexPinyinBean;

import java.util.List;

/**
 * Created by jikun on 17/6/2.
 */

public class GaoDeCityIIndexBarDataHelper implements IIndexBarDataHelper {
    @Override
    public IIndexBarDataHelper convert(List<? extends BaseIndexPinyinBean> datas) {
        return this;
    }

    @Override
    public IIndexBarDataHelper fillInexTag(List<? extends BaseIndexPinyinBean> datas) {
        if (null == datas || datas.isEmpty()) {
            return this;
        }
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            BaseIndexPinyinBean indexPinyinBean = datas.get(i);
            if (indexPinyinBean.isNeedToPinyin()) {
                //以下代码设置城市拼音首字母
                String tagString = indexPinyinBean.getBaseIndexPinyin().toString().substring(0, 1).toUpperCase();
                if (tagString.matches("[A-Z]")) {//如果是A-Z字母开头
                    indexPinyinBean.setBaseIndexTag(tagString);
                } else if (tagString.equals("热")) {
                    indexPinyinBean.setBaseIndexTag(tagString);
                } else {//特殊字母这里统一用#处理
                    indexPinyinBean.setBaseIndexTag("#");
                }
            }
        }
        return this;
    }

    @Override
    public IIndexBarDataHelper sortSourceDatas(List<? extends BaseIndexPinyinBean> datas) {
        return this;
    }

    @Override
    public IIndexBarDataHelper getSortedIndexDatas(List<? extends BaseIndexPinyinBean> sourceDatas, List<String> indexDatas) {
        if (null == sourceDatas || sourceDatas.isEmpty()) {
            return this;
        }
        //按数据源来 此时sourceDatas 已经有序
        int size = sourceDatas.size();
        String baseIndexTag;
        for (int i = 0; i < size; i++) {
            baseIndexTag = sourceDatas.get(i).getBaseIndexTag();
            if (!indexDatas.contains(baseIndexTag)) {//则判断是否已经将这个索引添加进去，若没有则添加
                indexDatas.add(baseIndexTag);
            }
        }

        return this;
    }
}
