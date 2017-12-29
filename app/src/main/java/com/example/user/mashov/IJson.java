package com.example.user.mashov;

import java.util.HashMap;
import java.util.List;

/**
 * Created by User on 12/23/17.
 */

public interface IJson <T> {
    public String makeJson(HashMap<String, T> params);
}
