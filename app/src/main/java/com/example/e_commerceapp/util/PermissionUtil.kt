package com.example.e_commerceapp.util

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

object PermissionUtil {

    fun hasPermission(context : Context) =
            EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            )
}