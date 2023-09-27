package com.example.onui.infra.feign.apple

import com.example.onui.global.config.feign.FeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


@FeignClient(name = "apple-public-key-client", url = "https://appleid.apple.com/auth", configuration = [FeignConfig::class])
interface AppleClient {

    @GetMapping("/keys")
    fun applePublicKeys(): ApplePublicKeys
}