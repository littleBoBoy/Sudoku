package com.example.sudoku.http;

import android.content.Context;
import android.widget.Toast;

import com.example.sudoku.R;

/**
 * @Author sunxin
 * @Description 错误处理
 */

public class ErrorHelper {
    public static void onError(Context context, Throwable throwable) {
        String errorString = throwable.toString();
        //返回的错误为空
        if (errorString == null) {
            Toast.makeText(context, context.getString(R.string.generic_server_down), Toast.LENGTH_SHORT).show();
        } else {
            //请求超时
            if (errorString.contains("TimeoutException") || errorString.contains("SocketTimeoutException")) {
                Toast.makeText(context, context.getString(R.string.request_time_out), Toast.LENGTH_SHORT).show();
            } else if (errorString.contains("SSLException")) {
                //能识别的请求异常，忽略不提示
            } else if (errorString.contains("ServiceConfigurationError") || errorString.contains("AuthenticatorException")) {
                //403、404等服务错误
                Toast.makeText(context, context.getString(R.string.generic_server_down), Toast.LENGTH_SHORT).show();
            } else if (errorString.contains("NetworkErrorException") || errorString.contains("NoConnectionPendingException") || errorString.contains("UnknownHostException")) {
                //网络未连接
                Toast.makeText(context, context.getString(R.string.network_hint), Toast.LENGTH_SHORT).show();
            } else if (errorString.contains("ConnectException")) {
                //连接不到服务器
                Toast.makeText(context, context.getString(R.string.fail_to_connected), Toast.LENGTH_SHORT).show();
            } else {

//                HttpException httpException=(HttpException)throwable ;
//                try {
//                    String es=httpException.response().errorBody().string();
//                    ErrorBean bean=new Gson().fromJson(es,ErrorBean.class);
//                    Toast.makeText(context,bean.getMsg(),Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }
}
