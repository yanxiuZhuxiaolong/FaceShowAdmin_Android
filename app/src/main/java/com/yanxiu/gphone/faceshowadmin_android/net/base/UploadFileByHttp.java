package com.yanxiu.gphone.faceshowadmin_android.net.base;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by frc on 17-7-4.
 */

public class UploadFileByHttp {
    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";
    private static final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * @param params       传递的普通参数
     * @param uploadFile   需要上传的文件名
     * @param fileFormName 需要上传文件表单中的名字
     * @param newFileName  上传的文件名称，不填写将为uploadFile的名称
     * @param urlStr       上传的服务器的路径
     * @throws IOException
     */
    public void uploadForm(Map<String, String> params, String fileFormName,
                           final File uploadFile, String newFileName, String urlStr, final UpLoadFileByHttpCallBack callBack)
            throws IOException {
        if (newFileName == null || "".equals(newFileName.trim())) {
            newFileName = uploadFile.getName();
        }

        StringBuilder sb = new StringBuilder();
        /**
         * 普通的表单数据
         */
        for (String key : params.keySet()) {
            sb.append("--" + BOUNDARY + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + key + "\""
                    + "\r\n");
            sb.append("\r\n");
            sb.append(params.get(key) + "\r\n");
        }
        /**
         * 上传文件的头
         */
        sb.append("--" + BOUNDARY + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + fileFormName
                + "\"; filename=\"" + newFileName + "\"" + "\r\n");
        sb.append("Content-Type: application/octet-stream" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
        sb.append("\r\n");

        final byte[] headerInfo = sb.toString().getBytes("UTF-8");
        final byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
//        Log.e("requestBody::", sb.toString());
        final URL url = new URL(urlStr);
        final byte[][] responseBody = {null};//响应体
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + BOUNDARY);
//        conn.setRequestProperty("Content-Length", String
//                .valueOf(headerInfo.length + uploadFile.length()
//                        + endInfo.length));
                    conn.setDoOutput(true);

                    OutputStream out = conn.getOutputStream();
                    InputStream in = new FileInputStream(uploadFile);
                    out.write(headerInfo);

                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }

                    out.write(endInfo);
                    in.close();
                    out.close();
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        responseBody[0] = getBytesByInputStream(is);
                        final String response = getStringByBytes(responseBody[0]);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(response);
                            }
                        });
                    } else {
                        callBack.onFail(conn.getResponseMessage());
                    }
                } catch (Exception e) {
                    callBack.onFail(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //根据字节数组构建UTF-8字符串
    private String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    //从InputStream中读取数据，转换成byte数组，最后关闭InputStream
    private byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    private void runOnUiThread(Runnable task) {
        handler.post(task);
    }

    public interface UpLoadFileByHttpCallBack {
        void onSuccess(String responseStr);

        void onFail(String errorMessage);
    }

}
