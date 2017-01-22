/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.dk.mp.main.home.data;

import com.dk.mp.core.entity.App;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HpDataProvider extends AbstractDataProvider {
    private List<ConcreteData> mData;
    private ConcreteData mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public HpDataProvider(List<App> apps) {
        mData = new LinkedList<>();

        final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
        mData.add(new ConcreteData(mData.size(), 1, "saf", swipeReaction, "asdf","asdf",""));

        for (int j = 0; j < apps.size(); j++) {
            final long id = mData.size();
//            final int viewType = j==0?1:0;
            final int viewType = 0;
            final String text = apps.get(j).getName();
            mData.add(new ConcreteData(id, viewType, text, swipeReaction, apps.get(j).getIcon(),apps.get(j).getAction(),apps.get(j).getId()));
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

            mLastRemovedData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final ConcreteData item = mData.remove(fromPosition);

        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    @Override
    public void swapItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        Collections.swap(mData, toPosition, fromPosition);
        mLastRemovedPosition = -1;
    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ConcreteData removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }

    @Override
    public void addItem(App app) {
        mData.add(new ConcreteData(mData.size()+1, 0, app.getName(), 0, app.getIcon(), app.getAction(),app.getId()));
    }

    public void changeDatas(List<App> apps){
        mData.clear();
        final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_SWIPE_UP | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_DOWN;
        mData.add(new ConcreteData(mData.size(), 1, "saf", swipeReaction, "asdf","asdf",""));
        for (int j = 0; j < apps.size(); j++) {
            final long id = mData.size();
            final int viewType = 0;
            final String text = apps.get(j).getName();
            mData.add(new ConcreteData(id, viewType, text, swipeReaction, apps.get(j).getIcon(),apps.get(j).getAction(),apps.get(j).getId()));
        }
    }


    public static final class ConcreteData extends Data {

        private final long mId;
        private final String mText;
        private final int mViewType;
        private boolean mPinned;
        private String mImagesoruce;
        private String mAction;

        private String mAppid;

        ConcreteData(long id, int viewType, String text, int swipeReaction, String Imagesoruce, String action, String appid) {
            mId = id;
            mViewType = viewType;
            mText = text;
            mImagesoruce = Imagesoruce;
            mAction = action;
            mAppid = appid;
        }

        @Override
        public String getAction(){return mAction;}

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public int getViewType() {
            return mViewType;
        }

        @Override
        public long getId() {
            return mId;
        }

        @Override
        public String toString() {
            return mText;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public boolean isPinned() {
            return mPinned;
        }

        @Override
        public String getIcon() {
            return mImagesoruce;
        }

        @Override
        public void setPinned(boolean pinned) {
            mPinned = pinned;
        }

        @Override
        public String getAppid() {
         return mAppid;
        }

    }

    public List<App> getmData() {
        List<App> apps = new ArrayList<>();
        for (ConcreteData data : mData){
            App app = new App("", "", "", "", data.getAppid(), "", data.getIcon(), data.getAction());
            apps.add(app);
        }
        return apps;
    }
}
