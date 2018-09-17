package ru.grakhell.userviewer.auth

class GitAccount(val name:String, val token:String) {

    companion object {
        const val SCOPES:String = "user " +
            "public_repo " +
            "repo " +
            "repo_deployment " +
            "repo:status " +
            "read:repo_hook " +
            "read:org " +
            "read:public_key " +
            "read:gpg_key"

        const val ARG_ACCOUNT_TYPE:String = "ACCOUNT_TYPE"
        const val ARG_AUTH_TYPE:String = "AUTH_TYPE"
        const val ARG_ACCOUNT_NAME:String = "ACCOUNT_NAME"
        const val ARG_IS_ADDING_NEW_ACCOUNT:String = "IS_ADDING_ACCOUNT"

        const val AUTHTOKEN_TYPE_GIT_SCOPE = "Read access"

    }
}