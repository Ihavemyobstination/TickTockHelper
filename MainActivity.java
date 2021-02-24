package com.wang.hongbaotest;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {


    public static final String version="17.5";//抖音版本17.5 17.6
    public static final boolean ISSLIDE=true;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int COUNTTEXT=1;
    private static final String SAVE_NAME="save_come_phone";
    private static final String SAVE_PHONE="save_phone";
    private static final String SAVE_COME="save_come";
    private static final String SAVE_NM="save_nm";
    private static final String SAVE_COUNTRY="save_country";
    private static final String GOSTART="My name is ";
    private static final String GOEND=" I will be busy with other things later, and I don't often use Tiktok. Or you can add my personal contact information, you can send a message to me, I will give you a message after I see, I hope to make a friend with you!";

    private String phoneNb="+85255353520";
    private String name="Andy";
    private String comeFrom="HonKong";
    private int cout=0;
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_come;
    private EditText edit_country;
    private TextView acout_cout;
    public boolean isMSRun=false;//发送消息方法是否运行 false为运行防止多次点击
    public boolean isMSDo=false;//发送消息线程是否运行 false为运行
    public boolean isGZRun=false;//关注方法是否运行 false为运行防止多次点击
    public boolean isGZDo=false;//关注线程是否运行 false为运行
    private int sumTime=0;
    private int msgDont=0;
    private DisplayMetrics displayMetrics;
    Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case COUNTTEXT:acout_cout.setText(msg.obj.toString());break;

                default:break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        displayMetrics = getResources().getDisplayMetrics();
        edit_name=findViewById(R.id.name_in);
        edit_phone=findViewById(R.id.phone);
        edit_come=findViewById(R.id.come);
        edit_country=findViewById(R.id.come_country_commit_text);
        acout_cout=findViewById(R.id.acount_count);
        SharedPreferences con=getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);
        name=con.getString(SAVE_NM,name);
        phoneNb=con.getString(SAVE_PHONE,phoneNb);
        comeFrom=con.getString(SAVE_COME,comeFrom);
        setCountryPhone(con.getString(SAVE_COUNTRY,null));
        edit_country.setText(con.getString(SAVE_COUNTRY,null));
        edit_name.setText(name);
        edit_phone.setText(phoneNb);
        edit_come.setText(comeFrom);
        checkPermission();
        findViewById(R.id.come_country_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences con=getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = con.edit();
                editor.putString( SAVE_COUNTRY, edit_country.getText().toString());
                editor.commit();
                setCountryPhone(edit_country.getText().toString());
                System.out.println("0");
            }
        });
        findViewById(R.id.phone_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                System.out.println("R.id.phone_commit is be clicked!");
                SharedPreferences con=getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = con.edit();
                editor.putString( SAVE_PHONE, edit_phone.getText().toString());
                editor.commit();
                phoneNb=edit_phone.getText().toString();
            }
        });

        findViewById(R.id.name_commit_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("R.id.name_commit_2 is be clicked!");
                SharedPreferences con=getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = con.edit();
                editor.putString( SAVE_NM, edit_name.getText().toString());
                editor.commit();
                name=edit_name.getText().toString();
            }
        });

        findViewById(R.id.come_commit_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("R.id.come_commit is be clicked!");
                SharedPreferences con=getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = con.edit();
                editor.putString( SAVE_COME, edit_come.getText().toString());
                editor.commit();
                comeFrom=edit_come.getText().toString();
            }
        });

        //屏幕横滑手势
        findViewById(R.id.bt_main_ShouShi).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

//                System.out.println("dogz is run:"+isGZDo+":"+isGZRun+":"+isMSDo+":"+isMSRun);
//                if(isGZRun){
//                    return;
//                }else{
//                    isGZRun=true;
//                }
//                isMSRun=false;
//                isMSDo=true;
//                isGZDo=false;

                sumTime=0;
                dogz();
            }
        });


        //点击指定控件
        findViewById(R.id.bt_main_DianJi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(isMSRun){
//                    return;
//                }else{
//                    isMSRun=true;
//                }
//                isGZRun=false;
//                isMSDo=false;
//                isGZDo=true;

                sumTime=0;
                doMesgRunnable();
            }
        });

        //用手势长按指定控件
        findViewById(R.id.bt_main_ChangAn).setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    Toast.makeText(MainActivity.this, "7.0及以上才能使用手势", Toast.LENGTH_SHORT).show();
                    return;
                }
                AccessibilityNodeInfo ces = HongBaoService.mService.findFirst(AbstractTF.newText("测试控件", true));
                if (ces == null) {
                    Utils.toast("找测试控件失败");
                    return;
                }

//                ces.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);//长按

                //这里为了示范手势的效果
                Rect absXY = new Rect();
                ces.getBoundsInScreen(absXY);
//                HongBaoService.mService.dispatchGestureClick(absXY.left + (absXY.right - absXY.left) / 2, absXY.top + (absXY.bottom - absXY.top) / 2);//手势点击效果
                //手势长按效果
                //控件正中间
                HongBaoService.mService.dispatchGestureLongClick(absXY.left + (absXY.right - absXY.left) / 2, absXY.top + (absXY.bottom - absXY.top) / 2);
            }
        });

        //用系统的返回效果
        findViewById(R.id.bt_main_FanHui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        });

        //测试的控件
//        View viewCes = findViewById(R.id.bt_main_CeShi);
//        viewCes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.toast("'测试控件'被点击了");
//                Log.e(TAG, "onClick: '测试控件'被点击了");
//            }
//        });
//        viewCes.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Utils.toast("'测试控件'被长按了");
//                Log.e(TAG, "onLongClick: '测试控件'被长按了");
//                return true;
//            }
//        });
//        if(System.currentTimeMillis()/1000>1601125503) isRun=true;
        System.out.println("System.currentTimeMillis()/1000 is :"+System.currentTimeMillis()/1000);
    }
    @SuppressLint("NewApi")
    private void dogz(){
        System.out.println("dogz is run:"+isGZDo+":"+isGZRun+":"+isMSDo+":"+isMSRun);
        if(isGZDo) return;
        new Thread(){
            @Override
            public void run() {
                try {
                    AccessibilityNodeInfo ces1 = HongBaoService.mService.findFirst(AbstractTF.newText("關注", true));
                    AccessibilityNodeInfo gz_list = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.GZLIST)));
                    if (ces1 != null) {
                        sumTime=0;
                        HongBaoService.clickView(ces1);
                    }
                    else if (ISSLIDE&&gz_list != null) {//向下滑动
                        if(sumTime<4) {
                            Path path = new Path();
                            path.moveTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels * 7 / 8);
                            path.lineTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 8);
                            final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, 400);
                            HongBaoService.mService.dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(), new AccessibilityService.GestureResultCallback() {
                                @Override
                                public void onCompleted(GestureDescription gestureDescription) {
                                    super.onCompleted(gestureDescription);
                                    sumTime++;
//                                    Toast.makeText(MainActivity.this, "手势成功", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(GestureDescription gestureDescription) {
                                    super.onCancelled(gestureDescription);
//                                Toast.makeText(MainActivity.this, "手势失败，请重启手机再试", Toast.LENGTH_SHORT).show();
                                }
                            }, null);
                        }
                        else{
                            HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        }
                    }
                    Thread.sleep(1+(int) (1500*Math.random()));
                    dogz();
                } catch (InterruptedException e) {
                    isGZRun=false;
                    dogz();
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private Runnable mesgRun =new Runnable() {
        @SuppressLint("NewApi")
        @Override
        public void run() {
            try {
                if(isMSRun) return;
                //底部收件匣按钮
                AccessibilityNodeInfo msg_button = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGBUTTON)));
                //右上角未读消息按钮
                AccessibilityNodeInfo msg_button_in = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGBUTTONIN)));
                //消息列表未读消息数字text
                AccessibilityNodeInfo msg_friend_has = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGFRIENDHAS)));
                //消息发送EditText
                AccessibilityNodeInfo edit_msg = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.EDITMSG)));
                //消息列表
                AccessibilityNodeInfo msg_friend_list = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGFRIENDLIST)));
                //打个招呼
                AccessibilityNodeInfo msg_one_list;
                if (msg_button_in != null) {
//                        AccessibilityNodeInfo msg_count_button_in=msg_button.getChild(0);
//                        String msg_count_in = msg_count_button_in.getText().toString();
//                        Log.d(TAG,"msg_count_in is :"+msg_count_in);

                    sumTime=0;
                    HongBaoService.clickView(msg_button_in);
                }else if (msg_button != null) {
                    HongBaoService.clickView(msg_button);
                }else if (msg_friend_has != null) {
                    sumTime=msgDont=0;
                    HongBaoService.clickView(msg_friend_has);
                }else if ((msg_one_list = findoneRecursive())!=null) {
                    sumTime=msgDont=0;
                    HongBaoService.clickView(msg_one_list);
                    Thread.sleep(500+getRandomTime());
                    edit_msg = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.EDITMSG)));
//                        sendMsg("hi",edit_msg);
                    for(int i=0;i<Dialog_File.getInstance().DialogA.size();i++){
                        sendMsg(getAData(i), edit_msg);
                        if(getATime(i)>0){
                            Thread.sleep(getATime(i) + getRandomTime());
                        }
                        if(i==0){
                            testWarnin();
                        }
                    }
                    HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }else if (sumTime>4 && getContact()) {
                    sumTime=msgDont=0;
                    String phone=getCountry();
                    int size=Dialog_File.getInstance().DialogC.size();
                    for(int i=0;i<size-3;i++){
                        sendMsg(getCData(i), edit_msg);
                        if(getCTime(i)>0){
                            Thread.sleep(getCTime(i) + getRandomTime());
                        }
                        if(i==0){
                            testWarnin();
                        }
                    }
                    sendMsg(getCData(size-3) + name + getCData(size-2), edit_msg);
                    Thread.sleep(getCTime(size-2) + getRandomTime());
                    sendMsg(getCData(size-1), edit_msg);
                    Thread.sleep(getCTime(size-1) + getRandomTime());
                    saveAcount();
                    if(phone!=null) {
                        sendMsg(phone, edit_msg);
                    }else{
                        sendMsg(phoneNb,edit_msg);
                    }
                }
                else if (ISSLIDE&&msg_friend_list != null) {//向下滑动
                    if(sumTime<70) {
                        Path path = new Path();
                        path.moveTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels * 7 / 8);
                        path.lineTo(displayMetrics.widthPixels / 2, displayMetrics.heightPixels / 8);
                        final GestureDescription.StrokeDescription sd = new GestureDescription.StrokeDescription(path, 0, 400);
                        HongBaoService.mService.dispatchGesture(new GestureDescription.Builder().addStroke(sd).build(), new AccessibilityService.GestureResultCallback() {
                            @Override
                            public void onCompleted(GestureDescription gestureDescription) {
                                super.onCompleted(gestureDescription);
                                sumTime++;
                                msgDont++;
                                if(msgDont>4500){
                                    isMSRun=true;
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    new Thread(chageAcout).start();
                                }

//                                    Toast.makeText(MainActivity.this, "手势成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(GestureDescription gestureDescription) {
                                super.onCancelled(gestureDescription);
//                                Toast.makeText(MainActivity.this, "手势失败，请重启手机再试", Toast.LENGTH_SHORT).show();
                            }
                        }, null);
                    }else{
//                        sumTime=0;
                        HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    }
                }
                else if (edit_msg != null) {
                    sumTime=msgDont=0;
                    saveAcount();
                    int index=0;
                    AccessibilityNodeInfo ces=null;
                    while(ces==null&&index<Dialog_File.getInstance().DialogC.get(Dialog_File.getInstance().DialogC.size()-1).data.length){
                        ces = HongBaoService.mService.findFirst(AbstractTF.newText(Dialog_File.getInstance().DialogC.get(Dialog_File.getInstance().DialogC.size()-1).data[index], true));
                        index++;
                    }
//                        AccessibilityNodeInfo ces = HongBaoService.mService.findFirst(AbstractTF.newText(getCData(Dialog_File.getInstance().DialogC.size()-1), true));
                    if (ces == null) {
                        AccessibilityNodeInfo msg_list = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGLIST)));
                        if(msg_list.getChildCount()>Dialog_File.getInstance().DialogB.size()+1){
                            String phone=getCountry();
                            int size=Dialog_File.getInstance().DialogC.size();
                            for(int i=0;i<size-3;i++){
                                sendMsg(getCData(i), edit_msg);
                                if(getCTime(i)>0){
                                    Thread.sleep(getCTime(i) + getRandomTime());
                                }
                                if(i==0){
                                    testWarnin();
                                }
                            }
                            sendMsg(getCData(size-3) + name + getCData(size-2), edit_msg);
                            Thread.sleep(getCTime(size-2) + getRandomTime());
                            sendMsg(getCData(size-1), edit_msg);
                            Thread.sleep(getCTime(size-1) + getRandomTime());
                            saveAcount();
                            if(phone!=null) {
                                sendMsg(phone, edit_msg);
                            }else{
                                sendMsg(phoneNb,edit_msg);
                            }
                        }else {
                            int size=Dialog_File.getInstance().DialogB.size();
                            for(int i=0;i<size;i++){
                                if(i==size-2){
                                    sendMsg(getBData(i)+ comeFrom, edit_msg);
                                }else {
                                    sendMsg(getBData(i), edit_msg);
                                }
                                if(getCTime(i)>0){
                                    Thread.sleep(getBTime(i) + getRandomTime());
                                }
                                if(i==0){
                                    testWarnin();
                                }
                            }
                        }
                    }
                    HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }
                Thread.sleep(1000);
//                if(canSend)
                doMesgRunnable();
            } catch (InterruptedException e) {
                isMSRun=false;
                doMesgRunnable();
                e.printStackTrace();
            } catch (WarningException e) {
                HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//                Thread.sleep(500);
                new Thread(chageAcout).start();
                e.printStackTrace();
            }
        }
    };
    @SuppressLint("NewApi")
    private void doMesgRunnable(){
        System.out.println("doMesgRunnable is run:"+isGZDo+":"+isGZRun+":"+isMSDo+":"+isMSRun);
        new Thread(mesgRun).start();
    }
    public AccessibilityNodeInfo findoneRecursive() {
        AccessibilityNodeInfo parent = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGFRIENDLIST)));
        if (parent == null) return null;
//        if(parent.getText().toString().endsWith("打個招呼")){
//            return
//        }
        Stack<AccessibilityNodeInfo> cs=new Stack<AccessibilityNodeInfo>();
        for(int i=0;i<parent.getChildCount();i++){
            cs.push(parent.getChild(i));
        }
        while (cs.size()>0){
            AccessibilityNodeInfo cd=cs.pop();
            if(cd==null)continue;
            if(cd.getChildCount()>0){
                for(int i=0;i<cd.getChildCount();i++) cs.push(cd.getChild(i));
            }else if(cd.getText()!=null && cd.getText().toString().endsWith("打個招呼")){
                return cd;
            }
        }
        return null;
    }
    public void saveAcount() {
        AccessibilityNodeInfo parent = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGLIST)));
        String regex="^.*\\d{6,}.*$";//正则表达式
        Pattern p=Pattern.compile(regex);
//        Matcher m=p.matcher(s);
        if (parent == null) return;
//        if(parent.getText().toString().endsWith("打個招呼")){
//            return
//        }
        Stack<AccessibilityNodeInfo> cs=new Stack<AccessibilityNodeInfo>();
        for(int i=0;i<parent.getChildCount();i++){
            cs.push(parent.getChild(i));
        }
        while (cs.size()>0){
            AccessibilityNodeInfo cd=cs.pop();
            if(cd.getChildCount()>0){
                for(int i=0;i<cd.getChildCount();i++) cs.push(cd.getChild(i));
            }else if(cd.getText()!=null && !cd.getText().toString().equals(phoneNb)&&country_phones.containsValue(cd.getText().toString())&&!cd.getText().toString().endsWith("打個招呼")&&p.matcher(cd.getText().toString()).matches()){
                saveCount(cd.getText().toString()+"\n");
                cout++;
                Message msg=new Message();
                msg.what=COUNTTEXT;
                msg.obj=cout+"";
                myHandler.sendMessage(msg);
            }
        }
    }
    public String getCountry() {
        AccessibilityNodeInfo parent = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGLIST)));
//        Matcher m=p.matcher(s);
        if (parent == null) return null;
//        if(parent.getText().toString().endsWith("打個招呼")){
//            return
//        }
        Stack<AccessibilityNodeInfo> cs=new Stack<AccessibilityNodeInfo>();
        int mycountry=0;
        for(int i=0;i<parent.getChildCount();i++){
            cs.push(parent.getChild(i));
        }
        while (cs.size()>0){
            AccessibilityNodeInfo cd=cs.pop();
            if(cd.getChildCount()>0){
                for(int i=0;i<cd.getChildCount();i++) cs.push(cd.getChild(i));
            }else if(cd.getText()!=null){
                Set<String> countrys=country_phones.keySet();
                for(String country:countrys){
                    if(cd.getText().toString().toLowerCase().contains(country.toLowerCase())){
                        if(!country.equals(comeFrom)){
                            return country_phones.get(country);
                        }else{
                            if(mycountry==1){
                                return country_phones.get(country);
                            }else{
                                mycountry++;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public boolean getContact() {
        AccessibilityNodeInfo parent = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MSGLIST)));
//        Matcher m=p.matcher(s);
        if (parent == null) return false;
//        if(parent.getText().toString().endsWith("打個招呼")){
//            return
//        }
        Stack<AccessibilityNodeInfo> cs=new Stack<AccessibilityNodeInfo>();
//        int mycountry=0;
        for(int i=0;i<parent.getChildCount();i++){
            cs.push(parent.getChild(i));
        }
        while (cs.size()>0){
            AccessibilityNodeInfo cd=cs.pop();
            if(cd.getChildCount()>0){
                for(int i=0;i<cd.getChildCount();i++) cs.push(cd.getChild(i));
            }else if(cd.getText()!=null){
                String[] dialogA=Dialog_File.getInstance().DialogA.get(Dialog_File.getInstance().DialogA.size()-1).data;
                String[] dialogB=Dialog_File.getInstance().DialogB.get(Dialog_File.getInstance().DialogB.size()-1).data;
                for(int i=0;i<dialogA.length;i++){
                    if(cd.getText().toString().toLowerCase().equals(dialogA[i].toLowerCase())){
                        return true;
                    }
                }

                for(int i=0;i<dialogB.length;i++){
                    if(cd.getText().toString().toLowerCase().equals(dialogB[i].toLowerCase())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void saveCount(String count){
        File file = new File(Environment.getExternalStorageDirectory(), "acout.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            //光标移到原始文件最后，再执行写入
            raf.seek(file.length());
            raf.write(count.getBytes());
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            FileOutputStream outputStream = openFileOutput("acout.txt", MODE_PRIVATE);
//            outputStream.write(count.getBytes());
//            outputStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    private int getRandomTime(){
        return (int) (5000*Math.random());
    }
    private String getAData(int i){
        return Dialog_File.getInstance().DialogA.get(i).data[(int) (Dialog_File.getInstance().DialogA.get(i).data.length*Math.random())];
    }
    private int getATime(int i){
        return Dialog_File.getInstance().DialogA.get(i).time;
    }
    private String getBData(int i){
        return Dialog_File.getInstance().DialogB.get(i).data[(int) (Dialog_File.getInstance().DialogB.get(i).data.length*Math.random())];
    }
    private int getBTime(int i){
        return Dialog_File.getInstance().DialogB.get(i).time;
    }
    private String getCData(int i){
        return Dialog_File.getInstance().DialogC.get(i).data[(int) (Dialog_File.getInstance().DialogC.get(i).data.length*Math.random())];
    }
    private int getCTime(int i){
        return Dialog_File.getInstance().DialogC.get(i).time;
    }
    private Runnable chageAcout=new Runnable() {
        @Override
        public void run() {
            try {
//                HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//                Thread.sleep(500);
                Thread.sleep(1000);
                HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Thread.sleep(1000);
                AccessibilityNodeInfo me = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.MEPANAL)));
                HongBaoService.clickView(me);
//                if (me != null) {
//                }com.zhiliaoapp.musically:id/j4
                Thread.sleep(1000);
                AccessibilityNodeInfo acoutB = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.ACOUTTITLE)));
                HongBaoService.clickView(acoutB);
                Thread.sleep(1000);
                List<AccessibilityNodeInfo> acoutList = HongBaoService.mService.findAll(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.ACOUTLISTITEM)));
                initAcoutIndex(acoutList);
                HongBaoService.clickView(acoutList.get(acoutIndex).getParent());
                acoutIndex++;
                if(acoutIndex>2)acoutIndex=0;
                Thread.sleep(12000);
                isMSRun=false;
                doMesgRunnable();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void initAcoutIndex(List<AccessibilityNodeInfo> acoutList){
        if(isInitAcoutIndex){
            isInitAcoutIndex=false;
            AccessibilityNodeInfo checked = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.ACOUTLISCHECKED)));
            for(int i=0;i<acoutList.size();i++){
                if(checked.getParent().equals(acoutList.get(i).getParent())){
                    acoutIndex=i+1;
                    if(acoutIndex>2)acoutIndex=0;
                    return;
                }
            }
        }
    }
    private boolean isInitAcoutIndex=true;
    private int acoutIndex=1;
//    private void chageAcout(){
//        HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//    }
//    private boolean canSend=true;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void testWarnin() throws WarningException, InterruptedException {
//        if(true)return;
        Thread.sleep(3000);
        AccessibilityNodeInfo warnMsg = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.WARNINGIMG)));
        HongBaoService.clickView(warnMsg);
        Thread.sleep(500);
        AccessibilityNodeInfo sendAgin = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.SENDAGIN)));
        HongBaoService.clickView(sendAgin);
        Thread.sleep(2000);
        warnMsg = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.WARNINGIMG)));
        if(warnMsg!=null){
            AccessibilityNodeInfo warnMsg1 = HongBaoService.mService.findFirst(AbstractTF.newText("由於到方的隐私榷鮼定,ta可能焦法收到你的讯息", true));
            if(warnMsg1==null){
                warnMsg1 = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.WARNINGADVICE)));
                if(warnMsg1!=null) {
                    Rect rect = new Rect();
                    warnMsg1.getBoundsInScreen(rect);
                    AccessibilityNodeInfo warnRecive = null;
                    for (int i = rect.bottom; i > rect.top; i -= 50)
                        for (int j = rect.left; j < rect.right; j += 50) {
//                            warnRecive = HongBaoService.mService.findFirst(AbstractTF.newText("已提交意見反應", true));
//                            if (warnRecive != null) {
//                                throw (new WarningException("warn msg"));
//
//                            }
                            HongBaoService.mService.dispatchGestureClick(j, i);
                            Thread.sleep(500);

                        }
                }
                throw(new WarningException("warn msg"));
            }else{
                HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Thread.sleep(500);
                throw(new InterruptedException());
            }
        }
    }
    private void sendMsg(String msg, AccessibilityNodeInfo edit_msg) throws InterruptedException {
//        if(!canSend) return;
        //消息发送按钮
        Thread.sleep(1000);
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, msg);
        edit_msg.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        AccessibilityNodeInfo edit_button = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.EDITBUTTON)));
        if (edit_button == null) {
            Thread.sleep(2000);
            edit_button = HongBaoService.mService.findFirst(AbstractTF.newId(TikTokVersions.getInstance().get(TikTokVersions.EDITBUTTON)));
//            HongBaoService.mService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//            doMesgRunnable();
//            return;
        }
//        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, msg);
//        edit_msg.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        Thread.sleep(1000);
        HongBaoService.clickView(edit_button);
        Thread.sleep(1000);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!HongBaoService.isStart()) {
            try {
                this.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            } catch (Exception e) {
                this.startActivity(new Intent(Settings.ACTION_SETTINGS));
                e.printStackTrace();
            }
        }
    }
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();
    private static final int PERMISSION_REQUEST = 1;
    private void checkPermission() {
        mPermissionList.clear();
        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST:
//                break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                break;
//        }
    }
    private Map<String,String> country_phones=new HashMap<String,String>();
    private void setCountryPhone(String countryPhone){
        country_phones.clear();
        if(countryPhone ==null)return ;
        try {
            String[] countrys = countryPhone.split("-");
            for (int i = 0; i < countrys.length; i++) {
                String[] cps = countrys[i].split(":");
                String[] cs = cps[0].split("\\.");
                for (int j = 0; j < cs.length; j++) {
                    country_phones.put(cs[j], cps[1]);
                }
            }
        }catch (Exception e){
                e.printStackTrace();
        }
    }
}
