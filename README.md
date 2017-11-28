# PreferenceRoom [![Build Status](https://travis-ci.org/skydoves/PreferenceRoom.svg?branch=master)](https://travis-ci.org/skydoves/PreferenceRoom)<br>
![logo](https://user-images.githubusercontent.com/24237865/33322917-375235ae-d48e-11e7-9dda-261f0df5c323.png)
Manage your project's SharedPreferences more efficiently.<br>
PreferenceRoom is inspired by [Architecture Components Room Persistence](https://developer.android.com/topic/libraries/architecture/room.html)
and [dagger](https://github.com/square/dagger).<br>
Fully supported in kotlin project.<br>
PreferenceRoom integrates scattered SharedPreferences as an entity.<br>
It supports putter & getter custom functions with security algorithm and could put & get objects.<br>
Also supports simple preference dependency injection with free from reflection.

## Download
#### Gradle
```java
dependencies {
    implementation 'com.github.skydoves:preferenceroom:1.0.5'
    annotationProcessor 'com.github.skydoves:preferenceroom-processor:1.0.5' // if android java project
    kapt 'com.github.skydoves:preferenceroom-processor:1.0.5' // if android kotlin project
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

## PreferenceEntity
![preferenceentity](https://user-images.githubusercontent.com/24237865/33240687-5fa9ccca-d2fd-11e7-8962-e39c8dad5f41.png)<br>
@PreferenceEntity annotation makes SharedPreferences data as an entity.<br>
"name" value in @PreferenceEntity determines entity's name.<br>
Entity's default naming rule is Class name with camel uppercase.<br>

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
userProfile.containsNickname(); // checks NickName key value is exist in SharedPreference.
userProfile.removeNickname(); // removes NickName key's value in SharedPreference.

// or invoke static.
Preference_UserProfile.getInstance(this).putNickname("my nickname");
```

auto-generated code is managed by singletons. </br>
but manage more efficiently using [PreferenceComponent](https://github.com/skydoves/PreferenceRoom#preferencecomponent) and
[Dependency Injection](https://github.com/skydoves/PreferenceRoom#dependency-injection).

### keyName
![keyname](https://user-images.githubusercontent.com/24237865/33240803-7c80bb7c-d2ff-11e7-98e4-cf43d6aebb1e.png)<br>
@KeyName annotation is used in an entity. <br>
@keyName's name value determines key name with camel uppercase.
```java
@KeyName(name = "visits") // keyname will be Visits.
protected final int visitCount = 1;
```

### TypeConverter
![typeconverter](https://user-images.githubusercontent.com/24237865/33240860-c5b6495a-d300-11e7-8122-804993a07b4a.png)<br>
You can put and get Objects using TypeConverter.<br>
@TypeConverter's converter value determines Converter.<br>
```java
@KeyName(name = "userPet")
@TypeConverter(converter = PetConverter.class)
protected Pet userPetInfo;
```

below example is converter using Gson. <br>
Converter should extends "PreferenceTypeConverter<?>" class.
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

### PreferenceFunction
![preferencefunction](https://user-images.githubusercontent.com/24237865/33240543-c292ee82-d2fa-11e7-86c2-b013830965b2.png)<br>
@PreferenceFunction annotation processes getter and setter functions. <br>
@PreferenceFunction's "keyname" value determines a target. The target should be a keyName. <br>
Function's name should start with "put" or "get" prefix. <br>
put_functionname_ will processes getter function and get_functionname_ will processes getter function.

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

### security
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
PreferenceComponent integrates entities. @PreferenceComponent annotation is used on an interface.<br>
@PreferenceComponent's 'entities' values are targets to integrated by component.<br>
PreferenceComponent's instance also singletons. And all entities instances are initialized when the component is initialized.<br>
```java
@PreferenceComponent(entities = {Profile.class, Device.class})
public interface UserProfileComponent {
}
```
After the build process, can using PreferenceComponent_(component's name) class. <br>

The best way to initialize component is initializing on Application class. because PreferenceRoom's instances are singleton process.

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
All we know already about dependency injection. <br>
PreferenceRoom supplies simple dependency injection process with free from reflection. <br>

first, we need to declare targets who will be injected by PreferenceRoom at Component.<br>
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

and next, we should inject instances of entity and component at targets. </br>
```java
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.activity_main);
   PreferenceComponent_UserProfileComponent.getInstance().inject(this);
}
```

the last, request dependency injection using @InjectPreference annotation. <br>
As we know, field's modifier should be a public.
```java
@InjectPreference
public PreferenceComponent_UserProfileComponent component;

@InjectPreference
public Preference_UserProfile userProfile;
```

## Usage in Kotlin
Already know if you interested in kotlin. but this section is for newbies.

Firstly we should create Component as Kotlin.
```kotlin
@PreferenceComponent(entities = arrayOf(Profile::class, Device::class))
interface UserProfileComponent {
    /**
     * declare dependency injection targets.
     */
    fun inject(target: MainActivity)
    fun inject(target: LoginActivity)
}
```

And the last, injecting is the same with java. but we should declare component's modifier as lateinit var.<br>
That's it.
```kotlin
@InjectPreference
lateinit var component: PreferenceComponent_UserProfileComponent
    
override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
   setContentView(R.layout.activity_main)
    PreferenceComponent_UserProfileComponent.getInstance().inject(this) // inject dependency injection to MainActivity.
```

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
