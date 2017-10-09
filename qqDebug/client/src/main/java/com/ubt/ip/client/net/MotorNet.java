package com.ubt.ip.client.net;

import com.ubt.ip.client.bean.MotorBean;
import com.ubt.ip.client.constants.ClientConstants;
import com.ubt.ip.client.listener.MotorListener;
import com.ubt.ip.client.service.MotorService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by afunx on 15/09/2017.
 */

public class MotorNet implements MotorService {

    private String mBaseUrl = "http://127.0.0.1";

    private final int mBasePort = 8266;

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl() + ":" + mBasePort)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public int getMotors(int[] motorsId, int[] motorsDegree) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        MotorProtocol motorProtocol = retrofit.create(MotorProtocol.class);
        Call<int[]> call = motorProtocol.getMotors(motorsId);
        try {
            Response<int[]> response = call.execute();
            for (int i = 0; i < motorsDegree.length; i++) {
                motorsDegree[i] = response.body()[i];
            }
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }

    @Override
    public int setMotor(MotorBean motorBean, MotorListener motorListener, int[] opId) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        MotorProtocol motorProtocol = retrofit.create(MotorProtocol.class);
        Call<Integer> call = motorProtocol.setMotor(motorBean);
        try {
            Response<Integer> response = call.execute();
            opId[0] = response.body();
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }

    @Override
    public int setMotors(List<MotorBean> motorBeanList, MotorListener motorListener, int[] opId) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        MotorProtocol motorProtocol = retrofit.create(MotorProtocol.class);
        Call<Integer> call = motorProtocol.setMotors(motorBeanList);
        try {
            Response<Integer> response = call.execute();
            opId[0] = response.body();
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }

    @Override
    public int cancelOperation(int opId, boolean[] isSuc) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        MotorProtocol motorProtocol = retrofit.create(MotorProtocol.class);
        Call<Boolean> call = motorProtocol.cancelOperation(opId);
        try {
            Response<Boolean> response = call.execute();
            isSuc[0] = response.body();
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }

    @Override
    public int enterReadMode(boolean[] isSuc) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        MotorProtocol motorProtocol = retrofit.create(MotorProtocol.class);
        Call<Boolean> call = motorProtocol.enterReadMode();
        try {
            Response<Boolean> response = call.execute();
            isSuc[0] = response.body();
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }

    @Override
    public int exitReadMode(boolean[] isSuc) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        MotorProtocol motorProtocol = retrofit.create(MotorProtocol.class);
        Call<Boolean> call = motorProtocol.exitReadMode();
        try {
            Response<Boolean> response = call.execute();
            isSuc[0] = response.body();
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }

    @Override
    public int checkReadMode(int[] readMode) {
        if (mBaseUrl == null) {
            return ClientConstants.ErrorCode.BASE_URL_NULL;
        }

        Retrofit retrofit = getRetrofit();

        MotorProtocol motorProtocol = retrofit.create(MotorProtocol.class);
        Call<Integer> call = motorProtocol.checkReadMode();
        try {
            Response<Integer> response = call.execute();
            readMode[0] = response.body();
            return ClientConstants.ErrorCode.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.IOEXCEPTION;
        } catch (Exception e) {
            e.printStackTrace();
            return ClientConstants.ErrorCode.UNKNOWN_ERR;
        }
    }
}
