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
 * REST API 请求异常类
 *
 * @author hubin
 * @since 2017-06-26
 */
class ApiException : RuntimeException {
    /**
     * 错误码
     */
    var errorCode: IErrorCode? = null
        private set

    constructor(errorCode: IErrorCode) : super(errorCode.msg) {
        this.errorCode = errorCode
    }

    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    companion object {
        /**
         * serialVersionUID
         */
        private const val serialVersionUID = -5885155226898287919L
    }
}
