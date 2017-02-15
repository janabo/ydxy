package com.dk.mp.core.ui;

import java.util.Map;

/**
 * Created by dongqs on 2017/2/14.
 */

public interface SubmitInterface {

    public boolean beforeLoad();

    public String setUrl();

    public Map<String , Object> setMap();
}
