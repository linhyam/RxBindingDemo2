package com.heima.rxbindingdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class Main2Activity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        可以实现类型转换
//        使用RxBinding之后，你可以使用RxJava operators来对响应的内容进行实时转换。让我们来看一下这个例子：
//
//        假设你想察看一个EditText输入文字时文本的变化(查看指定类型的数据)。
//        EditText的原始文本类型是CharSequence，而你要获取倒序的String类型的文本，你可以这样：
//

        final TextView nameLabel = (TextView) findViewById(R.id.name_label);
        final EditText name = (EditText)findViewById(R.id.name);
//        输入到funcl自动补全重写call方法
        Subscription editTextSub = RxTextView.textChanges(name).map(new Func1<CharSequence, String>()
        {
            @Override
            public String call(CharSequence charSequence)
            {
                return new StringBuilder(charSequence).reverse().toString();
            }
            //跟著}).輸入subscribe傳入创建一个新actionl重写call方法
        }).subscribe(new Action1<String>()
        {
            @Override
            public void call(String value)
            {
                nameLabel.setText(value);
            }
        });

    }
}
