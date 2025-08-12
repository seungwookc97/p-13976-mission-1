package com.global.app

import java.nio.file.Path

object AppConfig {
    private var mode = "dev"

    fun setModelToTest(){
        mode = "test"
    }

    fun setModelToDev(){
        mode = "dev"
    }

    val dbDirPath: Path
        get() {
            return Path.of("data/db/${mode}")
        }
}
