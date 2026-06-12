#include <jni.h>
#include <string>
#include <map>

extern "C" jobject Java_com_test_mandiri_news_core_network_helper_NativeHelper_appNativeValues(JNIEnv *env, jobject /* this */) {
    jclass mapClass = env->FindClass("java/util/HashMap");
    if (mapClass == NULL) {
        return NULL;
    }
    jmethodID init = env->GetMethodID(mapClass, "<init>", "(I)V");
    jmethodID put = env->GetMethodID(mapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    jsize initial_size = 1;
    jobject hashMap = env->NewObject(mapClass, init, initial_size);

    env->CallObjectMethod(hashMap, put, env->NewStringUTF("BASE_DEV_URL"),   env->NewStringUTF("https://newsapi.org/v2/"));
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("BASE_PROD_URL"),  env->NewStringUTF("https://newsapi.org/v2/"));
    env->CallObjectMethod(hashMap, put, env->NewStringUTF("NEWS_API_KEY"),   env->NewStringUTF("6315fb25cfd64344a00eeee6785ac115"));

    return hashMap;
}
