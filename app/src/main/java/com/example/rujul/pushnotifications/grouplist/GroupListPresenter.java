package com.example.rujul.pushnotifications.grouplist;

import android.util.Log;

import com.android.volley.VolleyError;
import com.example.rujul.pushnotifications.Constants;
import com.example.rujul.pushnotifications.database.DaoSession;
import com.example.rujul.pushnotifications.database.Group;
import com.example.rujul.pushnotifications.services.SharedPreferenceService;
import com.example.rujul.pushnotifications.services.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by rujul on 4/5/2016.
 */
public class GroupListPresenter implements VolleyService.VolleyInterface {

    GroupView view;
    String channelName;
    VolleyService volley;
    SharedPreferenceService spfs;
    DaoSession session;

    public GroupListPresenter(GroupView view,VolleyService volley,SharedPreferenceService spf,DaoSession session){
        this.view = view;
        this.volley = volley;
        this.spfs = spf;
        this.session = session;
    }

    public boolean onOkClicked(){
        channelName = view.getChannelName();
        Pattern pattern = Pattern.compile(Constants.ALPHA_NUM);
        if(!pattern.matcher(channelName).matches() || channelName.isEmpty()){
            view.showChannelNameError(Constants.ALPHA_NUM_ERROR);
            return false;
        }
        return true;
    }

    public void addOrJoinGroup(){
        Map<String,String> params = new HashMap<String,String>();
        params.put(Constants.GROUP_NAME,channelName);
        params.put(Constants.NAME,spfs.getStringData(Constants.ANAME));
        params.put(Constants.MOBILE,spfs.getStringData(Constants.AMOBILE));
        volley.makePostRequest(Constants.ADD_OR_JOIN_URL,params,this);
    }

    @Override
    public void onSuccess(JSONObject response) {
        try {
            JSONObject responseGroup = response.getJSONObject(Constants.DATA).getJSONObject(Constants.GROUP);
            Group group = new Group();
            group.setGroupId(responseGroup.getString(Constants.ID));
            group.setGroupName(responseGroup.getString(Constants.NAME));
            group.setTopicArn(responseGroup.getString(Constants.TOPIC_ARN));
            session.getGroupDao().insert(group);
            view.dataSetChanged(group);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error) {
        view.dataSetChanged(null);
        error.printStackTrace();
        Log.d("VOLLEY", new String(error.networkResponse.data));
    }

    public interface GroupView {
        public String getChannelName();
        public void showChannelNameError(String message);
        public void dataSetChanged(Group group);
    }

}
