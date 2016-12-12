package com.heima.rxbindingdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.MainThreadSubscription;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        传统方法
        
//        麻痹的事件处理的代码是在oncreate重写方法内完成，而不是在外面设置多个函数套进去。
//        这样在oncreate内就可以findviewbyid，其实想起来这个之前学打通用3步时，只有定义类的成员变量在方法外设定
//            而findviewbyid是在方法内。第三步就是增加监听器。
//        Button b = (Button) findViewById(R.id.button);
//        b.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//
//            }
//        });

////
//               以RxJava的形式来处理UI事件，对button点击事件的常规处理方式

//        Button b = (Button) findViewById(R.id.button);
////        麻痹的rxjava代码补全，是要打到new actionl才补全重写下面call方法
////        这例子就是区别于传统监听事件的用法
//        Subscription buttonSub = RxView.clicks(b).subscribe(new Action1<Void>()
//        {
//            @Override
//            public void call(Void aVoid)
//            {
////do some work here
//            }
//        });
//



//        如何用RxBinding完全实现TextWatcher。

//        创建类实现Observable.OnSubscribe<CharSequence>接口时自动补全call重写方法。然后再在里面创建下面提到的3个重写方法
        final class TextViewTextOnSubscribe implements Observable.OnSubscribe<CharSequence>
        {
            final TextView view;

            TextViewTextOnSubscribe(TextView view)
            {
                this.view = view;
            }

            @Override
            public void call(final Subscriber<? super CharSequence> subscriber)
            {
                checkUiThread();
//                原来before,on,after等textchanged方法重写的代码补全是创建textwacher()
//                        返回watcher对象时可以自动补全。
                final TextWatcher watcher = new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i, int i1, int i2)
                    {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2)
                    {
                        if (!subscriber.isUnsubscribed())
                        {
                            subscriber.onNext(s);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {

                    }
                };
//                调用view对象，使用增加textchanged监听器传入watcher，
//                之后触发补全onunsubscribe重写方法。在里面再次调用view的removexxxxx方法
                view.addTextChangedListener(watcher);
                subscriber.add(new MainThreadSubscription() {
                    @Override
                    protected void onUnsubscribe()
                    {
                        view.removeTextChangedListener(watcher);
                    }
                });
                //Emit initial value.
//                最后调用subscriber订阅对象的onnext方法传入view的gettext方法。
                subscriber.onNext(view.getText());
            }
        }
    }
}
