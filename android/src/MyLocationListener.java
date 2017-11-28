package com.linkcld.cordova.bmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDAbstractLocationListener;

import org.apache.cordova.LOG;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 百度定位Listener
 *
 * @author jack
 */
public class MyLocationListener extends BDAbstractLocationListener {

    private CallbackContext callbackContext;

    private BDLocation currentLocation;

    private static final String LOG_TAG = MyLocationListener.class.getSimpleName();

    @Override
    public void onReceiveLocation(BDLocation location) {
        currentLocation = location;
        if (locationSuccess(location)) {
            try {
                JSONObject json = new JSONObject();

                json.put("time", location.getTime());
                json.put("locType", location.getLocType());
                json.put("locTypeDescription", location.getLocTypeDescription());
                json.put("latitude", location.getLatitude());
                json.put("longitude", location.getLongitude());
                if (location.hasRadius()) {
                    json.put("radius", location.getRadius());
                }
                json.put("countryCode", location.getCountryCode());
                json.put("country", location.getCountry());
                json.put("citycode", location.getCityCode());
                json.put("city", location.getCity());
                json.put("district", location.getDistrict());
                json.put("street", location.getStreet());
                json.put("addr", location.getAddrStr());
                json.put("province", location.getProvince());

                json.put("userIndoorState", location.getUserIndoorState());
                json.put("locationDescribe", location.getLocationDescribe());

                if (location.getLocType() == BDLocation.TypeGpsLocation) {

                    //当前为GPS定位结果，可获取以下信息
                    json.put("direction", location.getDirection());//获取方向信息，单位度
                    json.put("speed", location.getSpeed());//获取当前速度，单位：公里每小时
                    json.put("altitude", location.getAltitude()); //获取海拔高度信息，单位米

                }

                PluginResult pluginResult;
                if (location.getLocType() == BDLocation.TypeServerError
                        || location.getLocType() == BDLocation.TypeNetWorkException
                        || location.getLocType() == BDLocation.TypeCriteriaException) {

                    json.put("msg", "定位失败");
                    pluginResult = new PluginResult(PluginResult.Status.ERROR, json);
                    pluginResult.setKeepCallback(true);
                } else {
                    pluginResult = new PluginResult(PluginResult.Status.OK, json);
                    pluginResult.setKeepCallback(true);
                }

                callbackContext.sendPluginResult(pluginResult);
            } catch (JSONException e) {
                String errMsg = e.getMessage();
                LOG.e(LOG_TAG, errMsg, e);

                PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, errMsg);
                callbackContext.sendPluginResult(pluginResult);
            }
        }
    }

    private Boolean locationSuccess(BDLocation location) {
        switch (location.getLocType()) {
            case BDLocation.TypeCriteriaException:;
            case BDLocation.TypeNetWorkException:;
            case BDLocation.TypeNone:;
            case BDLocation.TypeOffLineLocationFail:;
            case BDLocation.TypeServerCheckKeyError:;
            case BDLocation.TypeServerDecryptError:;
            case BDLocation.TypeServerError:
                LOG.e(LOG_TAG, location.getLocTypeDescription());
                return false;
            default: return true;
        }

    }

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

}
