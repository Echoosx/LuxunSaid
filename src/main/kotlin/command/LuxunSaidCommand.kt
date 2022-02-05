package org.echoosx.mirai.plugin.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import org.echoosx.mirai.plugin.LuxunSaid
import org.echoosx.mirai.plugin.utils.getBreak
import org.echoosx.mirai.plugin.utils.getCentBaseLine
import org.echoosx.mirai.plugin.utils.getFontSize
import org.echoosx.mirai.plugin.utils.getRightBaseLine
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

object LuxunSaidCommand:SimpleCommand(
    LuxunSaid,
    "lxs","鲁迅说",
    description = "鲁迅说"
) {

    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    @JvmStatic
    private val bgImg = ImageIO.read(LuxunSaid::class.java.classLoader.getResource("luxun.jpg"))
    private val fontIS = LuxunSaid::class.java.classLoader.getResourceAsStream("Microsoft-Yahei-Mono.ttf")
    private val defaultFont = Font.createFont(Font.TRUETYPE_FONT, fontIS)

    @Suppress("unused")
    @Handler
    suspend fun CommandSender.handle(vararg args:String){
        val text = args.joinToString("")
        if(text == "") return

        val name = "———鲁迅"
        lateinit var result: ExternalResource
        lateinit var os: ByteArrayOutputStream
        try {
            //获取图片的宽
            val srcImgWidth: Int = bgImg.getWidth(null)
            //获取图片的高
            val srcImgHeight: Int = bgImg.getHeight(null)
            val bufImg = BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB)
            val g = bufImg.createGraphics()

            // 背景
            g.drawImage(bgImg, 0, 0, srcImgWidth, srcImgHeight, null)

            // 文字
            val fontsize = getFontSize(text)

            g.font = defaultFont.deriveFont(Font.BOLD,fontsize.toFloat())   //设置字体
            g.color = Color(22,22,22) //设置字体颜色
            g.setRenderingHint(     // 消除锯齿
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            )

            // 字符数限定在30以内
            if(text.length <= 30){
                // 获取内容的x轴偏移量
                val x:Int = getCentBaseLine(text, srcImgWidth, fontsize)
                // 字符数在15以内
                if(text.length <= 15){
                    val y:Int = srcImgWidth - (fontsize + 5) * 2
                    g.drawString(text,x,y)
                    g.font = defaultFont.deriveFont(Font.BOLD, fontsize.toFloat()-3)
                    g.drawString(name, getRightBaseLine(name, srcImgWidth, fontsize - 3),y+fontsize+5)
                } else {
                    val newBreak = getBreak(text,14)
                    if(newBreak==text.length-1){
                        val y:Int = srcImgWidth - (fontsize + 5) * 2
                        g.drawString(text,x,y)
                        g.font = defaultFont.deriveFont(Font.BOLD, fontsize.toFloat()-3)
                        g.drawString(name, getRightBaseLine(name, srcImgWidth, fontsize - 3),y+fontsize+5)
                    }else {
                        val y: Int = srcImgWidth - (fontsize + 5) * 3
                        g.drawString(text.substring(0..newBreak), x, y)
                        g.drawString(text.substring(newBreak+1), x, y + fontsize + 2)
                        g.font = defaultFont.deriveFont(Font.BOLD, fontsize.toFloat()-3)
                        g.drawString(name, getRightBaseLine(name, srcImgWidth, fontsize - 3),y + 2 * fontsize + 5)
                    }
                }
                g.dispose()

                // 输出图片
                os = ByteArrayOutputStream()
                os.use { it.close() }
                withContext(Dispatchers.IO) {
                    ImageIO.write(bufImg, "jpg", os)
                }
                result = os.toByteArray().toExternalResource()
                result.use { it.close() }
                subject?.sendImage(result)
            }else
                sendMessage("不得超过30个字符")

        }catch (e: IOException) {
            LuxunSaid.logger.error(e)
            sendMessage("图片绘制失败")
        }
    }
}