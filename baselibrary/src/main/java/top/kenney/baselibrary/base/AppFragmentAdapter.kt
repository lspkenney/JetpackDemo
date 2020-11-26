package top.kenney.baselibrary.base

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by Kenney on 2019-05-28 18:23
 */
class AppFragmentAdapter(fm: FragmentManager, var list:List<Fragment>): FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT){
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}