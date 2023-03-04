package com.xlzn.hcpda.uhf.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xlzn.hcpda.uhf.MainActivity;
import com.xlzn.hcpda.uhf.R;
import com.xlzn.hcpda.uhf.UHFReader;
import com.xlzn.hcpda.uhf.entity.UHFReaderResult;
import com.xlzn.hcpda.uhf.enums.UHFSession;


public class ConfigFragment extends Fragment implements View.OnClickListener {

    private MainActivity  mainActivity;
    private Button btnGetPower,btnSetPower,btnGetFrequencyBand,btnSetFrequencyBand,btnSetSession,btnGetSession;
    private Spinner spFrequencyBand,spPower,spSession;
    private CheckBox cbTID,cbEPC;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_config, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity=(MainActivity)getActivity();

        btnGetPower=mainActivity.findViewById(R.id.btnGetPower);
        btnSetPower=mainActivity.findViewById(R.id.btnSetPower);
        btnGetFrequencyBand=mainActivity.findViewById(R.id.btnGetFrequencyBand);
        btnSetFrequencyBand=mainActivity.findViewById(R.id.btnSetFrequencyBand);
        btnSetSession=mainActivity.findViewById(R.id.btnSetSession);
        btnGetSession=mainActivity.findViewById(R.id.btnGetSession);
        spFrequencyBand=mainActivity.findViewById(R.id.spFrequencyBand);
        spPower=mainActivity.findViewById(R.id.spPower);
        spSession=mainActivity.findViewById(R.id.spSession);
        cbTID=mainActivity.findViewById(R.id.cbTID);
        cbEPC=mainActivity.findViewById(R.id.cbEPC);
        btnGetPower.setOnClickListener(this);
        btnSetPower.setOnClickListener(this);
        btnGetFrequencyBand.setOnClickListener(this);
        btnSetFrequencyBand.setOnClickListener(this);
        btnSetSession.setOnClickListener(this);
        btnGetSession.setOnClickListener(this);
        cbTID.setOnClickListener(this);
        cbEPC.setOnClickListener(this);
        ArrayAdapter  adapter = ArrayAdapter.createFromResource(mainActivity, R.array.arrPower, android.R.layout.simple_spinner_item);
        spPower.setAdapter(adapter);
        spPower.setSelection(30-5);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cbEPC:
                if (cbEPC.isChecked()) {
                    cbTID.setChecked(false);
                    UHFReaderResult<Boolean> result = UHFReader.getInstance().setInventoryTid(false);
                    if (result.getData()) {
                        Toast.makeText(mainActivity, R.string.success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mainActivity, R.string.fail, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.cbTID:
                if(cbTID.isChecked()) {
                    cbEPC.setChecked(false);
                    UHFReaderResult<Boolean>  result = UHFReader.getInstance().setInventoryTid(true);
                    if (result.getData()) {
                        Toast.makeText(mainActivity, R.string.success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mainActivity, R.string.fail, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnGetFrequencyBand:
                boolean reuslt=getFrequencyBand();
                if(reuslt){
                    Toast.makeText(mainActivity,R.string.success,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mainActivity,R.string.fail,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSetFrequencyBand:
                setFrequencyBand();
                break;
            case R.id.btnGetPower:
                getPower();
                break;
            case R.id.btnSetPower:
                setPower();
                break;
            case R.id.btnGetSession:
                 reuslt=getSession();
                if(reuslt){
                    Toast.makeText(mainActivity,R.string.success,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mainActivity,R.string.fail,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSetSession:
                setSession();
                break;
        }

    }

    private boolean getFrequencyBand(){
        //    北美（902-928）	0x01
        //    中国1（920-925）	0x06
        //    欧频（865-867）	0x08
        //    中国2（840-845）	0x0a
        //    全频段（840-960）	0xff
        UHFReaderResult<Integer> result= UHFReader.getInstance().getFrequencyRegion();
        if(result.getResultCode() == UHFReaderResult.ResultCode.CODE_SUCCESS){
             switch (result.getData()){
                 case 0x01:
                     spFrequencyBand.setSelection(0);
                     break;
                 case 0x06:
                     spFrequencyBand.setSelection(1);
                     break;
                 case 0x08:
                     spFrequencyBand.setSelection(2);
                     break;
                 case 0x0a:
                     spFrequencyBand.setSelection(3);
                     break;
                 case 0xff:
                     spFrequencyBand.setSelection(4);
                     break;
             }
            return true;
        }
        return false;
    }
    private void setFrequencyBand(){
        int value=1;
        switch (spFrequencyBand.getSelectedItemPosition()){
            case 0:
                value=0x01;
                break;
            case 1:
                value=0x06;
                break;
            case 2:
                value=0x08;
                break;
            case 3:
                value=0x0a;
                break;
            case 4:
                value=0xff;
                break;
        }
        UHFReaderResult<Boolean> result= UHFReader.getInstance().setFrequencyRegion(value);
        if(result.getResultCode() == UHFReaderResult.ResultCode.CODE_SUCCESS){
            Toast.makeText(mainActivity,R.string.success,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mainActivity,R.string.fail,Toast.LENGTH_SHORT).show();
        }
    }

    private void setPower(){
        UHFReaderResult<Boolean> result= UHFReader.getInstance().setPower(spPower.getSelectedItemPosition()+5);
        if(result.getResultCode() == UHFReaderResult.ResultCode.CODE_SUCCESS){
            Toast.makeText(mainActivity,R.string.success,Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mainActivity, R.string.fail, Toast.LENGTH_SHORT).show();
        }
    }
    private boolean getPower(){
        UHFReaderResult<Integer> result= UHFReader.getInstance().getPower();
        if(result.getResultCode() == UHFReaderResult.ResultCode.CODE_SUCCESS){
            spPower.setSelection(result.getData()-5);
            return true;
        }
        return false;
    }
    private boolean getSession(){
        UHFReaderResult<UHFSession> result= UHFReader.getInstance().getSession();
        if(result.getResultCode() == UHFReaderResult.ResultCode.CODE_SUCCESS){
             spSession.setSelection(result.getData().getValue());
            return true;
        }
        return false;
    }
    private void setSession(){
        UHFReaderResult<Boolean> result= UHFReader.getInstance().setSession(UHFSession.getValue(spSession.getSelectedItemPosition()));
        if(result.getResultCode() == UHFReaderResult.ResultCode.CODE_SUCCESS){
            Toast.makeText(mainActivity,R.string.success,Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mainActivity, R.string.fail, Toast.LENGTH_SHORT).show();
        }
    }
}