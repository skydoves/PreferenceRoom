# PreferenceRoom 

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Android CI](https://github.com/skydoves/PreferenceRoom/actions/workflows/android.yml/badge.svg)](https://github.com/skydoves/PreferenceRoom/actions/workflows/android.yml)
[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23335-orange.svg)](https://androidweekly.net/issues/issue-335)
<br>

PreferenceRoom is an android annotation processor library to manage `SharedPreferences` more efficiently and structurally.
PreferenceRoom was inspired by [Architecture Components Room Persistence](https://developer.android.com/topic/libraries/architecture/room.html)
and [dagger](https://github.com/square/dagger).
PreferenceRoom integrates scattered `SharedPreferences` as a single entity and it supports custom setter/getter functions with security algorithm.
Also this library provides simple dependency injection system, which is free from reflection, and fully-supported in kotlin project. 


## Who's using this library?

| [GithubFollows](https://github.com/skydoves)<br>[Open Source](https://github.com/skydoves/githubfollows) | [All-In-One](https://github.com/skydoves)<br>[Open Source](https://github.com/skydoves/all-in-one) | [Battle Comics](http://www.battlecomics.co.kr/)<br>[Product](https://play.google.com/store/apps/details?id=com.whalegames.app) | [Epoptia](http://epoptia.com/cloud-mes-manufacturing-execution-system/) <br> [Open Source](https://github.com/tsironis13/EpoptiaKioskModeApp) | [Sensemore](https://sensemore.io/)
| :---------------: | :---------------: | :---------------: | :---------------: | :---------------: |
| ![Octocat](https://user-images.githubusercontent.com/24237865/61508303-38343180-aa24-11e9-8b26-43dd5332be98.png) | ![allinone](https://user-images.githubusercontent.com/24237865/61508304-38ccc800-aa24-11e9-8d43-c245b7278f5f.png) | ![battleent](https://user-images.githubusercontent.com/24237865/61508305-38ccc800-aa24-11e9-9d02-2b936e33f8cd.png) | ![epoptia](https://user-images.githubusercontent.com/24237865/61509215-7f242600-aa28-11e9-97fc-be53960d079b.png) | ![Sensemore](https://user-images.githubusercontent.com/24237865/71539660-ee605780-2982-11ea-964d-389604da8fac.png)

## Download
[![Maven Central](https://img.shields.io/maven-central/v/com.github.skydoves/preferenceroom.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.skydoves%22%20AND%20a:%22preferenceroom%22)

### Gradle
Add the codes below to your **root** `build.gradle` file (not your module build.gradle file).
```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```
And add the dependency below to your **module**'s `build.gradle` file.

```gradle
dependencies {
    implementation "com.github.skydoves:preferenceroom:1.2.2"
    annotationProcessor "com.github.skydoves:preferenceroom-processor:1.2.2"
    // in kotlin project use kapt instead of annotationProcessor
    kapt "com.github.skydoves:preferenceroom-processor:1.2.2"
}
```

## Table of Contents
#### [1.PreferenceEntity](https://github.com/skydoves/PreferenceRoom#preferenceentity)
- [KeyName](https://github.com/skydoves/PreferenceRoom#keyname)
- [TypeConverter](https://github.com/skydoves/PreferenceRoom#typeconverter)
- [PreferenceFunction](https://github.com/skydoves/PreferenceRoom#preferencefunction) 
- [EncryptEntity](https://github.com/skydoves/PreferenceRoom#encryptentity)
#### [2.PreferenceComponent](https://github.com/skydoves/PreferenceRoom#preferencecomponent)
#### [3.Dependency Injection](https://github.com/skydoves/PreferenceRoom#dependency-injection) (Use with [dagger](https://github.com/skydoves/PreferenceRoom/releases))
#### [4.Usage in Kotlin](https://github.com/skydoves/PreferenceRoom#usage-in-kotlin) ([Incremental annotation processing](https://github.com/skydoves/PreferenceRoom#incremetal-annotation-processing))
#### [5.Proguard-Rules](https://github.com/skydoves/PreferenceRoom#proguard-rules)
#### [6.Debugging with Stetho](https://github.com/skydoves/PreferenceRoom#debugging-with-stetho)

## PreferenceEntity
![preferenceentity](https://user-images.githubusercontent.com/24237865/33240687-5fa9ccca-d2fd-11e7-8962-e39c8dad5f41.png)<br>
`@PreferenceEntity` annotation makes SharedPreferences data as an entity.<br>
Value in `@PreferenceEntity` determines the entity name.<br>
Entity's default name is determined by class name.<br>

```java
@PreferenceEntity("UserProfile")
public class Profile {
    protected final boolean login = false;
    @KeyName("nickname") protected final String userNickName = null;
    @KeyName("visits") protected final int visitCount = 1;

    @KeyName("userPet")
    @TypeConverter(PetConverter.class)
    protected Pet userPetInfo;

    @PreferenceFunction("nickname")
    public String putUserNickFunction(String nickname) {
        return "Hello, " + nickname;
    }

    @PreferenceFunction("nickname")
    public String getUserNickFunction(String nickname) {
        return nickname + "!!!";
    }

    @PreferenceFunction("visits")
    public int putVisitCountFunction(int count) {
        return ++count;
    }
}
```

After the build your project, `Preference_(entity name)` class will be generated automatically. <br>
```java
Preference_UserProfile userProfile = Preference_UserProfile.getInstance(this); // gets instance of the UserProfile entity.
userProfile.putNickname("my nickname"); // puts a value in NickName.
userProfile.getNickname(); // gets a nickname value.
userProfile.containsNickname(); // checks nickname key value is exist or not in entity.
userProfile.removeNickname(); // removes nickname key value in entity.
userProfile.nicknameKeyName(); // returns nickname key name.
userProfile.getEntityName(); // returns UserProfile entity name;
userProfile.getkeyNameList(); // returns UserProfile entity's key name lists.

// or invoke static.
Preference_UserProfile.getInstance(this).putNickname("my nickname");
```

we can listen the changed value using `OnChangedListener`.<br>
`onChanged` method will be invoked if we change the value using `put` method.
```java
userProfile.addNicknameOnChangedListener(new Preference_UserProfile.NicknameOnChangedListener() {
   @Override
   public void onChanged(String nickname) {
     Toast.makeText(getBaseContext(), "onChanged :" + nickname, Toast.LENGTH_SHORT).show();
   }
});
```

Auto-generated code is managed by singletons. </br>
But we can manage more efficiently using [PreferenceComponent](https://github.com/skydoves/PreferenceRoom#preferencecomponent) and
[Dependency Injection](https://github.com/skydoves/PreferenceRoom#dependency-injection). <br>

We can set SharedPreference to an entity as DefaultSharedPreferences using `@DefaultPreference` annotation like below.
```java
@DefaultPreference
@PreferenceEntity("ProfileWithDefault")
public class UserProfilewithDefaultPreference {
    @KeyName("nickname")
    protected final String userNickName = "skydoves";
    
    // key name will be 'Login'. (login's camel uppercase)
    protected final boolean login = false;
}
```
The `ProfileWithDefault` entity from the example, will be initialized like below on `PreferenceRoom` processor.
```java
PreferenceManager.getDefaultSharedPreferences(context);
```
So we can connect with PreferenceFragmentCompat, PreferenceScreen or etc.

### KeyName
![keyname](https://user-images.githubusercontent.com/24237865/33240803-7c80bb7c-d2ff-11e7-98e4-cf43d6aebb1e.png)<br>
`@KeyName` annotation can be used on an entity class's field. <br>
The field's key name will be decided by field name, but we can customize the name as taste. <br>
```java
@KeyName("visits") // keyname will be Visits.
protected final int visitCount = 1;
```

### TypeConverter
![typeconverter](https://user-images.githubusercontent.com/24237865/33240860-c5b6495a-d300-11e7-8122-804993a07b4a.png) <br>
SharedPreference persists only primitive type data. <br>
But PreferenceRoom supports persistence obejct data using `TypeConverter` annotation. <br>
`@TypeConverter` annotation should be annotated over an object field like below. <br>
```java
@TypeConverter(PetConverter.class)
protected Pet userPetInfo; // 'Pet' class field in an entity.
```

Below example is creating a converter class using Gson. <br>
Converter class should extends the `PreferenceTypeConverter<?>` class. <br>
The basic principle is making object class to string data for persistence and getting the string data and recover.<br>
`convertObject` performs how to change class data to a string, `convertType` performs how to recover class data from the string data.
```java
public class PetConverter extends PreferenceTypeConverter<Pet> {

    private final Gson gson;

    // default constructor will be called by PreferenceRoom
    public PetConverter() {
        this.gson = new Gson();
    }

    @Override
    public String convertObject(Pet pet) {
        return gson.toJson(pet);
    }

    @Override
    public Pet convertType(String string) {
        return gson.fromJson(string, Pet.class);
    }
}
```

#### BaseTypeConverter
We can generalize the converter using generic like below. <br>

```java
public class BaseGsonConverter<T> extends PreferenceTypeConverter<T> {

    private final Gson gson;

    public BaseGsonConverter(Class<T> clazz) {
        super(clazz);
        this.gson = new Gson();
    }

    @Override
    public String convertObject(T object) {
        return gson.toJson(object);
    }

    @Override
    public T convertType(String string) {
        return gson.fromJson(string, clazz);
    }
}
```
So we can use the converter to any classes.
```java
@KeyName("userinfo")
@TypeConverter(BaseGsonConverter.class)
protected PrivateInfo privateInfo;

@KeyName("userPet")
@TypeConverter(BaseGsonConverter.class)
protected Pet userPetInfo;
```

### PreferenceFunction
![preferencefunction](https://user-images.githubusercontent.com/24237865/33240543-c292ee82-d2fa-11e7-86c2-b013830965b2.png)<br>
`@PreferenceFunction` annotation processes pre/post-processing through getter and setter functions. <br>
`@PreferenceFunction` annotation's value decides a target. The target should be a keyName. <br>
The function's naming convention is which should start with `put` or `get` prefix. <br>
`put_functionname_` is pre-processing getter function and `get_functionname_` is postprocessing getter function.

```java
@PreferenceFunction("nickname")
public String putUserNickFunction(String nickname) {
    return "Hello, " + nickname;
}

@PreferenceFunction("nickname")
public String getUserNickFunction(String nickname) {
    return nickname + "!!!";
}
```

### EncryptEntity
SharedPreferences data are not safe from hacking even if private-mode.<br>
There is a simple way to encrypt whole entity using `@EncryptEntity` annotation.<br>
It is based on AES128 encryption. So we should set the key value along __16 size__ length.<br>
If `put` method invoked, the value is encrypted automatically. <br>
And if `get` method invoked, it returns the decrypted value.

```java
@EncryptEntity("1234567890ABCDFG")
@PreferenceEntity("UserProfile")
public class Profile {
}
```
Or we can customize encrypting and decrypting algorithm using `PreferenceFunction`.
```java
@PreferenceFunction("uuid")
public String putUuidFunction(String uuid) {
   return SecurityUtils.encrypt(uuid);
}

@PreferenceFunction("uuid")
public String getUuidFunction(String uuid) {
    return SecurityUtils.decrypt(uuid);
}
```

## PreferenceComponent
![preferencecomponent](https://user-images.githubusercontent.com/24237865/33240928-10a88e18-d302-11e7-8ff5-b5d4f33de692.png) <br>
PreferenceComponent integrates entities. `@PreferenceComponent` annotation should be annotated on an interface class.<br>
We can integrate many entities as one component using entities value in `@PreferenceComponent` annotation. <br>
So we can initialize many entities at once through the component and get instances from the component. <br>
And the instance of the `PreferenceComponent` is managed by singleton by PreferenceRoom.
`PreferenceComponent's instance also singletons. And all entities instances are initialized when the component is initialized.<br>
```java
@PreferenceComponent(entities = {Profile.class, Device.class})
public interface UserProfileComponent {
}
```
After build your project, `PreferenceComponent_(component's name)` class will be generated automatically. <br>
The best-recommanded way to initialize component is initializing on Application class. So we can manage the component as a singleton instance.

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceComponent_UserProfileComponent.init(this);
    }
}

```
After initializing the component, we can access and use the instances of component and component's entities anywhere.<br>
```java
PreferenceComponent_UserProfileComponent component = PreferenceComponent_UserProfileComponent.getInstance();
Preference_UserProfile userProfile = component.UserProfile();
Preference_UserDevice userDevice = PreferenceComponent_UserProfileComponent.getInstance().UserDevice();
```

## Dependency Injection
![di](https://user-images.githubusercontent.com/24237865/33241294-bfaf9b5a-d306-11e7-816a-2be938fafdf8.png) <br>
`PreferenceRoom` supports simple dependency injection process with free from reflection using `@InjectPreference` annotation. But If you want to use with dagger, check [this](https://github.com/skydoves/PreferenceRoom/releases) reference.

Firstly we should declare some target classes which to be injected preference instances in `PreferenceComponent`. <br>
```java
@PreferenceComponent(entities = {Profile.class, Device.class})
public interface UserProfileComponent {
    // declares targets for the dependency injection.
    void inject(MainActivity __);
    void inject(LoginActivity __);
}
```

We should annotate InjectPreference annotation to preference entity or component class field which generated by PreferenceRoom. <br>
The field's modifier should be `public`.
```java
@InjectPreference
public PreferenceComponent_UserProfileComponent component;

@InjectPreference
public Preference_UserProfile userProfile;
```

And the last, we should inject instances of entity and component to targets fields using `inject` method from an instance of the component.  </br>
```java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);
   PreferenceComponent_UserProfileComponent.getInstance().inject(this);
}
```

## Usage in Kotlin
It is similar to java project usage. <br>
But the most important thing is which we should use an `open` modifier on entity classes and PreferenceFunctions. <br>
And the field's modifier should be `@JvmField val`.
```java
@PreferenceEntity("UserDevice")
open class Device {
    @KeyName("version")
    @JvmField val deviceVersion: String? = null

    @KeyName("uuid")
    @JvmField val userUUID: String? = null

    @PreferenceFunction("uuid")
    open fun putUuidFunction(uuid: String?): String? {
        return SecurityUtils.encrypt(uuid)
    }

    @PreferenceFunction( "uuid")
    open fun getUuidFunction(uuid: String?): String? {
        return SecurityUtils.decrypt(uuid)
    }
}
```

And the component usage is almost the same as Java examples.
```java
@PreferenceComponent(entities = [Profile::class, Device::class])
interface UserProfileComponent {
    // declare dependency injection targets.
    fun inject(target: MainActivity)
    fun inject(target: LoginActivity)
}
```

And the last, the usage of the dependency injection is the same as the java. but we should declare the component and entity field's modifier as lateinit var. <br>
```java
@InjectPreference
lateinit var component: PreferenceComponent_UserProfileComponent
    
override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
   setContentView(R.layout.activity_main)
    PreferenceComponent_UserProfileComponent.getInstance().inject(this) // inject dependency injection to MainActivity.
```

### Incremetal annotation processing
Starting from version `1.3.30`, kapt supports incremental annotation processing as an experimental feature. Currently, annotation processing can be incremental only if all annotation processors being used are incremental.<br>
Incremental annotation processing is enabled by default starting from version `1.3.50`.<br>
`PreferenceRoom` supports incremental annotation processing since version `1.1.9` and here is a Before/After example result of the enabled.

Before (23.758s) | After (18.779s) | 
| :---------------: | :---------------: | 
| <img src="https://user-images.githubusercontent.com/24237865/76435566-7b4f7480-63fa-11ea-999f-25133c577d07.png" align="center" width="100%"/> | <img src="https://user-images.githubusercontent.com/24237865/76435569-7d193800-63fa-11ea-9d75-dd0a3e9de68c.png" align="center" width="100%"/> |


### Non Existent Type Correction
If you encounter `NonExistentClass` error at compile time, you should add below codes on your build.gradle. <br>
Default, Kapt replaces every unknown type (including types for the generated classes) to NonExistentClass, but you can change this behavior. Add the additional flag to the build.gradle file to enable error type inferring in stubs:
```xml
kapt {
  correctErrorTypes = true
}
```

## Proguard Rules
```xml
# Retain generated class which implement PreferenceRoomImpl.
-keep public class ** implements com.skydoves.preferenceroom.PreferenceRoomImpl

# Prevent obfuscation of types which use PreferenceRoom annotations since the simple name
# is used to reflectively look up the generated Injector.
-keep class com.skydoves.preferenceroom.*
-keepclasseswithmembernames class * { @com.skydoves.preferenceroom.* <methods>; }
-keepclasseswithmembernames class * { @com.skydoves.preferenceroom.* <fields>; }
```

## Debugging with Stetho
We can debug SharedPreferences values which managed by PreferenceRoom using Stetho. <br> 
![screenshot635705571](https://user-images.githubusercontent.com/24237865/43187949-e35f5812-902d-11e8-8aa9-c090b90e96c5.png)

## References
- [How to manage SharedPreferences on Android project more efficiently](https://medium.com/@skydoves/how-to-manage-sharedpreferences-on-android-project-5e6d5e28fee6)

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/skydoves/PreferenceRoom/stargazers)__ for this repository. :star:

## Sponsor :coffee:
If you feel like to sponsor me a coffee for my efforts, I would greatly appreciate it. <br>

<a href="https://www.buymeacoffee.com/skydoves" target="_blank"><img src="https://skydoves.github.io/sponsor.png" alt="Buy Me A Coffee" style="height: 51px !important;width: 217px !important;" ></a>

# License
```xml
Copyright 2017 skydoves

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
