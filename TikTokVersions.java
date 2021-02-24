package com.wang.hongbaotest;

import java.util.HashMap;
import java.util.Map;

public class TikTokVersions {
    public static final String GZLIST="gz_list";//需要的关注列表
    public static final String MSGBUTTON="msg_button";//底部收件匣按钮
    public static final String MSGBUTTONIN="msg_button_in";//右上角未读消息按钮
    public static final String MSGFRIENDHAS="msg_friend_has";//消息列表未读消息数字text
    public static final String EDITMSG="edit_msg";//消息发送EditText
    public static final String EDITBUTTON="edit_button";//消息发送按钮
    public static final String MSGFRIENDLIST="msg_friend_list";//消息列表
    public static final String MSGLIST="msg_list";//消息列表

    //2020.12.01新增控件，主要是用来账号切换和警告处理，暂时只捕捉到17.5版本
    public static final String ACOUTTITLE="aout_title";//账户标题，用来弹出账号列表
    public static final String MEPANAL="me_panal";//我 界面，在这里切换账号
    public static final String WARNINGADVICE="warning_advice";//警告文字部分
    public static final String WARNINGIMG="warning_image";//发送文字边上的警告图标
    public static final String ACOUTLISTITEM="acout_list_item";//账号列表里的头像部分，用来点击item
    public static final String ACOUTLISCHECKED="acout_list_checked";//账号列表里的选中部分，用来识别从哪个账号开始
    public static final String SENDAGIN="send_agin";//账号列表里的选中部分，用来识别从哪个账号开始

    private static Map<String,Map> instance=new HashMap<String,Map>();
    static{
        Map<String,String> item175=new HashMap<String,String>();
        item175.put(GZLIST,"com.zhiliaoapp.musically:id/cos");
        item175.put(MSGBUTTON,"com.zhiliaoapp.musically:id/bv4");
        item175.put(MSGBUTTONIN,"com.zhiliaoapp.musically:id/dp8");
        item175.put(MSGFRIENDHAS,"com.zhiliaoapp.musically:id/ciq");
        item175.put(EDITMSG,"com.zhiliaoapp.musically:id/bz1");
        item175.put(EDITBUTTON,"com.zhiliaoapp.musically:id/ctj");
        item175.put(MSGFRIENDLIST,"com.zhiliaoapp.musically:id/cue");
        item175.put(MSGLIST,"com.zhiliaoapp.musically:id/ci3");

        item175.put(ACOUTTITLE,"com.zhiliaoapp.musically:id/title");
        item175.put(MEPANAL,"com.zhiliaoapp.musically:id/bv5");
        item175.put(WARNINGADVICE,"com.zhiliaoapp.musically:id/c45");
        item175.put(WARNINGIMG,"com.zhiliaoapp.musically:id/d2b");
        item175.put(ACOUTLISTITEM,"com.zhiliaoapp.musically:id/j4");
        item175.put(ACOUTLISCHECKED,"com.zhiliaoapp.musically:id/a6j");
        item175.put(SENDAGIN,"android:id/button1");
        instance.put("17.5",item175);

        Map<String,String> item176=new HashMap<String,String>();
        item176.put(GZLIST,"com.zhiliaoapp.musically:id/cka");
        item176.put(MSGBUTTON,"com.zhiliaoapp.musically:id/br5");
        item176.put(MSGBUTTONIN,"com.zhiliaoapp.musically:id/djl");
        item176.put(MSGFRIENDHAS,"com.zhiliaoapp.musically:id/cej");
        item176.put(EDITMSG,"com.zhiliaoapp.musically:id/bv_");
        item176.put(EDITBUTTON,"com.zhiliaoapp.musically:id/cp5");
        item176.put(MSGFRIENDLIST,"com.zhiliaoapp.musically:id/cpv");
        item176.put(MSGLIST,"com.zhiliaoapp.musically:id/cdw");
        instance.put("17.6",item176);
    }
    public static Map<String,String> getInstance(){
        return instance.get(MainActivity.version);
    }
}
