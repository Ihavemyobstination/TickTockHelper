package com.wang.hongbaotest;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dialog_File {
    public List<DataTime> DialogA;
    public List<DataTime> DialogB;
    public List<DataTime> DialogC;
    public static Dialog_File getInstance(){return instance;}
    private static Dialog_File instance=new Dialog_File();
    private Dialog_File(){
        File file = new File(Environment.getExternalStorageDirectory(), "talk_setting.txt");
        DialogA=new ArrayList<DataTime>();
        DialogB=new ArrayList<DataTime>();
        DialogC=new ArrayList<DataTime>();
        if (!file.exists()) {
            DialogA.add(new DataTime("hi"));

            DialogB.add(new DataTime("Hi!<+>10"));
            DialogB.add(new DataTime("Nice to meet you<+>2"));
            DialogB.add(new DataTime("I'm from <+>18"));
            DialogB.add(new DataTime("What country are you from"));

            DialogC.add(new DataTime("Hey, what are you doing<+>26"));
            DialogC.add(new DataTime("My name is "));
            DialogC.add(new DataTime("I will be busy with other things later, and I don't often use Tiktok. Or you can add my personal contact information, you can send a message to me, I will give you a message after I see, I hope to make a friend with you!<+>5"));
            DialogC.add(new DataTime("My whatsapp<+>5"));
        }else{
            try {
                BufferedReader reader=new BufferedReader(new FileReader(file));
                initBlock(DialogA,reader.readLine());
                initBlock(DialogB,reader.readLine());
                initBlock(DialogC,reader.readLine());
                System.out.println("DialogA is :"+DialogA.toString());
                System.out.println("DialogB is :"+DialogB.toString());
                System.out.println("DialogC is :"+DialogC.toString());
                System.out.println();
            } catch (FileNotFoundException e) {
                DialogA=null;
                DialogB=null;
                DialogC=null;
                e.printStackTrace();
            } catch (IOException e) {
                DialogA=null;
                DialogB=null;
                DialogC=null;
                e.printStackTrace();
            }
        }
    }

    private void initBlock(List<DataTime> dialog,String line){
//        System.out.println("line is :"+line);
        String[] ls=line.split("<\\|>");
        for(int i=0;i<ls.length;i++){
            dialog.add(new DataTime(ls[i]));
        }
    }
    public class DataTime{
        public String[] data;
        public int time;
        public DataTime(String item){
            String[] ls=item.split("<\\+>");
            data=ls[0].split("<\\*>");
            if(ls.length>1){
                time=1000*Integer.parseInt(ls[1]);
            }
        }
        @Override
        public String toString(){
            return "("+data+","+time+")";
        }
    }
}
