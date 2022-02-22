package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.echoosx.mirai.plugin.command.LuxunSaidCommand
import org.echoosx.mirai.plugin.utils.*
import java.util.*

//import net.mamoe.mirai.event

object LuxunSaid : KotlinPlugin(
    JvmPluginDescription(
        id = "org.echoosx.mirai.plugin.LuxunSaid",
        name = "LuxunSaid",
        version = "1.0.2"
    ) {
        author("Echoosx")
    }
) {
    override fun onEnable() {
        logger.info { "LuxunSaid loaded" }
        //配置文件目录 "${dataFolder.absolutePath}/"
//        GlobalEventChannel.subscribeGroupMessages(
//            priority = EventPriority.LOW
//        ) {
//            startsWith("鲁迅说 ") { text ->
//                if (text == "") return@startsWith
//            }
//        }

        LuxunSaidCommand.register()
        val cacheClear = CacheClear()
        Timer().schedule(cacheClear, Date(), 60 * 30 * 1000)
    }
}