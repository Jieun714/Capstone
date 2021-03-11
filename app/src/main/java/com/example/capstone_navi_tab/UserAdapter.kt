package com.example.capstone_navi_tab

import android.content.Context
import com.google.firebase.firestore.QueryDocumentSnapshot

data class User(val id: String, val displayname: String, val email: String){
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["displayname"].toString(), doc["email"].toString())
    constructor(key: String, map: Map<*, *>) :
            this(key, map["displayname"].toString(), map["email"].toString())

}

class UserAdapter(private val context: Context, private var users:List<User>){
    fun updateList(newList: List<User>){
        users = newList
    }
}