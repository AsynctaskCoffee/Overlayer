package com.bird.overlayer.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar

inline operator fun ViewGroup.get(i: Int): View? = getChildAt(i)

/**
 * -=
 */
inline operator fun ViewGroup.minusAssign(child: View) = removeView(child)

/**
 * +=
 */
inline operator fun ViewGroup.plusAssign(child: View) = addView(child)

/**
 * if (view in views)
 */
inline fun ViewGroup.contains(child: View) = indexOfChild(child) != -1

inline fun ViewGroup.first(): View? = this[0]

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    for (i in 0 until childCount) {
        action(getChildAt(i))
    }
}

inline fun ViewGroup.forEachIndexed(action: (Int, View) -> Unit) {
    for (i in 0 until childCount) {
        action(i, getChildAt(i))
    }
}

inline fun <reified T : Any> ViewGroup.children() = object : Iterable<T> {
    override fun iterator() = object : Iterator<T> {
        var index = 0
        override fun hasNext() = index < childCount
        override fun next() = getChildAt(index++) as T
    }
}

var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

var View.invisible
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

// Change visibility to View.INVISIBLE instead of View.GONE
var View.shouldShow
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun EditText.onTextChanged(listener: (String) -> Unit) {

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val s = editable.toString()
            listener(s)
        }

        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    })
}

fun EditText.input() = text.toString()

fun EditText.onActionListener(imeAction: Int, listener: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == imeAction) {
            consume { listener() }
        }
        false
    }
}

fun View.setKeyboardVisibility(visible: Boolean) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.apply {
        if (visible) {
            toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            hideSoftInputFromWindow(windowToken, 0)
        }
    }
}

fun View.hideKeyboard(context: Context?) {
    context?.inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.postDelayed(delayInMillis: Long, block: () -> Unit) {
    postDelayed(block, delayInMillis)
}

fun ViewGroup.postDelayed(delayInMillis: Long, block: () -> Unit) {
    postDelayed(block, delayInMillis)
}

fun withDelay(delayInMillis: Long, block: () -> Unit) {
    Handler().postDelayed(Runnable(block), delayInMillis)
}

fun getViewKeypadHeight(view: View): Pair<Int, Int> {
    val r = Rect()
    val viewHeight = view.rootView.height
    view.getWindowVisibleDisplayFrame(r)
    val keypadHeight = viewHeight - r.bottom
    return Pair(viewHeight, keypadHeight)
}

fun ImageView.setTint(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, colorRes)))
}

inline fun View.snackbar(resId: Int, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, resId, length)
    snack.show()
}

inline fun View.snackbar(text: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, text, length)
    snack.show()
}

inline fun View.snackbar(resId: Int, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, resId, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(resId: Int, color: Int? = null, listener: (View) -> Unit) {
    setAction(resId, listener)
    color?.let { setActionTextColor(color) }
}

fun ViewPager.onPageSelected(position: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(p: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(p: Int) {
            currentItem = p
            position(p)
        }
    })
}