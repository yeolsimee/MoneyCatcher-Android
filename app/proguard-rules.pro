# Keep class names of Hilt injected ViewModels since their name are used as a multibinding map key.
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# https://github.com/square/okhttp/pull/6792
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

# naver login
-keep public class com.nhn.android.naverlogin.** {
       public protected *;
}

# Firebase
-keep class com.google.android.gms.** { *; }
-keep class com.google.firebase.** { *; }

# entity
-keep class com.yeolsimee.moneysaving.domain.entity.** { *; }
-keep class com.yeolsimee.moneysaving.data.entity.** { *; }