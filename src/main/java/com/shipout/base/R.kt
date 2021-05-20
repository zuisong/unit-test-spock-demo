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
package com.shipout.base

import java.io.*
import java.util.*

/**
 * REST API 返回结果
 *
 * @author hubin
 * @since 2018-06-05
 */
class R<T> : Serializable {
    /**
     * 业务错误码
     */
    var code: Long = 0

    /**
     * 结果集
     */
    private var data: T? = null

    /**
     * 描述
     */
    var msg: String? = null

    constructor() {
        // to do nothing
    }

    constructor(errorCode: IErrorCode) {
        var errorCode = errorCode
        errorCode = Optional.ofNullable(errorCode).orElse(ApiErrorCode.FAILED)
        this.code = errorCode.code
        msg = errorCode.msg
    }

    fun ok(): Boolean {
        return ApiErrorCode.SUCCESS.code == code
    }

    /**
     * 服务间调用非业务正常，异常直接释放
     */
    fun serviceData(): T? {
        if (!ok()) {
            throw ApiException(msg)
        }
        return data
    }

    fun getData(): T? {
        return data
    }

    fun setData(data: T) {
        this.data = data
    }

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is R<*>) return false
        val other = o
        if (!other.canEqual(this)) return false
        if (this.code != other.code) return false
        val `this$data`: Any? = getData()
        val `other$data` = other.getData()
        if (if (`this$data` == null) `other$data` != null else `this$data` != `other$data`) return false
        val `this$msg`: Any? = msg
        val `other$msg`: Any? = other.msg
        return if (`this$msg` == null) `other$msg` == null else `this$msg` == `other$msg`
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is R<*>
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$code` = this.code
        result = result * PRIME + (`$code` ushr 32 xor `$code`).toInt()
        val `$data`: Any? = getData()
        result = result * PRIME + (`$data`?.hashCode() ?: 43)
        val `$msg`: Any? = msg
        result = result * PRIME + (`$msg`?.hashCode() ?: 43)
        return result
    }

    override fun toString(): String {
        return "R(code=" + this.code + ", data=" + getData() + ", msg=" + msg + ")"
    }

    companion object {
        /**
         * serialVersionUID
         */
        private const val serialVersionUID = 1L
        fun <T> ok(data: T): R<T?> {
            var aec = ApiErrorCode.SUCCESS
            if (data is Boolean && java.lang.Boolean.FALSE == data) {
                aec = ApiErrorCode.FAILED
            }
            return restResult(data, aec)
        }

        fun <T> failed(msg: String?): R<T?> {
            return restResult(null, ApiErrorCode.FAILED.code, msg)
        }

        fun <T> failed(errorCode: IErrorCode): R<T?> {
            return restResult(null, errorCode)
        }

        fun <T> restResult(data: T?, errorCode: IErrorCode): R<T?> {
            return restResult(data, errorCode.code, errorCode.msg)
        }

        private fun <T> restResult(data: T?, code: Long, msg: String?): R<T?> {
            val apiResult = R<T?>()
            apiResult.code = code
            apiResult.setData(data)
            apiResult.msg = msg
            return apiResult
        }
    }
}
