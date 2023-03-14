package cn.armory.explore.activity;

import androidx.lifecycle.Observer;

import cn.armory.common.base.Result;
import cn.armory.common.base.mvvm.DataBindingConfig;
import cn.armory.common.base.mvvm.VmBaseActivity;
import cn.armory.explore.R;
import cn.armory.explore.bean.TextBean;
import cn.armory.explore.databinding.ActivityMainVmBinding;
import cn.armory.explore.viewModel.VmMainViewModel;

public class VmMainActivity extends VmBaseActivity<ActivityMainVmBinding, VmMainViewModel> {
    @Override
    protected void initLocalData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_vm;
    }


    @Override
    protected void initView() {
        binding.text.setText("123");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
       // viewModel.getList();
    }

    @Override
    protected void initLiveDataObserver() {
        viewModel.getListData().observe(this, new Observer<Result<TextBean>>() {
            @Override
            public void onChanged(Result<TextBean> textBeanResult) {

            }
        });
    }

    @Override
    protected DataBindingConfig getDataBindingConfig() {
        return null;
    }
}
