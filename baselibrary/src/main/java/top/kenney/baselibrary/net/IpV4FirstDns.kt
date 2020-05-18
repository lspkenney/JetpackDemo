package top.kenney.baselibrary.net

import okhttp3.Dns
import java.net.Inet4Address
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * Created by Kenney on 2019-09-29 14:44
 *   wifi 下接口响应很快，而在 4G 网络下，接口响应很慢问题，将ipv4优先设置成解析
 */
class IpV4FirstDns : Dns {
    override fun lookup(hostname: String?): MutableList<InetAddress> {
        if (hostname == null) {
            throw UnknownHostException("hostname == null")
        }
        try {
            val inetAddressList = mutableListOf<InetAddress>()
            val allByName = InetAddress.getAllByName(hostname);
            allByName.forEach {
                if (it is Inet4Address) {//将ipv4放在dns解析列表第一个位置
                    inetAddressList.add(0, it)
                } else {
                    inetAddressList.add(it)
                }
            }
            return inetAddressList
        } catch (var4: NullPointerException) {
            val unknownHostException = UnknownHostException("Broken system behaviour for dns lookup of $hostname")
            unknownHostException.initCause(var4)
            throw unknownHostException
        }

    }
}