package com.itchunyang.edittext;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * android:singleLine="true"//设置单行输入，一旦设置为true，则文字不会自动换行。
 * android:maxLines="5" 这能设置可视的最大行数
 * android:lines设置文本的行数，设置两行就显示两行，即使第二行没有数据。
 *
 * android:textColor = "#ff8c00"//字体颜色
 * android:textStyle="bold"
 * android:textAlign="center"//EditText没有这个属性，但TextView有，居中
 * android:textColorHighlight="#cccccc"//被选中文字的底色，默认为蓝色
 * android:textColorHint="#ffff00"//设置提示信息文字的颜色
 * android:textScaleX="1.5"//控制字与字之间的间距
 * android:typeface="monospace"//字型，normal, sans, serif, monospace
 * android:textAppearance="?android:attr/textAppearanceLargeInverse"//文字外观
 *
 * et.setSelection(et.length());//调整光标到最后一行
 * android:background="@null"//背景，这里没有，指透明
 * android:capitalize //首字母大写
 *
 * 一下用inputType替换
 * android:digits 设置只接受某些数字 如123456789.+-*、()
 * android:numeric //只接受数字
 * android:phoneNumber //输入电话号码 -->
 * android:autoText //自动拼写帮助 将自动执行输入值的拼写纠正。 android:inputType="textNoSuggestions"
 *
 * android：editable //是否可编辑
 * android:autoLink="all" //设置文本超链接样式当点击网址时，跳向该网址
 * android:cursorVisible设定光标为显示/隐藏，默认显示。
 * android:drawableBottom在text的下方输出一个drawable，如图片。如果指定一个颜色的话会把text的背景设为该颜色，并且同时和background使用时覆盖后者。
 * android:drawableLeft在text的左边输出一个drawable，如图片。
 * android:drawablePadding设置text与drawable(图片)的间隔，与drawableLeft、drawableRight、drawableTop、drawableBottom一起使用，可设置为负数，单独使用没有效果。
 * android:drawableRight在text的右边输出一个drawable，如图片。
 * android:drawableTop在text的正上方输出一个drawable，如图片。
 *
 * android:ellipsize设置当文字过长时,该控件该如何显示。有如下值设置："start"—?省略号显示在开头;"end"——省略号显示在结尾;"middle"—-省略号显示在中间;"marquee" ——以跑马灯的方式显示(动画横向移动)
 * android:inputMethod为文本指定输入法，需要完全限定名(完整的包名)。例如：com.google.android.inputmethod.pinyin，但是这里报错找不到。
 * android:inputType设置文本的类型，用于帮助输入法显示合适的键盘类型。在EditView中再详细说明，这里无效果。
 * android:maxLength限制显示的文本长度，超出部分不显示。
 *
 *
 */
public class MainActivity extends Activity {

    private EditText et;
    private EditText et_en;
    public static final String TAG = "EditText";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.et);
        et_en = (EditText) findViewById(R.id.et_en);

        et.requestFocus();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //这个方法被调用，说明在s字符串中，从start位置开始的count个字符即将被长度为after的新文本所取代。在这个方法里面改变s，会报错。
                Log.i(TAG, "beforeTextChanged: " +" s="+s + " start="+start+" count="+count+" after="+after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //这个方法被调用，说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本。在这个方法里面改变s，会报错
                Log.i(TAG, "onTextChanged: " +" s="+s + " start="+start+" count="+count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //这个方法被调用，那么说明s字符串的某个地方已经被改变。
                Log.i(TAG, "afterTextChanged: s="+s.toString());

            }
        });

        /**
         * 改变输入法中回车按钮的显示内容,如果回车按钮是执行搜索功能，则回车按钮上显示”搜索”，如果是执行发送功能，则显示”发送”,如果是下一步，则显示”下一步”。
         * IME_ACTION_SEARCH 搜索
         * IME_ACTION_SEND 发送
         * IME_ACTION_NEXT 下一步
         * IME_ACTION_DONE 完成
         */
        et.setImeOptions(EditorInfo.IME_ACTION_DONE);


        //监听软键盘Action事件  TextView 里的方法
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                actionId = EditorInfo.IME_ACTION_DONE ...

                //当actionId == XX_SEND 或者 XX_DONE时都触发x`
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if(actionId == EditorInfo.IME_ACTION_DONE  || actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)){
                    //处理回车事件
                    Toast.makeText(MainActivity.this,"完成填写",Toast.LENGTH_LONG).show();
                    EditText e = (EditText) v;
                    e.getText().clear();
                }

                return false;
            }
        });

        //为文本设置阴影的水平偏移
//        et.setShadowLayer(1,3,3, Color.BLACK);

        //对输入的内容可进行过滤限制
        //如果直接添加限制字数的规则，直接newLengthFilter就可以了，例如限制输入最大不超过16位：
//        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

        et_en.setText("hello");
        //如果此时你还有另一个规则，例如只能输入a-z的小写字母，直接在InputFilter数组中添加就可以了。
        et_en.setFilters(new InputFilter[]{new InputFilter.LengthFilter(26),new LetterFilter()});
//        et_en.setEnabled(false);

        //一个Activity上面一栏有EditText，每次打开窗口，EditText就会自动获取焦点并弹出输入法，遮盖了下面的列表，这样会让
        //使用者很不爽，所以就要禁止输入法自动弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //方案2：在Manifest.xml文件中的该Activity的注册信息下，加入属性： android:windowSoftInputMode="statehidden"


        Drawable delete = ContextCompat.getDrawable(this,R.drawable.delete);
        delete.setBounds(0,0,delete.getMinimumWidth(),delete.getMinimumHeight());
        et_en.setCompoundDrawables(null,null, delete,null);

        //Google官方API并没有给出一个直接的方法用来设置右边图片的点击事件，所以这里我们需要通过点击位置来判断点击事件
        et_en.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable[] drawables = et_en.getCompoundDrawables();
                for (int i = 0; i < drawables.length; i++) {
                    if(drawables[i] == null)
                        continue;
                    System.out.println("第"+i+"张图片 width="+drawables[i].getBounds().width()+
                            ",height="+drawables[i].getBounds().height());
                }

                Drawable drawable = drawables[2];
                if(drawable == null)
                    return false;

                if(event.getAction() != MotionEvent.ACTION_UP){
                    return false;
                }

                if(event.getX() > et_en.getWidth() - et_en.getPaddingRight() - drawable.getIntrinsicWidth()){
                    final EditText et = (EditText) v;


                    Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
                    translateAnimation.setInterpolator(new CycleInterpolator(3));
                    translateAnimation.setDuration(400);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            et.getText().clear();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    et.startAnimation(translateAnimation);

                }
                return false;
            }
        });


    }

    public void start(View view) {
        //设置选中某一段文本,可以方便的删除替换等
        et.setSelection(3,7);

        /**
         *  Editable与String有什么区别？
         *  Editable 是一个接口类型，对它的实例化对象作出任何改变都是对原有的实例化对象操作的，内存地址还是原来的那个。
         *  而对 String 的任何改变都是相当于重新实例化了一个 String 类出来，相当于重新分配了内存地址。
         */
    }

    //项目要求注册用户时屏蔽空格，使用InputFilter类来过滤空格
    class LetterFilter implements InputFilter{

        //返回空字符串，就代表匹配不成功，返回null代表匹配成功

        /**
         *
         * @param source 输入的文字
         * @param start  开始位置
         * @param end    结束位置
         * @param dest   输入之前文本框内容
         * @param dstart 原内容起始坐标，一般为0
         * @param dend   原内容终点坐标，一般为dest长度-1
         * @return
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // source=w   start=0 end=1 dest=   dstart=0 dend=0
            // source=wy  start=0 end=2 dest=w  dstart=0 dend=1
            // source=wyg start=0 end=3 dest=wy dstart=0 dend=2
            Log.i(TAG, "filter: "+"source="+source +" start="+start+" end="+end+" dest="+dest+" dstart="+dstart+" dend="+dend);

            //return "" 代表匹配失败,输入的不是英文!
            if(!source.toString().matches("^[a-zA-Z]*"))
                return "";


            //返回null代表匹配成功,确实是英文! 保持文本原样不变（keep original）
            //如果filter后的结果不是null，更新text变量,所有InputFilter后的结果就是最终显示在TextView中的文本。
            return null;
        }
    }
}
