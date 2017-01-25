package com.example.shichaonie.eduassist.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.example.shichaonie.eduassist.R;

/**
 * Created by Shichao Nie on 2017/1/1.
 */

public final class ConvertUtil{

    public static String toCategory(int num) {
        switch (num) {
            default:
                return "语文";
            case 1:
                return "数学";
            case 2:
                return "英语";
            case 3:
                return "物理";
            case 4:
                return "化学";
            case 5:
                return "生物";
            case 6:
                return "政治";
            case 7:
                return "历史";
            case 8:
                return "地理";
        }
    }
    public static String toAttribute(int num){
        switch (num){
            default:
                return "私密";
            case 1:
                return "公开";
        }
    }
    public static String toTitle(int num){
        switch (num){
            case 0:
                return "学生";
            default:
                return "教师";
        }
    }
    public static String toPrivateMode(int num){
        switch (num){
            case 0:
                return "关闭";
            default:
                return "打开";
        }
    }

}
