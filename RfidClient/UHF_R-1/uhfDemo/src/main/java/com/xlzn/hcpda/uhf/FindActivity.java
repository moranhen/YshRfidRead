package com.xlzn.hcpda.uhf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.xlzn.hcpda.uhf.entity.UHFReaderResult;
import com.xlzn.hcpda.uhf.entity.UHFTagEntity;
import com.xlzn.hcpda.uhf.enums.InventoryModeForPower;
import com.xlzn.hcpda.uhf.enums.UHFSession;
import com.xlzn.hcpda.uhf.interfaces.OnInventoryDataListener;
import com.xlzn.hcpda.uhf.module.UHFReaderSLR;
import com.xlzn.hcpda.uhf.retrofit.Repo;
import com.xlzn.hcpda.uhf.viewmodel.RepoViewModel;

import java.util.List;

public class FindActivity extends AppCompatActivity {
    private final String TAG = "TAG";
    private static final int LEVEL_NONE = 0;
    //Wifi信号等级（最弱）
    private static final int LEVEL_1 = 1;
    //Wifi信号等级
    private static final int LEVEL_2 = 2;
    //Wifi信号等级
    private static final int LEVEL_3 = 3;
    //Wifi信号等级（最强）
    private static final int LEVEL_4 = 4;
//    RFID rfid;
    Repo rfid;
    String ecp ="";
    String name = "";
    String kind ="";
    private TextView  findEtRead;
    private Button findBtnBind;
    private ImageView back;
    private Spinner spinner;
    Thread thread;
    private TextView findEtName;
//    AppDatabase db;
    RepoViewModel repoViewModel ;
    private final boolean startFlag = true;
    private ImageView signal;
    Handler findHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("find:::", String.valueOf(msg.what));
            if(startFlag){
                switch (msg.what) {
                    case LEVEL_1:
                        signal.setImageResource(R.drawable.sig1);
                        break;
                    case LEVEL_2:
                        signal.setImageResource(R.drawable.sig2);
                        break;
                    case LEVEL_3:
                        signal.setImageResource(R.drawable.sig3);
                        break;
                    case LEVEL_4:
                        signal.setImageResource(R.drawable.sig4);
                        break;
                    case LEVEL_NONE:
                    default:
                        signal.setImageResource(R.drawable.sig0);
                        break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        repoViewModel = ViewModelProviders.of(FindActivity.this).get(RepoViewModel.class);
        findEtRead = findViewById(R.id.find_etRead);
        findBtnBind =    findViewById(R.id.find_btnbind);
        findEtName =     findViewById(R.id.find_bdName);
        signal =     findViewById(R.id.find_Image);
        spinner = findViewById(R.id.find_sp1);
        back =findViewById(R.id.find_back);
//        Utils.loadSoundPool(this);
//        LoggerUtils.d(TAG, "demo 启动");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                thread.interrupt();
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        //获取上一个活动传递来的数据（键值对）
        ecp = intent.getStringExtra("ecp");
        name = intent.getStringExtra("name");
        kind = intent.getStringExtra("kind");
        spinner.setSelection(Integer.parseInt(kind));
        findEtRead.setText(ecp);
        findEtName.setText(name);

//        Disposable disposable1 = repoViewModel.getlocalRepo(ecp).subscribe(rf1 ->{
//            System.out.println("query number = " + rf1.getName());
//            rfid = rf1;
//        },throwable -> {
//            System.out.println("query error = " + throwable.getMessage());
//            throwable.printStackTrace();
//        });
//        thread= new Thread(new Runnable(){
//            @Override
//            public void run() {
//                int old = LEVEL_NONE;
////                while (true){
//                try {
//                    Thread.sleep(200);
//                    startDete();
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//                    if(Thread.currentThread().isInterrupted()){
//                        //处理中断逻辑
//                        Log.d("thread ","Interrupted");
//                        break;
//                    }
//
//                }
//            }
//        });
//        thread.start();

        findBtnBind.setOnClickListener(view -> {
            if (findBtnBind.getText().toString() .equals("停止检测")){
                UHFReaderResult<Boolean> booleanUHFReaderResult = UHFReader.getInstance().stopInventory();
                findBtnBind.setText("开始检测");
//                close();
//                thread.interrupt();
            }else {
                findBtnBind.setText("停止检测");
                startDete();
//                new OpenTask().execute();
//                thread.start();
            }

        });
    }
    public void startDete(){
        UHFReader.getInstance().setOnInventoryDataListener(new OnInventoryDataListener() {
            @Override
            public void onInventoryData(List<UHFTagEntity> tagEntityList) {
                String ecp111 = String.valueOf(findEtRead.getText());
                if (tagEntityList != null && tagEntityList.size() > 0) {
                    for (UHFTagEntity  uhfTagEntity:  tagEntityList) {
                        if (uhfTagEntity.getEcpHex().equals(ecp111)) {
                            int rssi =LEVEL_1;
                            int rss = uhfTagEntity.getRssi();
                            Log.d("find::",String.valueOf(rss));
                            if(rss >=-67&&rss < -55){
                                rssi =LEVEL_3 ;
                            }else if(rss >= -55 ){
                                rssi = LEVEL_4;
                            }else if(rss >=-100){
                                rssi = LEVEL_2;
                            }
                            Message message = new Message();
                            message.what = rssi;
                            findHandler.sendMessage(message);
//                                            Utils.play();
                            Log.d("send::", String.valueOf(rssi));
                        }
                    }
                }
            }
        });
        UHFReaderResult<Boolean> readerResult = UHFReader.getInstance().startInventory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Utils.releaseSoundPool();
//        close();
        Log.e(TAG, "Find,onDestroy: ");
    }
    @Override
    protected void onResume() {
        super.onResume();
//        requestPermission();

//        HcPowerCtrl ctrl = new HcPowerCtrl();
//        ctrl.identityPower(1);
        Log.e(TAG, "Find,onResume: ");
        new OpenTask().execute();
//        startDete();

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Find,onPause: ");
        close();

    }
    private void close() {
        Log.e(TAG, "Find,close ");
//        HcPowerCtrl ctrl = new HcPowerCtrl();
//        ctrl.identityPower(0);
        UHFReader.getInstance().disConnect();

    }
    public class OpenTask extends AsyncTask<String, Integer, UHFReaderResult> {
        ProgressDialog progressDialog;

        @Override
        protected UHFReaderResult doInBackground(String... params) {
            return UHFReader.getInstance().connect(FindActivity.this);
        }

        @Override
        protected void onPostExecute(UHFReaderResult result) {
            super.onPostExecute(result);
            if (result.getResultCode() == UHFReaderResult.ResultCode.CODE_SUCCESS) {
                UHFReader.getInstance().setSession(UHFSession.S1);
                if (UHFReaderSLR.getInstance().is5300) {
                    Log.e(TAG, "onPostExecute: 省电模式");
                    UHFReader.getInstance().setInventoryModeForPower(InventoryModeForPower.POWER_SAVING_MODE);
                }
                UHFReader.getInstance().setPower(30);
                Toast.makeText(FindActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FindActivity.this, R.string.fail, Toast.LENGTH_SHORT).show();
            }
            progressDialog.cancel();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(FindActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.start_connect));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }
}
//                    Disposable disposable1 = db.rfidDao().insertAll(rfid)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(ids -> {
//                                System.out.println("insert number = " + ids.size());
//                                Toast.makeText(FindActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
//                                for (long id : ids) {
//                                    System.out.println("insert id = " + id);
//                                }
//                            }, new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    System.out.println("insert error = " + throwable.getMessage());
//                                    throwable.printStackTrace();
//                                }
//                            });
//                    db.rfidDao().insertAll(rfid);