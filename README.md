# PreferenceRoom 
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![API](https://img.shields.io/badge/API-11%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=11)
[![Build Status](https://travis-ci.org/skydoves/PreferenceRoom.svg?branch=master)](https://travis-ci.org/skydoves/PreferenceRoom)
[![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23335-orange.svg)](https://androidweekly.net/issues/issue-335)
<br>
Android processor library for managing  SharedPreferences persistence efficiently and structurally.<br>
PreferenceRoom is inspired by [Architecture Components Room Persistence](https://developer.android.com/topic/libraries/architecture/room.html)
and [dagger](https://github.com/square/dagger). <br>
PreferenceRoom integrates scattered SharedPreferences as an entity.<br>
It supports putter & getter custom functions with security algorithm and could put & get objects. 
Also supports simple preference dependency injection with free from reflection. Fully supported in kotlin project. 

## Download
#### Gradle
And add below dependencies to your module's `build.gradle` file.
```gradle
dependencies {
    implementation "com.github.skydoves:preferenceroom:1.1.3"
    annotationProcessor "com.github.skydoves:preferenceroom-processor:1.1.3"
}
```

## Index
#### [1.PreferenceEntity](https://github.com/skydoves/PreferenceRoom#preferenceentity)
* [KeyName](https://github.com/skydoves/PreferenceRoom#keyname)
* [TypeConverter](https://github.com/skydoves/PreferenceRoom#typeconverter)
* [PreferenceFunction](https://github.com/skydoves/PreferenceRoom#preferencefunction) 
([security](https://github.com/skydoves/PreferenceRoom#security))
#### [2.PreferenceComponent](https://github.com/skydoves/PreferenceRoom#preferencecomponent)
#### [3.Dependency Injection](https://github.com/skydoves/PreferenceRoom#dependency-injection)
#### [4.Usage in Kotlin](https://github.com/skydoves/PreferenceRoom#usage-in-kotlin)
#### [5.Proguard-Rules](https://github.com/skydoves/PreferenceRoom#proguard-rules)
#### [6.Debugging with Stetho](https://github.com/skydoves/PreferenceRoom#debugging-with-stetho)

## PreferenceEntity
![preferenceentity](https://user-images.githubusercontent.com/24237865/33240687-5fa9ccca-d2fd-11e7-8962-e39c8dad5f41.png)<br>
`@PreferenceEntity` annotation makes SharedPreferences data as an entity.<br>
"name" value in `@PreferenceEntity` determines entity's name.<br>
Entity's default name is determined by class name.<br>

```java
@PreferenceEntity(name = "UserProfile")
public class Profile {
    protected final boolean login = false;
    @KeyName(name = "nickname") protected final String userNickName = null;
    @KeyName(name = "visits") protected final int visitCount = 1;

    @KeyName(name = "userPet")
    @TypeConverter(converter = PetConverter.class)
    protected Pet userPetInfo;

    @PreferenceFunction(keyname = "nickname")
    public String putUserNickFunction(String nickname) {
        return "Hello, " + nickname;
    }

    @PreferenceFunction(keyname = "nickname")
    public String getUserNickFunction(String nickname) {
        return nickname + "!!!";
    }

    @PreferenceFunction(keyname = "visits")
    public int putVisitCountFunction(int count) {
        return ++count;
    }
}
```

After the build process, we can use Preference_(entity's name) class like following. <br>
```java
Preference_UserProfile userProfile = Preference_UserProfile.getInstance(this);
userProfile.putNickname("my nickname"); // puts a SharedPreference in NickName key.
userProfile.getNickname(); // gets a SharedPreference value in NickName key.
userProfile.containsNickname(); // checks nickname key value is exist in SharedPreference.
userProfile.removeNickname(); // removes nickname key's value in SharedPreference.
userProfile.nicknameKeyName(); // returns nickname fields's key name.
userProfile.getEntityName(); // returns UserProfile entity's name;
userProfile.getkeyNameList(); // returns UserProfile entity's KeyName list of fields.

// or invoke static.
Preference_UserProfile.getInstance(this).putNickname("my nickname");
```

Auto-generated code is managed by singletons. </br>
But manage more efficiently using [PreferenceComponent](https://github.com/skydoves/PreferenceRoom#preferencecomponent) and
[Dependency Injection](https://github.com/skydoves/PreferenceRoom#dependency-injection). <br>

We can set SharedPreference as DefaultSharedPreferences using `@DefaultPreference` annotation like below.
```java
@DefaultPreference
@PreferenceEntity(name = "ProfileWithDefault")
public class UserProfilewithDefaultPreference {
    @KeyName(name = "nickname")
    protected final String userNickName = "skydoves";

    /**
     * key name will be 'Login'. (login's camel uppercase)
     */
    protected final boolean login = false;

    // - skipped - //
}
```
Then "ProfileWithDefault" entity's instance will be initialized like below.
```java
PreferenceManager.getDefaultSharedPreferences(context);
```
So we can connect with PreferenceActivity, PreferenceFragment or etc.

### KeyName
![keyname](https://user-images.githubusercontent.com/24237865/33240803-7c80bb7c-d2ff-11e7-98e4-cf43d6aebb1e.png)<br>
`@KeyName` annotation is used in an entity. <br>
`@KeyName`'s name value determines Sharedpreference's key name.
```java
@KeyName(name = "visits") // keyname will be Visits.
protected final int visitCount = 1;
```

### TypeConverter
![typeconverter](https://user-images.githubusercontent.com/24237865/33240860-c5b6495a-d300-11e7-8122-804993a07b4a.png)<br>
You can put and get Objects using TypeConverter.<br>
`@TypeConverter`'s converter value determines Converter.<br>
```java
@KeyName(name = "userPet")
@TypeConverter(converter = PetConverter.class)
protected Pet userPetInfo;
```

below example is converter using Gson. <br>
Converter should extends `PreferenceTypeConverter<?>` class.
```java
public class PetConverter extends PreferenceTypeConverter<Pet> {

    private final Gson gson;

    /**
     * default constructor will be called by PreferenceRoom
     */
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
This is a Base-Gson-Converter example. <br>
You can apply to all objects using generics like below.

```java
public class BaseGsonConverter<T> extends PreferenceTypeConverter<T> {

    private final Gson gson;

    /**
     * default constructor will be called by PreferenceRoom
     */
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

and using like this.
```java
@KeyName(name = "userinfo")
@TypeConverter(converter = BaseGsonConverter.class)
protected PrivateInfo privateInfo;

@KeyName(name = "userPet")
@TypeConverter(converter = BaseGsonConverter.class)
protected Pet userPetInfo;
```

### PreferenceFunction
![preferencefunction](https://user-images.githubusercontent.com/24237865/33240543-c292ee82-d2fa-11e7-86c2-b013830965b2.png)<br>
`@PreferenceFunction` annotation processes getter and setter functions. <br>
`@PreferenceFunction`'s `keyname` value determines a target. The target should be a keyName. <br>
Function's name should start with `put` or `get` prefix. <br>
`put_functionname_` will processes getter function and `get_functionname_` will processes getter function.

```java
@PreferenceFunction(keyname = "nickname")
public String putUserNickFunction(String nickname) {
    return "Hello, " + nickname;
}

@PreferenceFunction(keyname = "nickname")
public String getUserNickFunction(String nickname) {
    return nickname + "!!!";
}
```

### Security
SharedPreferences data are not safe from hacking even if private-mode.<br>
When saving private-user data on SharedPreference, we can save by encrypting and decrypt algorithm with PreferenceFunction.

```java
@PreferenceFunction(keyname = "uuid")
public String putUuidFunction(String uuid) {
   return SecurityUtils.encrypt(uuid);
}

@PreferenceFunction(keyname = "uuid")
public String getUuidFunction(String uuid) {
    return SecurityUtils.decrypt(uuid);
}
```

## PreferenceComponent
![preferencecomponent](https://user-images.githubusercontent.com/24237865/33240928-10a88e18-d302-11e7-8ff5-b5d4f33de692.png) <br>
PreferenceComponent integrates entities. `@PreferenceComponent` annotation is used on an interface.<br>
`@PreferenceComponent`'s 'entities' values are targets to integrated by component.<br>
PreferenceComponent's instance also singletons. And all entities instances are initialized when the component is initialized.<br>
```java
@PreferenceComponent(entities = {Profile.class, Device.class})
public interface UserProfileComponent {
}
```
After the build process, can using `PreferenceComponent_(component's name)` class. <br>

The best way to initialize component is initializing on Application class. because `PreferenceRoom`'s instances are singleton process.

```java
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceComponent_UserProfileComponent.init(this);
    }
}

```
After initializing on Application Class, we can access anywhere to component and component's entities.<br>
```java
Preference_UserProfile userProfile = PreferenceComponent_UserProfileComponent.getInstance().UserProfile();
Preference_UserDevice userDevice = PreferenceComponent_UserProfileComponent.getInstance().UserDevice();
```

## Dependency Injection
![di](https://user-images.githubusercontent.com/24237865/33241294-bfaf9b5a-d306-11e7-816a-2be938fafdf8.png) <br>
All we know already about __dependency injection__. <br>
`PreferenceRoom` supplies simple dependency injection process with free from reflection. <br>

First, we need to declare targets who will be injected by `PreferenceRoom` at Component.<br>
```java
@PreferenceComponent(entities = {Profile.class, Device.class})
public interface UserProfileComponent {
    /**
     * declare dependency injection targets.
     */
    void inject(MainActivity __);
    void inject(LoginActivity __);
}
```

Next, we should inject instances of entity and component at targets. </br>
```java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);
   PreferenceComponent_UserProfileComponent.getInstance().inject(this);
}
```

The last, request dependency injection using `@InjectPreference` annotation. <br>
As we know, field's modifier should be a public.
```java
@InjectPreference
public PreferenceComponent_UserProfileComponent component;

@InjectPreference
public Preference_UserProfile userProfile;
```

## Usage in Kotlin
First, create an entity.
The most important thing is we shuold use `open` modifier at entity classes and PreferenceFunctions.
And field's modifier should be `@JvmField val`.
```java
@PreferenceEntity(name = "UserDevice")
open class Device {
    @KeyName(name = "version")
    @JvmField val deviceVersion: String? = null

    @KeyName(name = "uuid")
    @JvmField val userUUID: String? = null

    @PreferenceFunction(keyname = "uuid")
    open fun putUuidFunction(uuid: String?): String? {
        return SecurityUtils.encrypt(uuid)
    }

    @PreferenceFunction(keyname = "uuid")
    open fun getUuidFunction(uuid: String?): String? {
        return SecurityUtils.decrypt(uuid)
    }
}
```

Second, create a `Component` like below.
```java
@PreferenceComponent(entities = arrayOf(Profile::class, Device::class))
interface UserProfileComponent {
    /**
     * declare dependency injection targets.
     */
    fun inject(target: MainActivity)
    fun inject(target: LoginActivity)
}
```

And the last, injecting is the same with the java. but we should declare component's modifier as lateinit var.<br>
That's it.
```java
@InjectPreference
lateinit var component: PreferenceComponent_UserProfileComponent
    
override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
   setContentView(R.layout.activity_main)
    PreferenceComponent_UserProfileComponent.getInstance().inject(this) // inject dependency injection to MainActivity.
```
#### Non Existent Type Correction
But if you encounter `NonExistentClass` error at compile time, you should add below codes on your build.gradle. <br>
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

You can debugging SharedPreferences values with Stetho. <br> 
To view your app’s SharedPreferences, open the Resources tab of the Developer Tools window and select LocalStorage. <br>
You will see stored preferences by PreferenceRoom. Clicking a file displays the key-value pairs stored in that file. <br><br>
![screenshot635705571](https://user-images.githubusercontent.com/24237865/43187949-e35f5812-902d-11e8-8aa9-c090b90e96c5.png)

## References
- [How to manage SharedPreferences on Android project more efficiently](https://medium.com/@skydoves/how-to-manage-sharedpreferences-on-android-project-5e6d5e28fee6)

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
