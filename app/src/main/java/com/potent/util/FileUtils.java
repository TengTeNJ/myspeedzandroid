package com.potent.util;

import android.os.Environment;


import com.potent.common.event.DownLoadEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.greenrobot.event.EventBus;

public class FileUtils {
    public static String SDCardRoot;
    private boolean downLoadAble;

    public FileUtils() {
        // 得到当前外部存储设备的目录
        SDCardRoot = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + File.separator;
    }

    public static File creatFileInSDCard(String path)
            throws IOException {
        String[] dirs = path.split("\\\\");
        String dir = "";
        for (int i = 0; i < dirs.length - 1; i++) {
            dir = dir + dirs[i] + File.separator;
        }
        File dirfile = new File(SDCardRoot + dir + File.separator);
        dirfile.mkdirs();
        File file = new File(SDCardRoot + dir + dirs[dirs.length - 1]);
        System.out.println("creat file---->" + file);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws java.io.IOException
     */
    public File createFileInSDCard(String fileName, String dir)
            throws IOException {
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        System.out.println("file---->" + file);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param
     */
    public File creatSDDir(String dir) {
        File dirFile = new File(SDCardRoot + dir + File.separator);
        System.out.println(dirFile.mkdirs());
        return dirFile;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public boolean isFileExist(String fileName, String path) {
        File file = new File(SDCardRoot + path + File.separator + fileName);
        return file.exists();
    }

    public void deleteFileExist(String fileName, String path) {
        File file = new File(SDCardRoot + path + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public void stopDownLoad(DownLoadEvent event) {
        if (event.getActionType() == DownLoadEvent.mileageType.STOP) {
            downLoadAble = false;
        }
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public int write2SDFromInput(String path, String fileName,
                                 InputStream input) {
        EventBus.getDefault().register(this, "stopDownLoad");
        downLoadAble = true;
        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(path);
            file = createFileInSDCard(fileName, path);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[10 * 1024];
            int bytesize = 0;
            int temp;
            while ((temp = input.read(buffer)) != -1) {
                if (downLoadAble) {
                    output.write(buffer, 0, temp);
                    bytesize += temp;
                    EventBus.getDefault().post(new DownLoadEvent(bytesize, DownLoadEvent.mileageType.DOWNLOAD));
                } else {
                    EventBus.getDefault().unregister(this);
                    return 2;
                }
                //Thread.sleep(200);
                //callback.OnMilestone(bytesize);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
            EventBus.getDefault().unregister(this);
            return -1;
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
        return file == null ? -1 : 1;
    }

    public File write2SDFromInput(File file, InputStream input) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int temp;
            while ((temp = input.read(buffer)) != -1) {
                output.write(buffer, 0, temp);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public interface FileCallBack {
        public void OnMilestone(int bytesize);
    }

}