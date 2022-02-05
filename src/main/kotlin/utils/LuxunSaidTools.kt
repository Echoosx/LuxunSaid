
package org.echoosx.mirai.plugin.utils

// 获取内容的基线位置(x)
fun getCentBaseLine(text: String, srcImgWidth: Int,fontsize:Int): Int {
    return if(text.length<=15) {
        val fullCharNum = countFullChar(text)
        srcImgWidth / 2 - (text.length-fullCharNum) * fontsize * 3/10 - (fullCharNum * fontsize) / 2
    }
    else {
        val newBreak = getBreak(text,14)
        val fullCharNum = countFullChar(text.substring(0..newBreak))
        srcImgWidth / 2 - (newBreak+1-fullCharNum) * fontsize * 3/11 - (fullCharNum * fontsize) / 2
    }
}

// 获取署名的基线位置(y)
fun getRightBaseLine(text: String, srcImgWidth:Int,fontsize:Int): Int{
    return srcImgWidth - text.length * fontsize - 15
}

// 获取字体大小
fun getFontSize(text: String): Int{
    return if(text.length <= 10)
        45
    else if(text.length <= 15)
        512/text.length
    else
        35
}

//计算全宽字符个数
fun countFullChar(text:String):Int{
    var num = 0
    for(tmp in text){
        if (isChinese(tmp) or isJapanese(tmp))
            num ++
    }
    return num
}

/**
 * 判断是否为中文或中文字符
 */
fun isChinese(ch: Char): Boolean {
    //获取此字符的UniCodeBlock
    val ub = Character.UnicodeBlock.of(ch)
    //  GENERAL_PUNCTUATION 判断中文的“号
    //  CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
    //  HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
    if (ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION) {
        return true
    }
    return false
}
/**
 * 判断是否为日文
 */
fun isJapanese(ch: Char):Boolean {
    val japaneseUnicodeBlocks = HashSet<Character.UnicodeBlock>()
    japaneseUnicodeBlocks.add(Character.UnicodeBlock.HIRAGANA)
    japaneseUnicodeBlocks.add(Character.UnicodeBlock.KATAKANA)
    japaneseUnicodeBlocks.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
    return japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(ch))
}

/**
 * 获取新的断句位置
 * breakpoint为断开前的位置 如0-14、15-20 breakpoint值为14
 */
fun getBreak(text:String,breakpoint:Int):Int{
    if(breakpoint>=text.length-1)
        return breakpoint
    var res = breakpoint
    var tmp: Char
    var tmpNext: Char
    for(i in breakpoint until text.length-1){
        tmp = text[i]
        tmpNext = text[i+1]
        if((tmp in 'A'..'Z' || tmp in 'a'..'z') && (tmpNext in 'A'..'Z' || tmpNext in 'a'..'z')){
            res++
        }else{
            break
        }
    }
    return res
}