# PreferenceRoom
Manage your project's SharedPreference more efficiently.<br>
PreferenceRoom inspired by [Architecture Components Room Persistence](https://developer.android.com/topic/libraries/architecture/room.html)
and [dagger](https://github.com/square/dagger).

## Download
#### Gradle
```java
dependencies {
    implementation 'com.github.skydoves:preferenceroom:1.0.3'
    annotationProcessor 'com.github.skydoves:preferenceroom-processor:1.0.3'
}
```

## PreferenceEntity
![preferenceentity](https://user-images.githubusercontent.com/24237865/33240687-5fa9ccca-d2fd-11e7-8962-e39c8dad5f41.png)<br>
@PreferenceEntity annotation makes SharedPreference data as an entity.<br>
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

After the build process, can using Preference_(entity's name) class like following. <br>
```java
Preference_UserProfile userProfile = Preference_UserProfile.getInstance(this);
userProfile.putNickname("my nickname"); // puts SharedPreference in NickName key.
userProfile.getNickname(); // gets SharedPreference value in NickName key.
userProfile.containsNickname(); // checks NickName key value is exist in SharedPreference.
userProfile.removeNickname(); // removes NickName key's value in SharedPreference.

// or invoke static.
Preference_UserProfile.getInstance(this).putNickname("my nickname");
```

auto-generated code is managed by singleton. <br>
but manage more efficiently using [PreferenceComponent](https://github.com/skydoves/PreferenceRoom#preferencefunction) and
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

below example is converter using with Gson. <br>
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
@PreferenceFunction's keyname value sets a target of a key. <br>
Function's name should start with put or get prefix. <br>
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
SharedPreference is not safe from hacking even if private-mode.<br>
When saving private-user data on SharedPreference, you can save with encrypt and decrypt algorithm with PreferenceFunction.

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


## Dependency Injection

