package ru.grakhell.oauthlib.manager

class GitAccount {
    companion object {

        const val client_id = "ef5770ffd7b3fb12a445"
        const val client_sec = "258b878a1431750bb69fa135f29abd883419cc4b"
        const val key_note = "User view app key"

        val SCOPE: ArrayList<String> = arrayListOf(
            "user",
            "public_repo",
            "repo",
            "repo_deployment",
            "repo:status",
            "read:repo_hook",
            "read:org",
            "read:public_key",
            "read:gpg_key")

        const val ACCOUNT_TYPE = "com.github.account"
        const val AUTHTOKEN_TYPE_GIT_SCOPE = "Read access"

        const val ARG_ACCOUNT_TYPE: String = "ACCOUNT_TYPE"
        const val ARG_AUTH_TYPE: String = "AUTH_TYPE"
        const val ARG_IS_ADDING_NEW_ACCOUNT: String = "IS_ADDING_ACCOUNT"
        const val ARG_IS_GET_TOKEN: String = "IS_GET_TOKEN"
    }
}