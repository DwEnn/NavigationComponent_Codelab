/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.example.android.codelabs.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(">>>", "onCreateView()")
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(">>>", "onViewCreated()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(">>>", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(">>>", "onResume()")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(">>>", "onDetach()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(">>>", "onDestroy()")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(">>>", "onDestroyView()")
    }

}
