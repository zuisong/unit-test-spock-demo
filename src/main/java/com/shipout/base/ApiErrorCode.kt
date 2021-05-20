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

/**
 * REST API 错误码
 *
 * @author hubin
 * @since 2017-06-26
 */
enum class ApiErrorCode(override val code: Long, override val msg: String) : IErrorCode {
    /**
     * 失败
     */
    FAILED(-1, "failed"),

    /**
     * 成功
     */
    SUCCESS(0, "success");

    override fun toString(): String {
        return String.format(" ErrorCode:{code=%s, msg=%s} ", code, msg)
    }

    companion object {
        fun fromCode(code: Long): ApiErrorCode {
            val ecs = values()
            for (ec in ecs) {
                if (ec.code == code) {
                    return ec
                }
            }
            return SUCCESS
        }
    }
}
