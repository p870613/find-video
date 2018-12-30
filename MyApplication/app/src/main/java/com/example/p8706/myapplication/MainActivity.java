package com.example.p8706.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getRootDirectory;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> files_name = new ArrayList<String>();
    ArrayList<String> total_path = new ArrayList<String>();
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> datas=new ArrayList<Map<String, Object>>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        /*權限取得寫*/
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        /*權限取得讀*/
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        /*get mp4 files*/
        listf(getExternalStorageDirectory().toString());
        /*put in the listview,datas, */
        for (int i = 0; i < files_name.size(); i++) {
            Map map = new HashMap();
            map.put("path",  total_path.get(i));
            map.put("mp4_name",files_name.get(i));
            datas.add(map);
        }

        simpleAdapter = new SimpleAdapter(this, datas, R.layout.path_name, new String[]{"mp4_name", "path"}, new int[]{R.id.path, R.id.mp4_name});
        /*print */
        listView.setAdapter(simpleAdapter);

        /*listview click*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(arg2);
                String path = map.get("path");
                String mp4_name = map.get("mp4_name");
                /*output a txt file ,where has the path of video you choose*/
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
                    try {
                        //取得SD卡路徑
                        File SDCardpath = Environment.getExternalStorageDirectory();
                        File myDataPath = new File(SDCardpath.getAbsolutePath() + "/final_project");
                        if (!myDataPath.exists())
                            myDataPath.mkdirs();
                        //將資料寫入到SD卡
                        FileWriter myFile = new FileWriter(SDCardpath.getAbsolutePath() + "/final_project/path.txt");
                        myFile.write(path);
                        myFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    /*recursive  ,  find the .mp4*/
    public void listf(String directoryName) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                if(check_filename(file.toString()) == true) {
                    files_name.add(get_filename(file.toString()));
                    total_path.add(file.toString());
                }
            } else if (file.isDirectory()) {
                listf(file.getAbsolutePath());
            }
        }
    }
    public boolean check_filename(String a)
    {
        String[] str = a.split("/");
        String s = str[str.length - 1];
        String[] sub_name = s.split("[.]");
        if(sub_name[sub_name.length -1].equals("mp4"))
        {
            return true;
        }
        return false;
    }
    public String get_filename(String a)
    {
        String[] str = a.split("/");
        return str[str.length - 1];
    }
}