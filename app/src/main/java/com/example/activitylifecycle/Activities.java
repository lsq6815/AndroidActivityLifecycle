package com.example.activitylifecycle;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 提供在 3 个 Activities 间通用的方法，和变量
 */
public class Activities {

    /// 用于标记 Activities 发出的 log 信息
    public final static String TAG = "Activity_TAG";
    /// 用于存储 Activities 当前状态的 SharedPreferences 的名字
    public final static String PREFERENCES_NAME = "activity_statuses";
    /// 用于标记调用者
    public final static String EXTRA_CALLER = "caller";
    /// 用于标记 Message.what 的值的含义
    public final static int UPDATE_VIEWS = 1;
    /// 线程等待的时间（以毫秒为单位）
    public final static int WAIT_TIME = 1000;

    /**
     * 将 @p viewGroup 下的任意子 Button 的背景颜色设置成给定的 @p color
     * @param viewGroup 包含 Button 的 ViewGroup
     * @param color 表示 RGB 值的整数，不要跟 R.color.* 混淆
     */
    public static void setButtonsBackground(ViewGroup viewGroup, int color) {
        // 遍历子 View
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            // 如果子 View 是 Button 的实例
            if (viewGroup.getChildAt(i) instanceof Button) {
                // 设置其背景颜色
                viewGroup.getChildAt(i).setBackgroundColor(color);
            }
        }
    }

    /**
     * 更新 TextView 中的 Log 信息
     */
    public static void updateLogToView(TextView lifecycleMethodList) {
        lifecycleMethodList.setText(getLog());
    }

    /**
     * 从 logcat 获得 Log 信息
     * @return 包含 Log 信息的 String，每个 Log 信息被 newline 分隔
     */
    public static String getLog() {
        try {
            // 调用 Process 执行 Command
            Process process = Runtime.getRuntime().exec("logcat -d");
            // 对结果建立流
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            // StringBuilder 是可变的字符串（mutable）
            StringBuilder log = new StringBuilder();
            String line;
            // 读取下一行 Log，直到 EOF
            while ((line = bufferedReader.readLine()) != null) {
                // 如果 Log 包含 TAG
                if (line.contains(TAG)) {
                    // 精简 Log 信息，因为 readLine() 不包含 newline，所以手动添加
                    String reducedLog = line.substring(line.indexOf(TAG) + TAG.length() + 1) + '\n';
                    // 将 Log 插到原有 Log 的头部，以保持时间降序排序
                    /// @warning 可以用 LinkArray 优化
                    log.insert(0, reducedLog);
                }
            }
            return log.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "failed to read from logcat";
    }
}
