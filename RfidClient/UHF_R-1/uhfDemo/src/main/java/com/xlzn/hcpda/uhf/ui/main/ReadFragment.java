package com.xlzn.hcpda.uhf.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.xlzn.hcpda.uhf.MainActivity;
import com.xlzn.hcpda.uhf.R;
import com.xlzn.hcpda.uhf.UHFReader;
import com.xlzn.hcpda.uhf.Utils;
import com.xlzn.hcpda.uhf.entity.UHFReaderResult;
import com.xlzn.hcpda.uhf.entity.UHFTagEntity;
import com.xlzn.hcpda.uhf.interfaces.OnInventoryDataListener;
import com.xlzn.hcpda.uhf.viewmodel.RepoViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class ReadFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private TextView etDataRead;
    private Button btnRead;
    private Button btnBind;
    private TextView bdName;
    private TextView readKind;
    RepoViewModel repoViewModel ;
    Thread thread;
    private static final int LEVEL_NONE = 0;
    //Wifi信号等级（最弱）
    private static final int LEVEL_1 = 1;
    //Wifi信号等级
    private static final int LEVEL_2 = 2;
    //Wifi信号等级
    private static final int LEVEL_3 = 3;
    //Wifi信号等级（最强）
    private static final int LEVEL_4 = 4;
    String bdkey="";
    private boolean startFlag = true;
    private ImageView signal;
    Handler sigHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Log.d("hhhhhh:::", String.valueOf(msg.what));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        etDataRead = mainActivity.findViewById(R.id.read_etRead);
        btnBind = mainActivity.findViewById(R.id.read_btnbind);
        bdName = mainActivity.findViewById(R.id.read_bdName);
        signal = mainActivity.findViewById(R.id.read_Image);
        btnRead = mainActivity.findViewById(R.id.read_btnRead);
        readKind = mainActivity.findViewById(R.id.read_kindtext);
        repoViewModel = ViewModelProviders.of(mainActivity).get(RepoViewModel.class);
        btnRead.setOnClickListener(this);
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name11 = String.valueOf(btnBind.getText());
                if(name11.equals("开始检测")){
                    thread.start();
                    btnBind.setText("停止检测");
//                    Disposable disposable1 = repoViewModel.rfidDao().insertAll(rfid)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Consumer<List<Long>>() {
//                                @Override
//                                public void accept(List<Long> ids) throws Exception {
//                                    System.out.println("insert number = " + ids.size());
//                                    Toast.makeText(mainActivity, R.string.success, Toast.LENGTH_SHORT).show();
//                                    for (long id : ids) {
//                                        System.out.println("insert id = " + id);
//                                    }
//                                }
//                            }, new Consumer<Throwable>() {
//                                @Override
//                                public void accept(Throwable throwable) throws Exception {
//                                    System.out.println("insert error = " + throwable.getMessage());
//                                    throwable.printStackTrace();
//                                }
//                            });
//                    db.rfidDao().insertAll(rfid);
                }else{
                    thread.interrupt();
                    btnBind.setText("开始检测");
                }
            }
        });
        thread  = new Thread(new Runnable(){
            @Override
            public void run() {
                int old = LEVEL_NONE;
                while (true){
                    try {
                        Thread.sleep(100);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    if(Thread.currentThread().isInterrupted()){
                        //处理中断逻辑
                        Log.d("thread ","Interrupted");
                        break;
                    }
                    String ecp11 = String.valueOf(etDataRead.getText());
                    if (!ecp11.equals("")){
                        UHFReader.getInstance().setOnInventoryDataListener(new OnInventoryDataListener() {
                            @Override
                            public void onInventoryData(List<UHFTagEntity> tagEntityList) {
                                String ecp111 = String.valueOf(etDataRead.getText());
                                if (tagEntityList != null && tagEntityList.size() > 0) {
                                    for (UHFTagEntity  uhfTagEntity:  tagEntityList) {
                                        if (uhfTagEntity.getEcpHex().equals(ecp111)) {
                                            int rssi =LEVEL_1;
                                            int rss = uhfTagEntity.getRssi();
                                            if(rss >=-67&&rss < -55){
                                                rssi =LEVEL_3 ;
                                            }else if(rss >= -55 ){
                                                rssi = LEVEL_4;
                                            }else if(rss >=-100){
                                                rssi = LEVEL_2;
                                            }
                                            Message message = new Message();
                                            message.what = rssi;
                                            sigHandler.sendMessage(message);
                                            Utils.play();
                                            Log.d("send::", String.valueOf(rssi));
                                        }
                                    }
                                }
                            }
                        });
                    }
                    UHFReaderResult<Boolean> readerResult = UHFReader.getInstance().startInventory();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_btnRead:
                String password = "00000000";
                int address = 2;
                int wordCount = 6;
                int membank = 1;
                UHFReaderResult<String> readerResult = null;
                readerResult = UHFReader.getInstance().read(password, membank, address, wordCount, null);
                if (readerResult.getResultCode() != UHFReaderResult.ResultCode.CODE_SUCCESS) {
                    etDataRead.setText("");
                    Toast.makeText(mainActivity, R.string.fail, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mainActivity, R.string.success, Toast.LENGTH_SHORT).show();

                String ecp1 = readerResult.getData();
                etDataRead.setText(ecp1);
                Disposable disposable = repoViewModel.getlocalRepo(ecp1)
                        .subscribe(rf1-> {
                            System.out.println("query number = " + rf1.getName());
                            bdName.setText(rf1.getName());
                            readKind.setText(rf1.getKind());
                            bdkey= rf1.getName();
                        }, Throwable::printStackTrace);
                break;
        }
    }
}