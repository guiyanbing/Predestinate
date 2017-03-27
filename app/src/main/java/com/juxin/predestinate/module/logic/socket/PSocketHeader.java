package com.juxin.predestinate.module.logic.socket;

import com.juxin.predestinate.module.util.ByteUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * socket头信息
 * Created by ZRP on 2017/3/23.
 */
public class PSocketHeader {

    private int bufferSize = 2 * 1024 * 1024;
    private boolean isHeader = true;

    public long userId = 0;             //客户id，爱爱中消息头字段
    public long length = 0;             //消息长度
    public int type = 0;                //消息类型

    /**
     * 设置消息头信息
     *
     * @param buffer 传入的字节数组
     */
    public void setBuffer(byte[] buffer) {
        if (buffer == null) {
            return;
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, TCPConstant.TCP_DATA_Header_Size);
        byte[] b = new byte[4];//读取数组单个最长

        //根据socket头定义，读取4-4-2的数据
        bais.read(b, 0, 4);
        this.length = ByteUtil.toUnsignedInt(b);
        bais.read(b, 0, 4);
        this.userId = ByteUtil.toUnsignedInt(b);
        bais.read(b, 0, 2);
        this.type = ByteUtil.toUnsignedShort(b);

        try {
            bais.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.length > bufferSize) {
            this.length = bufferSize;
        }
    }

    public boolean isHeader() {
        return isHeader;
    }

    public int getHeaderSize() {
        return TCPConstant.TCP_DATA_Header_Size;
    }

    public int getSize() {
        if (isHeader) {
            return getHeaderSize();
        } else {
            return (int) length;
        }
    }

    public int getLength() {
        return (int) length;
    }

    public void toggle() {
        isHeader = !isHeader;
    }

    @Override
    public String toString() {
        return "PSocketHeader{" +
                "bufferSize=" + bufferSize +
                ", isHeader=" + isHeader +
                ", userId=" + userId +
                ", length=" + length +
                ", type=" + type +
                '}';
    }
}
