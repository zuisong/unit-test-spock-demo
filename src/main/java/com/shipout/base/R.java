/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.shipout.base;

import java.io.Serializable;
import java.util.Optional;

/**
 * REST API 返回结果
 *
 * @author hubin
 * @since 2018-06-05
 */
public class R<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 业务错误码
     */
    private long code;
    /**
     * 结果集
     */
    private T data;
    /**
     * 描述
     */
    private String msg;

    public R() {
        // to do nothing
    }

    public R(IErrorCode errorCode) {
        errorCode = Optional.ofNullable(errorCode).orElse(ApiErrorCode.FAILED);
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public static <T> R<T> ok(T data) {
        ApiErrorCode aec = ApiErrorCode.SUCCESS;
        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            aec = ApiErrorCode.FAILED;
        }
        return restResult(data, aec);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, ApiErrorCode.FAILED.getCode(), msg);
    }

    public static <T> R<T> failed(IErrorCode errorCode) {
        return restResult(null, errorCode);
    }

    public static <T> R<T> restResult(T data, IErrorCode errorCode) {
        return restResult(data, errorCode.getCode(), errorCode.getMsg());
    }

    private static <T> R<T> restResult(T data, long code, String msg) {
        R<T> apiResult = new R<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public boolean ok() {
        return ApiErrorCode.SUCCESS.getCode() == code;
    }

    /**
     * 服务间调用非业务正常，异常直接释放
     */
    public T serviceData() {
        if (!ok()) {
            throw new ApiException(this.msg);
        }
        return data;
    }

    public long getCode() {
        return this.code;
    }

    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof R)) return false;
        final R<?> other = (R<?>) o;
        if (!other.canEqual(this)) return false;
        if (this.getCode() != other.getCode()) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        return this$msg == null ? other$msg == null : this$msg.equals(other$msg);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof R;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $code = this.getCode();
        result = result * PRIME + (int) ($code >>> 32 ^ $code);
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        final Object $msg = this.getMsg();
        result = result * PRIME + ($msg == null ? 43 : $msg.hashCode());
        return result;
    }

    public String toString() {
        return "R(code=" + this.getCode() + ", data=" + this.getData() + ", msg=" + this.getMsg() + ")";
    }
}
