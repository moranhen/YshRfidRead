package com.xlzn.hcpda.uhf.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xlzn.hcpda.uhf.AppDatabase;
import com.xlzn.hcpda.uhf.FindActivity;
import com.xlzn.hcpda.uhf.MainActivity;
import com.xlzn.hcpda.uhf.R;
import com.xlzn.hcpda.uhf.UHFReader;
import com.xlzn.hcpda.uhf.entity.SelectEntity;
import com.xlzn.hcpda.uhf.entity.UHFReaderResult;
import com.xlzn.hcpda.uhf.entity.UHFTagEntity;
import com.xlzn.hcpda.uhf.interfaces.OnInventoryDataListener;
import com.xlzn.hcpda.uhf.retrofit.Repo;
import com.xlzn.hcpda.uhf.viewmodel.RepoViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;


public class InventoryFragment extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity = null;
    static MainActivity ma = null;
    private RecyclerView recyclerview = null;
    private CheckBox cbSelectInventory;
    private Button btClear, btStartStop, btnSingle;
    private MyAdapter myAdapter;
    private int count = 0;
    private long startTime;
    AppDatabase db;
    RepoViewModel repoViewModel ;
    List<Repo> repoDe = new ArrayList<>();
    List<Repo> repoLocal = new ArrayList<>();
    List<Repo> reposLocal = new ArrayList<>();
    List<Repo> reposRemote = new ArrayList<>();
    private TextView tvNumber, tvTime, tvCount;
    private SelectEntity selectEntity = null;
    private List<UHFTagEntity> tagEntityList = new ArrayList<>();
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                UHFTagEntity uhfTagEntity = (UHFTagEntity) msg.obj;
                Repo re = null;
                boolean isLabel =false;
                for(Repo r :reposLocal){
                    System.out.println(uhfTagEntity.getEcpHex()+"dddd::"+r.getUid());
                    if(uhfTagEntity.getEcpHex().equals(r.getUid())){
                        re = r;
                        isLabel =true;
                        break;
                    }
                }
                count++;
                boolean isFlag = false;
                boolean isRepoFlag =false;

                if(isLabel){
                    for (UHFTagEntity entity : tagEntityList) {
                        if (entity.getEcpHex().equals(uhfTagEntity.getEcpHex())) {
                            entity.setCount(entity.getCount() + uhfTagEntity.getCount());
                            isFlag = true;
                            break;
                        }
                    }
                    for( Repo repo :repoDe){
                        if(repo.getUid().equals(uhfTagEntity.getEcpHex())){
                            isRepoFlag =true;
                            break;
                        }
                    }
                    if (!isFlag) {
                        tagEntityList.add(uhfTagEntity);
                    }
                    if(!isRepoFlag){
                        repoDe.add(re);
                        myAdapter.notifyDataSetChanged();
                    }
                }

                tvCount.setText(count + "");
                tvNumber.setText(tagEntityList.size() + "");
                myAdapter.notifyDataSetChanged();
            } else {
                handler.sendEmptyMessageDelayed(2, 100);
                long time = SystemClock.elapsedRealtime() - startTime;
                tvTime.setText(time / 1000 + "");
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        ma = (MainActivity) getActivity();

        cbSelectInventory = mainActivity.findViewById(R.id.cbSelectInventory);
        recyclerview = mainActivity.findViewById(R.id.recyclerview);
        btClear = mainActivity.findViewById(R.id.btClear);
        btStartStop = mainActivity.findViewById(R.id.btStartStop);
        btnSingle = mainActivity.findViewById(R.id.btnSingle);
        tvNumber = mainActivity.findViewById(R.id.tvNumber);
        tvTime = mainActivity.findViewById(R.id.tvTime);
        tvCount = mainActivity.findViewById(R.id.tvCount);
        repoViewModel = ViewModelProviders.of(mainActivity).get(RepoViewModel.class);
        //设置LayoutManager，以LinearLayoutManager为例子进行线性布局
        recyclerview.setLayoutManager(new LinearLayoutManager(mainActivity));
        //设置分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL));
        //创建适配器
        myAdapter = new MyAdapter(repoDe);
        //设置适配器
        recyclerview.setAdapter(myAdapter);

        btnSingle.setOnClickListener(this);
        btStartStop.setOnClickListener(this);
        btClear.setOnClickListener(this);
        cbSelectInventory.setOnClickListener(this);
        cbSelectInventory.setVisibility(View.GONE);

        mainActivity.setkeyDown(new MainActivity.KeyDown() {
            @Override
            public void onKeyDown(int keyCode) {
                startStop();
            }
        });
//        fetchData();



    }

    private void fetchData() {
        repoViewModel.getRepos().observe(mainActivity,repos -> {
            if (repos!=null){
                reposRemote.clear();
                reposRemote.addAll(repos);
                Disposable disposable3 = repoViewModel.getLocalRepos().subscribe(localrepos->{
                    repoLocal = localrepos;
                    for (Repo local : repoLocal){
                        boolean isDelete = true;
                        for (Repo re :repos){
                            if(local.getUid().equals(re.getUid())){
                                isDelete=false;
                                break;
                            }
                        }
                        if(isDelete){
                            Disposable disposable4 = repoViewModel.deleteLocalRepo(local).subscribe(ids->{
                                Log.d("Inverntory","删除本地数据");
                            }, Throwable::printStackTrace);
                        }
                    }
                },Throwable::printStackTrace);
                for(Repo repo : repos){
                    Disposable disposable = repoViewModel.getlocalRepo(repo.getUid()).subscribe(repo1->{
                        if(!repo.getName().equals(repo1.getName()) || !repo.getKind().equals(repo1.getKind())){
                            repo1.setName(repo.getName());
                            repo1.setKind(repo.getKind());
                            Disposable disposable2 = repoViewModel.updateLocalRepo(repo1).subscribe(ids->{
                                Log.d("Inverntory","更新数据成功");
                            }, Throwable::printStackTrace);
                        }
                    }, throwable -> {
                        Log.d("find::","error");
                        Disposable disposable1 = repoViewModel.insertRepo(repo).subscribe(ids->{
                            System.out.println("insert id = " + ids);
                        }, throwable1 -> {
                            Log.d("find::","insert error");
                        });
                    });

                }
                fetchLocal();
            }
        });

    }
    private void fetchLocal(){
        Disposable disposable3 = repoViewModel.getLocalRepos().subscribe(localrepos->{
            reposLocal.clear();
            reposLocal.addAll(localrepos);
        },Throwable::printStackTrace);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btClear:
                clear();
                break;
            case R.id.btStartStop:
                // UHFReader.getInstance().setInventoryModeForPower(InventoryModeForPower.POWER_SAVING_MODE);
                startStop();
                break;
            case R.id.cbSelectInventory:

                break;
            case R.id.btnSingle:
                UHFReaderResult<UHFTagEntity> uhfTagEntityUHFReaderResult = null;
                uhfTagEntityUHFReaderResult = UHFReader.getInstance().singleTagInventory();
                if (uhfTagEntityUHFReaderResult.getResultCode() != UHFReaderResult.ResultCode.CODE_SUCCESS) {
                    Toast.makeText(getActivity(), "单次盘点失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message message = new Message();
                message.what = 1;
                message.obj = uhfTagEntityUHFReaderResult.getData();
                handler.sendMessage(message);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        reposLocal.clear();
        btStartStop.setText(R.string.starts);
        UHFReader.getInstance().stopInventory();
        handler.removeMessages(2);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivity.setkeyDown(null);
    }
    private void startStop() {
        Log.e("TAG", "startStop: " + btStartStop.getText());
        Log.e("TAG", "startStop: " + getResources().getString(R.string.start));

        if (btStartStop.getText().equals(getResources().getString(R.string.start))) {
            fetchLocal();
            UHFReader.getInstance().setOnInventoryDataListener(new OnInventoryDataListener() {
                @Override
                public void onInventoryData(List<UHFTagEntity> tagEntityList) {
                    if (tagEntityList != null && tagEntityList.size() > 0) {
                        for (int k = 0; k < tagEntityList.size(); k++) {
                            if (!TextUtils.isEmpty(tagEntityList.get(k).getEcpHex())) {
                                Message message = new Message();
                                message.what = 1;
//                                message.obj = tagEntityList.get(k).getEcpHex();
                                message.obj = tagEntityList.get(k);
                                handler.sendMessage(message);
//                                Utils.play();
                            }
                        }
                    }
                }
            });

            UHFReaderResult<Boolean> readerResult = UHFReader.getInstance().startInventory();
            if (readerResult.getData()) {
                handler.sendEmptyMessageDelayed(2, 100);
                startTime = SystemClock.elapsedRealtime();
                btStartStop.setText(R.string.stop);
                btnSingle.setEnabled(false);
            } else {
                Toast.makeText(mainActivity, "盘点失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            btStartStop.setText(R.string.start);
            btnSingle.setEnabled(true);
            UHFReaderResult<Boolean> booleanUHFReaderResult = UHFReader.getInstance().stopInventory();
            if (booleanUHFReaderResult.getResultCode()==0) {
                SystemClock.sleep(200);
                for (int i = 0; i < tagEntityList.size(); i++) {
                    Log.e("TAG", "标签 "+tagEntityList.get(i).getEcpHex()+" 次数: " + tagEntityList.get(i).getCount() );
                }
            }

            handler.removeMessages(2);
        }
    }

    private void clear() {
        tagEntityList.clear();
        repoDe.clear();
        tvNumber.setText("0");
        tvTime.setText("0");
        tvCount.setText("0");
        startTime = SystemClock.elapsedRealtime();
        count = 0;
        myAdapter.notifyDataSetChanged();
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<UHFTagEntity> dataList;
        List<Repo> repos;

        private static final int LEVEL_NONE = 0;
        //Wifi信号等级（最弱）
        private static final int LEVEL_1 = 1;
        //Wifi信号等级
        private static final int LEVEL_2 = 2;
        //Wifi信号等级
        private static final int LEVEL_3 = 3;
        //Wifi信号等级（最强）
        private static final int LEVEL_4 = 4;
        MyAdapter( List<Repo> repos) {
//            this.dataList = dataList;
            this.repos =repos;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//            UHFTagEntity entity = dataList.get(position);
            Repo repo = repos.get(position);
            holder.tvID.setText((position + 1) + "");
            holder.tvEPC.setText(repo.getName());
            List<String> kindList = Arrays.asList("纸板", "纸箱", "印板", "刀模");
            holder.tvKind.setText(kindList.get(Integer.parseInt(repo.getKind())));
//            final String ecp = entity.getEcpHex();
//            Context context = ma.getApplicationContext();
//            db = AppDatabase.getAppInstance(context);
//            if(!ecp.equals("")){
//                Disposable disposable = db.rfidDao().loadAllById(ecp)
//                        .distinctUntilChanged()
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(rf1 -> {
//                            System.out.println("query number = " + rf1.rfidName);
//                            holder.tvEPC.setText(rf1.rfidName);
//                        }, throwable -> {
//                            System.out.println("query error = " + throwable.getMessage());
//                            throwable.printStackTrace();
//                        });
//            }
//            final String ecp1 = String.valueOf(holder.tvEPC.getText());
//            if (entity.getTidHex() != null && !ecp1.equals("")) {
//                holder.tvEPC.setText("EPC:" + entity.getEcpHex() + "\nTID:" + entity.getTidHex());
//            } else {
//                holder.tvEPC.setText(entity.getEcpHex());
//            }
//            int rssi =LEVEL_1;
//            int rss = entity.getRssi();
//            if(rss >=-67&&rss < -55){
//                rssi =LEVEL_3 ;
//            }else if(rss >= -55 ){
//                rssi = LEVEL_4;
//            }else if(rss >=-100){
//                rssi = LEVEL_2;
//            }
//            holder.tvRssi.setText(String.valueOf(rssi));

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(ma, FindActivity.class);
                String ecp11  =repo.getUid();;
                String name = repo.getName();
                String kind = repo.getKind();
                intent.putExtra("ecp",ecp11);
                intent.putExtra("name",name);
                intent.putExtra("kind",kind);
                ma.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return repos == null ? 0 : repos.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvID;
            private TextView tvEPC;
//            private TextView tvRssi;
            private TextView tvKind;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvID = itemView.findViewById(R.id.tvID);
                tvEPC = itemView.findViewById(R.id.tvEPC);
//                tvRssi = itemView.findViewById(R.id.tvRssi);
                tvKind = itemView.findViewById(R.id.tvKind);
            }
        }
    }

}