package com.hanafey.android

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.hanafey.example.theming.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.reflect.KClass

class ViewModelLazyAndAlso<VM : ViewModel>(
    private val viewModelLazy: ViewModelLazy<VM>,
    private val andAlso: (VM) -> Unit
) : Lazy<VM> {
    override val value: VM
        get() {
            val isInitialized = viewModelLazy.isInitialized()
            val vm = viewModelLazy.value
            if (!isInitialized) {
                andAlso(vm)
            }
            return vm
        }

    override fun isInitialized(): Boolean {
        return viewModelLazy.isInitialized()
    }

}

@MainThread
fun <VM : ViewModel> Fragment.createViewModelLazyAndAlso(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    andAlso: (VM) -> Unit
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazyAndAlso(ViewModelLazy(viewModelClass, storeProducer, factoryPromise), andAlso)
}

/**
 * Just like [Fragment.viewModels] except caller is given the chance to do one time processing on the returned
 * view model.
 * @param andAlso If the returned object is not from cache this lambda gets the newly created object to do work on
 * before it is returned.
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModelsAndAlso(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null,
    noinline andAlso: (VM) -> Unit
) = createViewModelLazyAndAlso(VM::class, { ownerProducer().viewModelStore }, factoryProducer, andAlso)

/**
 * View binder. It must be called when the view id is valid, which means after the view
 * hierarchy has been inflated.
 */
fun <T : View> Activity.bindView(@IdRes id: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        findViewById<T>(id)
    }
}

fun <T : View> Fragment.bindView(@IdRes id: Int): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        this.activity?.findViewById<T>(id) ?: throw IllegalStateException("Fragment not activity attached, or view does not contain ID!")
    }
}

/**
 * Changes view visibility to [View.VISIBLE] or [View.INVISIBLE] based on [visible]. Does nothing if
 * view is already in target state.
 */
fun View.visible(visible: Boolean) {
    if (visible && visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    } else if (!visible && visibility == View.VISIBLE) {
        visibility = View.INVISIBLE
    }
}

inline fun elog(tag: String, message: () -> String) {
    if (BuildConfig.DEBUG) {
        if (Log.isLoggable(tag, Log.ERROR)) {
            Log.println(Log.ERROR, tag, message())
        }
    }
}

inline fun dlog(tag: String, enabled: Boolean = true, message: () -> String) {
    if (enabled) {
        if (BuildConfig.DEBUG) {
            if (Log.isLoggable(tag, Log.ERROR)) {
                Log.println(Log.ERROR, tag, message())
            }
        }
    }
}

fun stackTraceToString(exception: Throwable): String {
    val os = ByteArrayOutputStream(1024)
    val ps = PrintStream(os)
    try {
        exception.fillInStackTrace()
        exception.printStackTrace(ps)
        ps.flush()
        return os.toString("UTF8")
    } finally {
        ps.close()
        os.close()
    }
}

interface DialogFragmentListener {
    /**
     * @param tag The tag given to the [androidx.fragment.app.DialogFragment] when in was shown.
     * @param intent The message back to the caller.
     */
    fun message(tag: String, intent: Intent)

    companion object {
        val RESULT_OK = Intent("OK")
        val RESULT_CANCEL = Intent("CANCEL")
    }
}
