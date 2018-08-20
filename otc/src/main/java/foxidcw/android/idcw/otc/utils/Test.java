package foxidcw.android.idcw.otc.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.cjwsc.idcm.Utils.LogUtil;

public class Test implements InputFilter {
    private static final int DECIMAL_DIGITS = 2;//小数的位数
    private static final int INTEGER_DIGITS = Integer.MAX_VALUE;//整数位的位数
    private static final int TOTAL_DIGITS = Integer.MAX_VALUE; //整数部分 + “小数点” + 小数部分
    private int currentLimitDigits = INTEGER_DIGITS;//当前控制的位数值
//    CharSequence source
//    为即将输入的字符串
//    int start
//    source的start, start 为0
//
//    int end
//    source的end ，因为start为0，end也可理解为source长度了
//
//    Spanned dest
//    输入框中原来的内容
//
//    int dstart
//    要替换或者添加的起始位置，即光标所在的位置
//    int dend
//    要替换或者添加的终止始位置，若为选择一串字符串进行更改，则为选中字符串 最后一个字符在dest中的位置

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceStr = source.toString();
        String destStr = dest.toString();
        if ("".equals(sourceStr)) return null;
        /**
         * 1.判断首位是否是-  如果是不让再输入-
         */
        //if (destStr.startsWith("-"))

//         语言中指针是很容易学习的， 语言中使用指针可以更简单的执行一些任务。
//
//        接下来让我们来一步步学习 Go 语言指针。
//
//        我们都知道，变量是一种使用方便的占位符，用于引用计算机内存地址。
//
//         语言的取地址符是 &，放到一个变量前使用就会返回相应变量的内存地址。
//
//        以下实例演示了变量在内存中地址：
//
//package main
//
//import "fmt"
//
//        func main() {
//            var a int = 10
//
//            fmt.Printf("变量的地址: %x\n", &a  )
//        }
//        执行以上代码输出结果为：
//
//        变量的地址: 20818a220
//        现在我们已经了解了什么是内存地址和如何去访问它。接下来我们将具体介绍指针。
//
//        什么是指针
//        一个指针变量指向了一个值的内存地址。
//
//        类似于变量和常量，在使用指针前你需要声明指针。指针声明格式如下：
//
//        var var_name *var-type
//        var-type 为指针类型，var_name 为指针变量名，* 号用于指定变量是作为一个指针。以下是有效的指针声明：
//
//        var ip *int        /* 指向整型*/
//        var fp *float32    /* 指向浮点型 */
//        本例中这是一个指向 int 和 float32 的指针。
//
//        如何使用指针
//        指针使用流程：
//
//        定义指针变量。
//        为指针变量赋值。
//        访问指针变量中指向地址的值。
//        在指针类型前面加上 * 号（前缀）来获取指针所指向的内容。
//
//package main
//
//import "fmt"
//
//        func main() {
//            var a int= 20   /* 声明实际变量 */
//            var ip *int        /* 声明指针变量 */
//
//            ip = &a  /* 指针变量的存储地址 */
//
//            fmt.Printf("a 变量的地址是: %x\n", &a  )
//
//   /* 指针变量的存储地址 */
//            fmt.Printf("ip 变量储存的指针地址: %x\n", ip )
//
//   /* 使用指针访问值 */
//            fmt.Printf("*ip 变量的值: %d\n", *ip )
//        }
//        以上实例执行输出结果为：
//
//        a 变量的地址是: 20818a220
//        ip 变量储存的指针地址: 20818a220
//                *ip 变量的值: 20
//         空指针
//        当一个指针被定义后没有分配到任何变量时，它的值为 nil。
//
//        nil 指针也称为空指针。
//
//        nil在概念上和其它语言的null、None、nil、NULL一样，都指代零值或空值。
//
//        一个指针变量通常缩写为 ptr。
//
//        查看以下实例：
//
//package main
//
//import "fmt"
//
//        func main() {
//            var  ptr *int
//
//            fmt.Printf("ptr 的值为 : %x\n", ptr  )
//        }
//        以上实例输出结果为：
//
//        ptr 的值为 : 0
//        空指针判断：
//
//        if(ptr != nil)     /* ptr 不是空指针 */
//            if(ptr == nil)    /* ptr 是空指针 */

        if (dstart != 0 && "-".equals(sourceStr)) return "";
        if (destStr.startsWith("-") && destStr.length() == 1 && dstart == 1 && sourceStr.equals(".")) return "";
        if (destStr.contains("-") && sourceStr.equals("-")) return "";
        if (dstart == 0 && (".".equals(sourceStr) || destStr.contains("-")))
            return "";
        if (destStr.contains(".") && sourceStr.equals(".")) return "";
        String dValue = destStr;
        String[] splitArray = dValue.split("\\.");
        switch (splitArray.length) {
            case 1:
                //判断当前是否有小数点，如果有小数点就把控制位数变为TOTAL_DIGITS(其实只要比整数位数+1大就可以)
                if (dValue.indexOf(".") != -1) {
                    currentLimitDigits = TOTAL_DIGITS;
                } else {
                    //如果没有小数点，则继续控制前面整数位的位数为6位
                    currentLimitDigits = INTEGER_DIGITS;
                }
                /**这里如果我们直接输入999999时候，其实已经不能按其他数字了，
                 不然就超过一百万了，但是这时候如果输入的是小数点，则可以在输入框中显示小数点。
                 而且这时候在上面已经把当前的位数限制变大，
                 这时候就可以就可以输入其他数字，然后接下去就会跳入到下面的case 2的判断了。
                 **/
                if (splitArray[0].length() >= currentLimitDigits && !".".equals(sourceStr)) {
                    return "";
                }
                break;
            case 2:
                String integerValue = splitArray[0];
                String dotValue = splitArray[1];
                int dotIndex = dValue.indexOf(".");
                if (integerValue.length() >= INTEGER_DIGITS && dstart <= dotIndex) {
                    return "";
                }
                if (dotValue.length() >= DECIMAL_DIGITS && dstart > dotIndex) {
                    return "";
                }
                break;
            default:
                break;
        }
        LogUtil.e("source --- > " + source);
        LogUtil.e("start --- > " + start);
        LogUtil.e("end --- > " + end);
        LogUtil.e("dest --- > " + dest);
        LogUtil.e("dstart --- > " + dstart);
        LogUtil.e("dend --- > " + dend);
        return null;
    }
}
