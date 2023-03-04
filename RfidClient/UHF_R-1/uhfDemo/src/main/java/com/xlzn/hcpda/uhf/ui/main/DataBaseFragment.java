package com.xlzn.hcpda.uhf.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xlzn.hcpda.uhf.MainActivity;
import com.xlzn.hcpda.uhf.R;
import com.xlzn.hcpda.uhf.RFID;
import com.xlzn.hcpda.uhf.retrofit.Repo;
import com.xlzn.hcpda.uhf.viewmodel.RepoViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataBaseFragment extends Fragment {

    private MainActivity mainActivity = null;
    private RecyclerView recyclerview = null;
    private Button dataRefresh;
    private MyAdapter myAdapter;
    RepoViewModel repoViewModel ;
    private List<RFID> rfList= new ArrayList<>();
    private List<Repo> rpList= new ArrayList<>();
    public DataBaseFragment() {
        // Required empty public constructor
    }

    public static DataBaseFragment newInstance(String param1, String param2) {
        DataBaseFragment fragment = new DataBaseFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_data_base, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        repoViewModel = ViewModelProviders.of(mainActivity).get(RepoViewModel.class);
        dataRefresh = mainActivity.findViewById(R.id.data_refresh);
//        Disposable disposable = db.rfidDao().getAll()
//                .distinctUntilChanged()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<RFID>>() {
//                    @Override
//                    public void accept(List<RFID> rfids) throws Exception {
//                        rfList=rfids;
//                        myAdapter.notifyDataSetChanged();
//                        System.out.println("dataBase = " + rfids.size());
//                        for (RFID product : rfids) {
//                            System.out.println("query = " + product.rfidName);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        System.out.println("query error = " + throwable.getMessage());
//                        throwable.printStackTrace();
//                    }
//                });
//        rfList = rfidList ;
//        thread.start();
        recyclerview = mainActivity.findViewById(R.id.data_recyclerview);
        //设置LayoutManager，以LinearLayoutManager为例子进行线性布局
        recyclerview.setLayoutManager(new LinearLayoutManager(mainActivity));
        //设置分割线
        recyclerview.addItemDecoration(new DividerItemDecoration(mainActivity, LinearLayoutManager.VERTICAL));
        //创建适配器
//        myAdapter = new MyAdapter(rfList);
        myAdapter = new MyAdapter(rpList);
        //设置适配器
        recyclerview.setAdapter(myAdapter);
        fetchLocalData();
        dataRefresh.setOnClickListener(view -> {
            fetchData();
        });

    }

    private void fetchLocalData() {
        Disposable disposable1 =repoViewModel.getLocalRepos().subscribe(repos -> {
            rpList.clear();
            rpList.addAll(repos);
            myAdapter.notifyDataSetChanged();
            System.out.println("dataBase = " + repos.size());
            for (Repo product : repos) {
                System.out.println("query = " + product.getName());
            }
        }, Throwable::printStackTrace);
    }

    private void fetchData() {
        repoViewModel.getRepos().observe(mainActivity,repos -> {
            if (repos != null) {
                Disposable disposable3 = repoViewModel.getLocalRepos().subscribe(localrepos -> {
                    for (Repo local : localrepos) {
                        boolean isDelete = true;
                        for (Repo re : repos) {
                            if (local.getUid().equals(re.getUid())) {
                                isDelete = false;
                                break;
                            }
                        }
                        if (isDelete) {
                            Disposable disposable4 = repoViewModel.deleteLocalRepo(local).subscribe(ids -> {
                                Log.d("Inverntory", "删除本地数据");
                            }, Throwable::printStackTrace);
                        }
                    }
                }, Throwable::printStackTrace);
                for (Repo repo : repos) {
                    Disposable disposable = repoViewModel.getlocalRepo(repo.getUid()).subscribe(repo1 -> {
                        if (!repo.getName().equals(repo1.getName()) || !repo.getKind().equals(repo1.getKind())) {
                            repo1.setName(repo.getName());
                            repo1.setKind(repo.getKind());
                            Disposable disposable2 = repoViewModel.updateLocalRepo(repo1).subscribe(ids -> {
                                Log.d("Inverntory", "更新数据成功");
                            }, Throwable::printStackTrace);
                        }
                    }, throwable -> {
                        Log.d("find::", "error");
                        Disposable disposable1 = repoViewModel.insertRepo(repo).subscribe(ids -> {
                            System.out.println("insert id = " + ids);
                        }, throwable1 -> {
                            Log.d("find::", "insert error");
                        });
                    });

                }
                fetchLocalData();
            }
        });

    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//        private List<RFID> dataList;
        private List<Repo> dataList;
        MyAdapter(List<Repo> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
            Repo entity = dataList.get(position);
            holder.dataID.setText((position + 1) + "");
            holder.dataEPC.setText(entity.getUid());
            holder.dataName.setText(entity.getName());
            List<String> kindList = Arrays.asList("纸板", "纸箱", "印板", "刀模");
            holder.dataKind.setText(kindList.get(Integer.parseInt(entity.getKind())));
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private TextView dataID;
            private TextView dataEPC;
            private TextView dataName;
            private TextView dataKind;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                dataID = itemView.findViewById(R.id.data_ID);
                dataEPC = itemView.findViewById(R.id.data_EPC);
                dataName = itemView.findViewById(R.id.data_name);
                dataKind = itemView.findViewById(R.id.data_kind);
            }
        }
    }
}