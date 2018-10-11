package com.android.enoticoncreaterkotlin.db

import android.content.Context
import com.litesuits.orm.LiteOrm
import com.litesuits.orm.db.assit.QueryBuilder
import java.util.*

class LiteOrmHelper {

    private val DB_NAME = "EmoticonCreatorKotlin.db"

    private val DEBUGGABLE = true // 是否输出log

    private var liteOrm: LiteOrm? = null

    constructor(context: Context) {
        liteOrm = LiteOrm.newSingleInstance(context, DB_NAME)
        liteOrm!!.setDebugged(DEBUGGABLE)
    }

    constructor(context: Context, isSingle: Boolean) {
        liteOrm = if (isSingle) {
            LiteOrm.newSingleInstance(context, DB_NAME)
        } else {
            LiteOrm.newCascadeInstance(context, DB_NAME)
        }
        liteOrm!!.setDebugged(DEBUGGABLE)
    }

    fun <T> queryFirst(cla: Class<T>): T? {
        val list: ArrayList<T> = queryAll(cla)
        return if (list.isNotEmpty()) {
            list[0]
        } else {
            null
        }
    }

    fun <T> queryFirst(clazz: Class<T>, where: String, vararg whereArgs: Any): T? {
        val list: ArrayList<T> = queryAll(clazz, where, whereArgs)
        return if (list.isNotEmpty()) {
            list[0]
        } else {
            null
        }
    }

    fun <T> queryAll(clazz: Class<T>): ArrayList<T> {
        return liteOrm!!.query(clazz)
    }

    fun <T> queryAll(clazz: Class<T>, where: String, whereArgs: Array<out Any>): ArrayList<T> {
        return liteOrm!!.query(QueryBuilder(clazz).where(where, whereArgs))
    }

    fun <T> queryAll(clazz: Class<T>, orderBy: String): ArrayList<T> {
        return liteOrm!!.query(QueryBuilder(clazz).appendOrderDescBy(orderBy))
    }

    fun <T> queryAll(clazz: Class<T>, where: String, orderBy: String, whereArgs: Array<out Any>): ArrayList<T> {
        return liteOrm!!.query(QueryBuilder(clazz).where(where, whereArgs).appendOrderDescBy(orderBy))
    }

    fun <T> queryAllOrderDescBy(clazz: Class<T>, orderBy: String): ArrayList<T> {
        return liteOrm!!.query(QueryBuilder(clazz).appendOrderDescBy(orderBy))
    }

    fun <T> queryAllWhereDescBy(clazz: Class<T>, where: String, orderBy: String, vararg whereArgs: Any): ArrayList<T> {
        return liteOrm!!.query(QueryBuilder(clazz).where(where, *whereArgs).appendOrderDescBy(orderBy))
    }

    fun <T> save(objec: T): Long {
        return liteOrm!!.save(objec)
    }

    fun <T> saveAll(objects: List<T>): Int {
        return liteOrm!!.save(objects)
    }

    fun <T> update(`object`: T): Int {
        return liteOrm!!.update(`object`)
    }

    fun <T> update(cla: Class<T>, where: String, vararg whereArgs: Any): Int {
        return liteOrm!!.update(QueryBuilder(cla).where(where, whereArgs))
    }

    fun <T> updateAll(objects: List<T>): Int {
        return liteOrm!!.update(objects)
    }

    fun <T> delete(`object`: T): Int {
        return liteOrm!!.delete(`object`)
    }

    fun <T> deleteAll(cla: Class<T>): Int {
        return liteOrm!!.delete(cla)
    }

    fun closeDB() {
        liteOrm?.close()
    }

}