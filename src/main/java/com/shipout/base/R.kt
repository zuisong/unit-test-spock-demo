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

/**
 * REST API 返回结果
 *
 * @author hubin
 * @since 2018-06-05
 */
@Suppress("MemberVisibilityCanBePrivate")
data class R<T>(
    /**
     * 业务错误码
     */
    val code: Int,

    /**
     * 结果集
     */
    val data: T?,

    /**
     * 描述
     */
    val msg: String? = null,
) : Serializable {

    fun success(): Boolean {
        return ApiErrorCode.SUCCESS.code == code
    }

    /**
     * 服务间调用非业务正常，异常直接释放
     */
    fun serviceData(): T? {
        if (!success()) {
            throw ApiException(msg)
        }
        return data
    }

    companion object {
        /**
         * serialVersionUID
         */
        private const val serialVersionUID = 1L

        @JvmStatic
        fun <T> ok(data: T): R<T?> {
            var aec = ApiErrorCode.SUCCESS
            if (data is Boolean && java.lang.Boolean.FALSE == data) {
                aec = ApiErrorCode.FAILED
            }
            return restResult(data, aec)
        }

        @JvmStatic
        fun <T> failed(msg: String?): R<T?> {
            return restResult(null, ApiErrorCode.FAILED.code, msg)
        }

        @JvmStatic
        fun <T> failed(errorCode: IErrorCode): R<T?> {
            return restResult(null, errorCode)
        }

        @JvmStatic
        fun <T> restResult(data: T?, errorCode: IErrorCode): R<T?> {
            return restResult(data, errorCode.code, errorCode.msg)
        }

        private fun <T> restResult(data: T?, code: Int, msg: String?): R<T?> =
            R<T?>(code = code, data = data, msg = msg)
    }
}
