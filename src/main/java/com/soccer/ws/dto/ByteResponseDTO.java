package com.soccer.ws.dto;

import java.util.Arrays;

public class ByteResponseDTO {
    private byte[] bytes;

    public ByteResponseDTO(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteResponseDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByteResponseDTO that = (ByteResponseDTO) o;

        return Arrays.equals(bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
