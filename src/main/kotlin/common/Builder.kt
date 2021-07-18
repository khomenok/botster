package common

interface Builder<T> {
    fun build(): T
}

fun <T, B : Builder<T>> builderFun(builder: B, init: B.() -> Unit): T {
    builder.init()
    return builder.build()
}