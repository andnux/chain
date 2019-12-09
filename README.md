### 快速集成：
- **Step 1.** Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
- **Step 2.** Add the dependency
```groovy
dependencies {
    def chain_version = '0.1.1'
    implementation "com.github.andnux:chain:${chain_version}"
    or
    implementation "com.github.andnux.chain:btc:${chain_version}"
    implementation "com.github.andnux.chain:core:${chain_version}"
    implementation "com.github.andnux.chain:eos:${chain_version}" 
    implementation "com.github.andnux.chain:eth:${chain_version}" 
    implementation "com.github.andnux.chain:tron:${chain_version}" 
    implementation "com.github.andnux.chain:vsys:${chain_version}" 
}
```