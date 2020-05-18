package top.kenney.baselibrary.update

import com.daimajia.numberprogressbar.NumberProgressBar

/**
 * Created by Kenney on 2019-02-28 13:54
 */
interface UpdateDialogFragmentListener {
    fun onUpdate(nbp: NumberProgressBar)
    fun onCancel()
}