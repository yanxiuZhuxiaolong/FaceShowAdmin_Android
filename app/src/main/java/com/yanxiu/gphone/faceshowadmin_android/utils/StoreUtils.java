package com.yanxiu.gphone.faceshowadmin_android.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.yanxiu.gphone.faceshowadmin_android.FSAApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/5/19.
 */
public class StoreUtils {
    public static final String PATH = "yanxiu/storage/";
    public static final String DATAINFO = PATH + "relevantData";

    public static boolean isSdcardAvailable() {
        boolean exist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        return exist;
    }

    public static File getSdcardRootDirectory() {
        if (isSdcardAvailable()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /**
     * 获取手机存储文件位置
     *
     * @return
     */
    public static String getFilePath() {
        String filePath = null;
        if (getExternalSdCardPath() != null) {
            filePath = getExternalSdCardPath();
        } else {
            filePath = FSAApplication.getInstance().getFilesDir().getAbsolutePath();
        }
        return filePath;
    }

    /**
     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
     *
     * @return
     */
    public static ArrayList<String> getDevMountList() {
        String[] toSearch = FileUtils.readFile("/system/etc/vold.fstab").split(" ");
        ArrayList<String> out = new ArrayList<String>();
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i].contains("dev_mount")) {
                if (new File(toSearch[i + 2]).exists()) {
                    out.add(toSearch[i + 2]);
                }
            }
        }
        return out;
    }

    /**
     * 获取扩展SD卡存储目录
     * <p/>
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     *
     * @return
     */
    public static String getExternalSdCardPath() {

        if (isSdcardAvailable()) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }

        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }

        return null;
    }
    /**
     * 保存相关数据到文件
     *
     * @param data
     * @param file
     */
//    private static void writeTxtData(Context context, final String data, final File file) {
//        new YanxiuSimpleAsyncTask<Void>(context) {
//            @Override
//            public Void doInBackground() {
//                OutputStreamWriter out = null;
//                try {
//                    out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
//                    out.write(data);
//                    out.flush();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (out != null) {
//                            out.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//            @Override
//            public void onPostExecute(Void result) {
//
//            }
//        }.start();
//    }
///**
//     * 保存相关数据到文件
//     *
//     * @param data
//     * @param file
//     */
//    private static void writeRelevantData(Context context, final String data, final File file) {
//        new YanxiuSimpleAsyncTask<Void>(context) {
//            @Override
//            public Void doInBackground() {
//                ObjectOutputStream out = null;
//                try {
//                    out = new ObjectOutputStream(new FileOutputStream(file));
//                    out.writeObject(data);
//                    out.flush();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (out != null) {
//                            out.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return null;
//            }
//            @Override
//            public void onPostExecute(Void result) {
//
//            }
//        }.start();
//    }

    /**
     * 从文件中读取数据
     *
     * @param file
     * @return
     */
    private static String readRelevantData(File file) {
        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(new FileInputStream(file));
            return (String) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static File getRelevantFile(Context context, String filename) {
        if (!isSdcardAvailable()) {
            return null;
        }

        try {
            File dir = new File(Environment.getExternalStorageDirectory(), DATAINFO);

            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            File file = new File(dir, filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String saveRelevantData(Context context, String filename, String data) {
        File file = getRelevantFile(context, filename);

        if (file != null && !TextUtils.isEmpty(data)) {
            writeRelevantData(context, data, file);
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    public static String saveTxtData(Context context, String filename, String data) {
        File file = getRelevantFile(context, filename);

        if (file != null && !TextUtils.isEmpty(data)) {
            writeTxtData(context, data, file);
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 保存相关数据到文件
     *
     * @param data
     * @param file
     */
    private static void writeTxtData(Context context, final String data, final File file) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                OutputStreamWriter out = null;
                try {
                    out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                    out.write(data);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;

            }
        }.execute();

    }

    /**
     * 保存相关数据到文件
     *
     * @param data
     * @param file
     */
    private static void writeRelevantData(Context context, final String data, final File file) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(new FileOutputStream(file));
                    out.writeObject(data);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();

    }

    public static String getRelevantData(Context context, String filename) {
        File file = getRelevantFile(context, filename);

        if (file != null) {
            return readRelevantData(file);
        }
        return null;
    }

    /**
     * 获取制定路径的剩余空间 MB
     * @param path
     * @return
     */
    public static int getFreeMemory(String path) {
        int FreeMemory = 0;
        if (!StringUtils.isEmpty(path)) {
            File file = null;
            try {
                file = new File(path);
                if (file.exists()) {
                    long memory = file.getFreeSpace();
                    FreeMemory = (int) (memory / 1024 / 0124);
                }
            } catch (Exception e) {
                return FreeMemory;
            }
        } else {
            return FreeMemory;
        }
        return FreeMemory;
    }
}
