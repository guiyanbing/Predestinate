package com.juxin.predestinate.module.logic.socket;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.utils.JniUtil;
import com.juxin.predestinate.module.util.ByteUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * socket消息发送结构体
 *
 * 长连接读取数据时以以下方式读取
 * |---           数据头       10 byte               ___|
 * |---    4 byte  ---| |---4 byte ---| |--- 2byte   ---|
 * |---   数据长度 ---| |---用户uid---| |--- 消息类型---|
 *
 * |---               数据体  N byte                 ---|
 * |---             数据长度个字节的数据体           ---|
 *
 * .... 重复以上....
 */
public class NetData implements Parcelable {
    /**
     * 数据头信息 数据长度 字节长度
     * 代表数据头信息后发送的数据体的大小
     */
    public final static int READ_DATA_HEAD_LENGTH_SIZE = 4;

    /**
     * 数据头信息 用户uid 字节长度
     */
    public final static int READ_DATA_HEAD_UID_SIZE = 4;
    /**
     * 数据头信息 数据类型msg type 字节长度
     */
    public final static int READ_DATA_HEAD_MSG_TYPE_SIZE = 2;


    private int length = 0;     // 4个字节存储消息体的长度
    private long uid = 0;       // 中4个字节存储用于识别客户端的ID，是一个无符号整数
    private int msgType;        // 最后2字节消息类型，是一个无符号短整数，表示消息类型
    private String content = null;

    // 同时为零，表示不用
    private int num1 = 0;
    private int num2 = 0;

    public NetData(long uid, int msgType, String content) {
        super();
        this.uid = uid;
        this.msgType = msgType;
        this.content = content;

        if (TextUtils.isEmpty(content)) {
            this.length = 0;
            this.content = content;
        } else {
            this.content = JniUtil.GetEncryptString(content);
            try {
                this.length = this.content.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                this.length = 0;
            }
        }
    }

    public NetData(int length, long uid, int msgType, String content){
        super();
        this.length = length;
        this.uid = uid;
        this.msgType = msgType;
        this.content = content;
    }

    // uid 必须是一个整型
    public NetData(String uid, int msgType, String content) {
        this(getUidFromStr(uid), msgType, content);
    }

    public NetData(long uid, int msgType, int num1, int num2) {
        super();
        this.uid = uid;
        this.msgType = msgType;
        this.content = null;

        this.length = 8;
        this.num1 = num1;
        this.num2 = num2;
    }

    public NetData(byte[] buffer, int length) {
        if (buffer == null || length < 10) return;

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, length);
        byte[] b = new byte[4];

        bais.read(b, 0, 4);
        this.length = ByteUtil.toInt(b);
        bais.read(b, 0, 4);
        this.uid = ByteUtil.toUnsignedInt(b);
        bais.read(b, 0, 2);
        this.msgType = ByteUtil.toUnsignedShort(b);

        try {
            bais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String tmp = null;
        try {
            tmp = new String(buffer, 10, this.length);
            if (!TextUtils.isEmpty(tmp)) {
                tmp = new String(JniUtil.GetDecryptString(tmp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.content = tmp;
    }

    public NetData(int contentLength, byte[] buffer, int length) {
        if (buffer == null || length < 6) {
            return;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, length);
        byte[] b = new byte[4];

        this.length = contentLength;
        bais.read(b, 0, 4);
        this.uid = ByteUtil.toUnsignedInt(b);
        bais.read(b, 0, 2);
        this.msgType = ByteUtil.toUnsignedShort(b);

        try {
            bais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tmp = null;
        try {
            tmp = new String(buffer, 6, this.length);
            if (!TextUtils.isEmpty(tmp)) {
                tmp = new String(JniUtil.GetDecryptString(tmp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.content = tmp;
    }

    /**
     * 从数据流中解析并读取数据体
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static NetData parseNetData(InputStream inputStream) throws IOException {
        NetData netData = null;
        byte[] buffer = new byte[8 * 1024];
        int readLen = -1;
        int totalReadLen = READ_DATA_HEAD_LENGTH_SIZE + READ_DATA_HEAD_UID_SIZE + READ_DATA_HEAD_MSG_TYPE_SIZE;
        int leftReadLen = totalReadLen;

        //读取数据头信息
        while((readLen = inputStream.read(buffer,totalReadLen - leftReadLen,leftReadLen)) != -1){
            leftReadLen = totalReadLen - readLen;
            if(leftReadLen == 0){
                break;
            }
        }
        //长连接一般情况下不会出现不完整的数据头且断开网络
        if(readLen == -1 && leftReadLen != 0){
            throw new IOException("数据读取错误");
        }

        //解析数据头信息
        int dataLength = ByteUtil.toInt(Arrays.copyOfRange(buffer,0,READ_DATA_HEAD_LENGTH_SIZE));
        long dataUid = ByteUtil.toUnsignedInt(Arrays.copyOfRange(buffer,READ_DATA_HEAD_LENGTH_SIZE,READ_DATA_HEAD_LENGTH_SIZE + READ_DATA_HEAD_UID_SIZE));
        int dataMsgType = ByteUtil.toUnsignedShort(Arrays.copyOfRange(buffer,READ_DATA_HEAD_LENGTH_SIZE + READ_DATA_HEAD_UID_SIZE,READ_DATA_HEAD_LENGTH_SIZE + READ_DATA_HEAD_UID_SIZE + READ_DATA_HEAD_MSG_TYPE_SIZE));
        String dataContent = "";

        PLogger.d("Socket read packet thread packet head , length:" + dataLength + ",uid:" + dataUid + ",msgType:" + dataMsgType);

        //考虑到以后可能只有数据头的情况，此处加入数据长度为0时候的判断
        if(dataLength != 0) {
            totalReadLen = dataLength;
            leftReadLen = dataLength;
            ByteArrayOutputStream bufferOutput = new ByteArrayOutputStream();
            try{
                //读取数据头信息
                while ((readLen = inputStream.read(buffer, 0, leftReadLen)) != -1) {
                    bufferOutput.write(buffer, 0, readLen);
                    leftReadLen = totalReadLen - readLen;
                    if (leftReadLen == 0) {
                        break;
                    }
                }

                final byte[] dataBytes = bufferOutput.toByteArray();
                //验证读取数据和数据长度是否相符合
                if (dataBytes != null && dataBytes.length == dataLength) {
                    dataContent = new String(dataBytes, 0, dataLength);
                    dataContent = new String(JniUtil.GetDecryptString(dataContent));
                }else{
                    throw new IOException("数据读取错误");
                }
            }finally {
                bufferOutput.close();
            }
        }
        PLogger.d("Socket read packet thread packet content:" + dataContent);

        netData = new NetData(dataLength, dataUid, dataMsgType, dataContent);
        return netData;
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNum(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    private static long getUidFromStr(String uid) {
        long id = 0;
        try {
            id = Integer.valueOf(uid);
        } catch (Exception e) {
        }
        return id;
    }

    public byte[] getBytes() {
        byte[] ret = null;

        try {
            byte[] bLength = ByteUtil.toBytes(length);
            byte[] bUid = ByteUtil.unsignedToBytes(uid);
            byte[] bType = ByteUtil.unsignedToBytes(msgType);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            baos.write(bLength);
            baos.write(bUid);
            baos.write(bType);

            if (num1 == 0 && num2 == 0) {
                if (!TextUtils.isEmpty(content)) {
                    baos.write(content.getBytes("UTF-8"));
                }
            } else {
                baos.write(ByteUtil.toBytes(num1));
                baos.write(ByteUtil.toBytes(num2));
            }

            baos.flush();
            baos.close();
            ret = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError ooe) {
            ooe.printStackTrace();
        }
        return ret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.length);
        dest.writeLong(this.uid);
        dest.writeInt(this.msgType);
        dest.writeString(this.content);
        dest.writeInt(this.num1);
        dest.writeInt(this.num2);
    }

    protected NetData(Parcel in) {
        this.length = in.readInt();
        this.uid = in.readLong();
        this.msgType = in.readInt();
        this.content = in.readString();
        this.num1 = in.readInt();
        this.num2 = in.readInt();
    }

    public static final Parcelable.Creator<NetData> CREATOR = new Parcelable.Creator<NetData>() {
        @Override
        public NetData createFromParcel(Parcel source) {
            return new NetData(source);
        }

        @Override
        public NetData[] newArray(int size) {
            return new NetData[size];
        }
    };

    @Override
    public String toString() {
        return "NetData{" +
                "length=" + length +
                ", uid=" + uid +
                ", msgType=" + msgType +
                ", content='" + content + '\'' +
                ", num1=" + num1 +
                ", num2=" + num2 +
                '}';
    }

    public String getHeaderString(){
        return "NetData Header :" +
                "length=" + length +
                ", uid=" + uid +
                ", msgType=" + msgType;
    }
}
