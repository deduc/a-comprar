package org.ivandev.acomprar.database.handlers

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.ivandev.acomprar.Literals
import org.ivandev.acomprar.database.entities.UserActionsEntity
import org.ivandev.acomprar.enumeration.user_actions.UserBuyingEnum

object UserActionsHandler {
    fun initialize(db: SQLiteDatabase) {
        addUserBuyingAction(db)
    }

    private fun addUserBuyingAction(db: SQLiteDatabase) {
        val values = ContentValues().apply {
            put(Literals.Database.ColumnNames.ACTION_TYPE_COLUMN, Literals.UserActions.USER_BUYING)
            put(Literals.Database.ColumnNames.ACTION_VALUE_COLUMN, UserBuyingEnum.USER_IS_NOT_BUYING)
            put(Literals.Database.ColumnNames.TIMESTAMP_COLUMN, System.currentTimeMillis().toString())
        }

        val result = db.insert(
            Literals.Database.Tables.USER_ACTIONS_TABLE,
            null,
            values
        )

        if (result == 1L) println("[DEBUG] Acción user_buying añadida")
        else println("ERROR Añadiendo acción user_buying")
    }

    fun getUserActionByActionType(db: SQLiteDatabase, actionType: String): UserActionsEntity {
        var result = UserActionsEntity()

        db.query(
            Literals.Database.Tables.USER_ACTIONS_TABLE,
            null,
            "${Literals.Database.ColumnNames.ACTION_TYPE_COLUMN} = ?",
            arrayOf(actionType),
            null,
            null,
            null
        ).use { it: Cursor ->
            if (it.moveToFirst()) {
                result = UserActionsEntity(
                    id = it.getInt(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ID_COLUMN)),
                    actionType = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ACTION_TYPE_COLUMN)),
                    actionValue = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.ACTION_VALUE_COLUMN)),
                    timestamp = it.getString(it.getColumnIndexOrThrow(Literals.Database.ColumnNames.TIMESTAMP_COLUMN)),
                )
            }
        }
        return result
    }

    fun knowIfUserIsBuying(db: SQLiteDatabase): Boolean {
        return getUserActionByActionType(
            db, Literals.UserActions.USER_BUYING
        ).actionValue == UserBuyingEnum.USER_IS_BUYING
    }

    fun setUserIsBuying(db: SQLiteDatabase, newActionValue: String) {
        var action = getUserActionByActionType(db, Literals.UserActions.USER_BUYING)

        var values = ContentValues().apply {
            put(Literals.Database.ColumnNames.ACTION_TYPE_COLUMN, action.actionType)
            put(Literals.Database.ColumnNames.ACTION_VALUE_COLUMN, newActionValue)
            put(Literals.Database.ColumnNames.TIMESTAMP_COLUMN, System.currentTimeMillis().toString())
        }

        db.update(
            Literals.Database.Tables.USER_ACTIONS_TABLE,
            values,
            "${Literals.Database.ColumnNames.ID_COLUMN} = ?",
            arrayOf(action.id.toString())
        )
    }
}
