package com.theinsuranceboss.app

import android.app.Application
import com.theinsuranceboss.app.data.repo.AppRepository
import com.theinsuranceboss.app.data.session.SessionStore

class BossApp : Application() {
    lateinit var session: SessionStore
        private set
    lateinit var repository: AppRepository
        private set

    override fun onCreate() {
        super.onCreate()
        session = SessionStore(this)
        repository = AppRepository(session)
    }
}
