/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gorgeous.launcher3.config;

import android.content.Context;

/**
 * <pre>
 *     author: Marco
 *     date  : 2024.09.13
 *     desc  : Defines a set of flags used to control various launcher behaviors.
 * </pre>
 *
 * Please only add flags to your assigned block to prevent merge conflicts. If you do not have
 * a block, please update the current empty block and add a new empty block below to prevent
 * merge conflicts with the previous block.
 * List of blocks can be found:
 * <a href="http://go/gnl-flags-block-directory">here</a>
 *
 * <p>All the flags should be defined here with appropriate default values.
 */
public final class GorgeousFlags {

    /**
     * Marco: Single-layer Launcher
     */
    public static boolean ENABLE_SINGLE_LAYER(Context context) {
        // 保留逻辑
        // return Settings.System.getInt(context.getContentResolver(), "launcher_style", 0) == 1;
        return true;
    };
}
