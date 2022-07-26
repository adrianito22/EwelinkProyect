package com.tiburela.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.github.realzimboguy.ewelink.api.EweLink;
import com.github.realzimboguy.ewelink.api.model.devices.DeviceItem;
import com.github.realzimboguy.ewelink.api.model.devices.Devices;
import com.github.realzimboguy.ewelink.api.wss.WssResponse;
import com.github.realzimboguy.ewelink.api.wss.wssrsp.WssRspMsg;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginewelink();


    }


    private void loginewelink(){


        EweLink eweLink = new EweLink("us", Credenciales.correo,Credenciales.pass,60);





        try {
            eweLink.login();

            Devices getDevices = eweLink.getDevices();



         //   Log.i("dispositivos","devices is "+dev);


            for (DeviceItem devicelist : getDevices.getDevicelist()) {

              //  Log.i("dispositivos","devices is "+devicelist.getDeviceid());
              //  Log.i("dispositivos","devices is "+devicelist.getName());

               // System.out.println(devicelist.getDeviceid());
               // System.out.println(devicelist.getName());

                eweLink.getDeviceStatus(devicelist.getDeviceid());

            }

          //  System.out.println(eweLink.getDevice("10009ce53b"));

          //  System.out.println(eweLink.setDeviceStatusByName("Pool Tank","off"));

            eweLink.getWebSocket(new WssResponse() {

                @Override
                public void onMessage(String s) {
                    Log.i("dispositivos","string mensaje "+s);

                    //if you want the raw json data
                  //  System.out.println("on message in test raw:" + s);

                }

                @Override
                public void onMessageParsed(WssRspMsg rsp) {

                    if (rsp.getError() == null) {

                        //normal scenario
                        StringBuilder sb = new StringBuilder();
                        sb.append("Device:").append(rsp.getDeviceid()).append(" - ");
                        if (rsp.getParams() != null) {
                            sb.append("Switch:").append(rsp.getParams().getSwitch()).append(" - ");
                            sb.append("Voltage:").append(rsp.getParams().getVoltage()).append(" - ");
                            sb.append("Power:").append(rsp.getParams().getPower()).append(" - ");
                            sb.append("Current:").append(rsp.getParams().getCurrent()).append(" - ");
                        }


                        Log.i("dispositivos","string mensaje "+sb.toString());

                      //  System.out.println(sb.toString());

                    } else if (rsp.getError() == 0) {
                        //this is from a login response

                       // System.out.println("login success");
                    } else if (rsp.getError() > 0) {
                      //  System.out.println("login error:" + rsp.toString());

                        Log.i("dispositivos","error "+rsp.toString());

                    }
                }

                @Override
                public void onError(String error) {
                 //   System.out.println("onError in test, this should never be called");
                    //System.out.println(error);


                    Log.i("dispositivos","error es "+ error);


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }










    }
}