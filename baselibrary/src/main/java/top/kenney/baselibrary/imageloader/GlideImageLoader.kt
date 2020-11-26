package top.kenney.baselibrary.imageloader

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lzy.imagepicker.loader.ImageLoader
import top.kenney.baselibrary.R

class GlideImageLoader : ImageLoader {
    override fun displayImage(
        activity: Activity,
        path: String,
        imageView: ImageView,
        width: Int,
        height: Int
    ) {
        Glide.with(activity) //配置上下文
            //.load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
            .load(path) //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
            .error(R.drawable.ic_default_image) //设置错误图片
            .placeholder(R.drawable.ic_default_image) //设置占位图片
            .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存全尺寸
            .into(imageView)
    }

    override fun displayImagePreview(
        activity: Activity,
        path: String,
        imageView: ImageView,
        width: Int,
        height: Int
    ) {
        Glide.with(activity) //配置上下文
            //.load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
            .load(path) //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
            .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存全尺寸
            .into(imageView)
    }

    override fun clearMemoryCache() {}
}