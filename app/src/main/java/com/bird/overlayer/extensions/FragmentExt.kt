package com.bird.overlayer.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun FragmentActivity.toast(@StringRes resourceId: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(resourceId), length)
}

fun FragmentActivity.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

inline fun FragmentManager.inTransactionWithStateLoss(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitNowAllowingStateLoss()
}

fun AppCompatActivity.findFragmentByTag(tag: String) = supportFragmentManager.findFragmentByTag(tag)

fun AppCompatActivity.hasFragmentByTag(tag: String) = supportFragmentManager.findFragmentByTag(tag) != null

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, tag: String) {
    supportFragmentManager.inTransaction {
        setPrimaryNavigationFragment(fragment)
        replace(frameId, fragment, tag)
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, tag: String) {
    supportFragmentManager.inTransaction {
        setPrimaryNavigationFragment(fragment)
        addToBackStack(tag)
        add(frameId, fragment)
    }
}

fun AppCompatActivity.popFragment(tag: String?) {
    supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    addFragment(fragment, frameId, fragment.javaClass.name)
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, targetFragment: Fragment, requestCode: Int) {
    fragment.setTargetFragment(targetFragment, requestCode)
    addFragment(fragment, frameId)
}

fun AppCompatActivity.replaceFragmentIfNotExists(fragment: Fragment, frameId: Int, tag: String) {
    if (!hasFragmentByTag(tag)) {
        replaceFragment(fragment, frameId, tag)
    }
}

fun AppCompatActivity.addFragmentIfNotExists(fragment: Fragment, frameId: Int, tag: String) {
    if (!hasFragmentByTag(tag)) {
        addFragment(fragment, frameId, tag)
    }
}

fun Fragment.replaceFragment(fragment: Fragment, frameId: Int, tag: String = "") {
    childFragmentManager.inTransaction {
        replace(frameId, fragment, tag)
    }
}

fun Fragment.replaceFragmentWithStateLoss(fragment: Fragment, frameId: Int, tag: String = "") {
    childFragmentManager.inTransactionWithStateLoss {
        replace(frameId, fragment, tag)
    }
}

fun Fragment.addFragment(fragment: Fragment, frameId: Int, tag: String = "") {
    childFragmentManager.inTransaction {
        addToBackStack(tag)
        add(frameId, fragment, tag)
    }
}

fun AppCompatActivity.showFragment(fragment: Fragment) {
    supportFragmentManager.inTransaction {
        show(fragment)
    }
}

fun AppCompatActivity.isPopBackStackImmediate(): Boolean {
    if (supportFragmentManager.backStackEntryCount > 0) {
        return supportFragmentManager.popBackStackImmediate()
    }
    return false
}
