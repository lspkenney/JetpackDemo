package top.kenney.baselibrary.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
/**
 * 打开或关闭软键盘
 *
 *
 */
object KeyBoardUtils {
    /**
     * 打卡软键盘
     *
     * @param mView
     */
    fun openKeybord(
        mView: View) {
        val imm = mView.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mView, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    /**
     * 关闭软键盘
     *
     * @param mView
     * 输入框
     */
    @JvmStatic
    fun closeKeybord(
        mView: View
    ) {
        val imm = mView.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mView.windowToken, 0)
    }
}