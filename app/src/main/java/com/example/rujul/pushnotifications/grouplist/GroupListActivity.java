package com.example.rujul.pushnotifications.grouplist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.PushApp;
import com.example.rujul.pushnotifications.R;
import com.example.rujul.pushnotifications.chat.ChatActivity;
import com.example.rujul.pushnotifications.database.DaoSession;
import com.example.rujul.pushnotifications.database.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends AppCompatActivity implements GroupListAdapter.GroupListClicked,GroupListPresenter.GroupView{

    View channelDialog;
    EditText etAddChannel;
    AlertDialog dialog;
    FloatingActionButton fab;
    RecyclerView rvGroupList;
    DaoSession daoSession;
    GroupListAdapter groupAdapter;
    List<Group> groups;
    GroupListPresenter presenter;
    PushApp mApp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initActivity();
        createChannelDialog();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChannelDialog();
            }
        });
    }

    private void showChannelDialog() {
        etAddChannel.setText("");
        dialog.show();
    }

    private void startProgressBar(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating group");
        progressDialog.show();
    }
    private void initActivity() {
        mApp = PushApp.getApp();
        channelDialog =  LayoutInflater.from(this).inflate(R.layout.dialog_add_channel, null);
        etAddChannel = (EditText) channelDialog.findViewById(R.id.etChannelName);
        rvGroupList = (RecyclerView) findViewById(R.id.rvGroupList);
        daoSession= mApp.getDaoSession();

        fab = (FloatingActionButton) findViewById(R.id.fabAddOrJoin);
        groups = daoSession.getGroupDao().loadAll();

        if(groups.size()==0)
            groups = new ArrayList<Group>();

        groupAdapter = new GroupListAdapter(groups,this);
        rvGroupList.setLayoutManager(new LinearLayoutManager(this));
        rvGroupList.setAdapter(groupAdapter);

        presenter = new GroupListPresenter(this,mApp.getVolley(),mApp.getSPF(),daoSession);
    }


    private void createChannelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(channelDialog);

        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);
        builder.setTitle("Add/Join group");

        dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {
                Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (presenter.onOkClicked()) {
                            dialog.dismiss();
                            presenter.addOrJoinGroup();
                            startProgressBar();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onGroupItemClicked(int position) {
        Group grp = groups.get(position);
        Intent i = new Intent(GroupListActivity.this,ChatActivity.class);
        i.putExtra(Constants.ID, grp.getGroupId());
        startActivity(i);
    }

    @Override
    public String getChannelName() {
        return etAddChannel.getText().toString();
    }

    @Override
    public void showChannelNameError(String message) {
            etAddChannel.setError(message);
    }

    @Override
    public void dataSetChanged(Group group) {
        progressDialog.dismiss();
        if(group==null){
            PushApp.makeToast("There was some error on server");
            return;
        }
        groups.add(group);
        groupAdapter.notifyDataSetChanged();
    }
}
