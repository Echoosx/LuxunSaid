package org.echoosx.mirai.plugin

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.*
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.info
import org.echoosx.mirai.plugin.utils.*
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

//import net.mamoe.mirai.event

object LuxunSaid : KotlinPlugin(
    JvmPluginDescription(
        id = "org.echoosx.mirai.plugin.LuxunSaid",
        name = "LuxunSaid",
        version = "0.1.0"
    ) {
        author("Echoosx")
    }
) {
    override fun onEnable() {
        logger.info { "LuxunSaid loaded" }
        //配置文件目录 "${dataFolder.absolutePath}/"
        GlobalEventChannel.subscribeGroupMessages(
            priority = EventPriority.LOW
        ){
            startsWith("鲁迅说 "){ text->
                if (text == "") return@startsWith
                val bgImg = ImageIO.read(LuxunSaid::class.java.classLoader.getResource("luxun.jpg"))
//                val text = message.contentToString().substring(4)
                val name = "———鲁迅"
                lateinit var result: ExternalResource
                lateinit var os: ByteArrayOutputStream
                try {
                    val srcImgWidth: Int = bgImg.getWidth(null) //获取图片的宽
                    val srcImgHeight: Int = bgImg.getHeight(null) //获取图片的高
                    val bufImg = BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB)
                    val g = bufImg.createGraphics()

                    // 背景
                    g.drawImage(bgImg, 0, 0, srcImgWidth, srcImgHeight, null)

                    // 文字
                    val fontsize = getFontSize(text)
                    g.font = Font("Microsoft Yahei Mono", Font.BOLD, fontsize) //设置字体
                    g.color = Color(22,22,22) //设置字体颜色
                    g.setRenderingHint(     // 消除锯齿
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                    )

                    if(text.length <= 30){
                        val x:Int = getCentBaseLine(text, srcImgWidth, fontsize)
                        if(text.length <= 15){
                            val y:Int = srcImgWidth - (fontsize + 5) * 2
                            g.drawString(text,x,y)
                            g.font = Font("Microsoft Yahei Mono", Font.BOLD, fontsize-3)
                            g.drawString(name, getRightBaseLine(name, srcImgWidth, fontsize - 3),y+fontsize+5)
                        } else {
                            val newbreak = getBreak(text,14)
                            if(newbreak==text.length-1){
                                val y:Int = srcImgWidth - (fontsize + 5) * 2
                                g.drawString(text,x,y)
                                g.font = Font("Microsoft Yahei Mono", Font.BOLD, fontsize-3)
                                g.drawString(name, getRightBaseLine(name, srcImgWidth, fontsize - 3),y+fontsize+5)
                            }else {
                                val y: Int = srcImgWidth - (fontsize + 5) * 3
                                g.drawString(text.substring(0..newbreak), x, y)
                                g.drawString(text.substring(newbreak+1), x, y + fontsize + 2)
                                g.font = Font("Microsoft Yahei Mono", Font.BOLD, fontsize - 3)
                                g.drawString(name,getRightBaseLine(name, srcImgWidth, fontsize - 3),y + 2 * fontsize + 5)
                            }
                        }
                        g.dispose()

                        // 输出图片
                        os = ByteArrayOutputStream()
                        os.use { it.close() }
                        ImageIO.write(bufImg,"jpg",os)
                        result = os.toByteArray().toExternalResource()
                        result.use { it.close() }
                        subject.sendImage(result)
                    }else
                        group.sendMessage("不得超过30个字符")

                }catch (e: IOException) {
                    logger.error(e)
                    group.sendMessage("图片绘制失败")
                }
            }
        }

        val cacheClear = CacheClear()
        Timer().schedule(cacheClear, Date(), 60 * 30 * 1000)
    }
}