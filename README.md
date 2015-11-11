##**MT Flickr**

MT Flickr is based on the famous Flickr photos sharing service. This is possible by using the public Flickr API. 
It's mainly fetches photos through a Flickr REST Api. 

##**MT Flickr Features**

* Users of MT Flickr will able to fetch photos by searching for any particular keyword.
* Display photos in a RecyclerView including the title on each cell.
* Display more photos for a particular user when tap on any of the selected photo (cell). 
* Using volley framwork for networking functionality and to implement a caching solution so when the same search keyword is used the data is retrieved from the cache.
* Supporting all Common AndroidScreen sizes 
* Supporting three different styles for 5.0-, 5.0 "Lollipop" and 6.0 "Marshmallow"(i.e. Changing Colors). 
* Supporting Android 4.0+


##**API Methods**

##**Extra-API Methods**

####**people**
* flickr.people.getPhotos

####**photos**
* flickr.photos.search

##**Download**

You can use Gradle:
```
dependencies {
    compile 'com.android.support:appcompat-v7:23.1.0'
    compile "com.android.support:support-v4:23.1.0"
    compile "com.mcxiaoke.volley:library:1.0.16"
    compile "com.android.support:recyclerview-v7:23.1.0"
    compile 'com.github.bumptech.glide:glide:3.6.1'
    compile 'com.github.bumptech.glide:volley-integration:1.3.1@aar'
    compile 'com.squareup.picasso:picasso:2.5.1'
    compile 'com.google.code.gson:gson:2.2.+'
}
```

##**Request Caching**
Using Volley framwork and it's powerful cache mechanism to maintain request cache. This saves lot of internet bandwidth and reduces user waiting time. Following are few example of using volley cache methods.
* Like below you can check for a cached response of an URL before making a network call.
```
Cache cache = AppController.getInstance().getRequestQueue().getCache();
Entry entry = cache.get(url);
if(entry != null){
    try {
        String data = new String(entry.data, "UTF-8");
        // handle data, like converting it to xml, json, bitmap etc.,
    } catch (UnsupportedEncodingException e) {      
        e.printStackTrace();
        }
    }
}else{
    // Cached response doesn't exists. Make network call here
}
```

##**Author**
Mahmoud Turki © MT
