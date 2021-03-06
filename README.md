# BadgeFactory
Simple factory class for building numbered badges for Android.  Perfect for notification labels.  It will draw a shaped bitmap with the correct dimensions for a badge

## Dependencies
There are no external dependencies outside of this being exclusively for android.  It is supported for api level 10+.

## Installation

Sorry folks, this class doesn't warrant having its own artifact at this point, HOWEVER "installing" it is easy enough:

* clone repo OR simply copy paste class into your project
* Change Namespace to your own, it won't compile until you do

Thats it!  From there you can use it within your adapter classes.  Heres a quick example

```java

public Adapter(Context context) {
    this.mFactory = new BadgeFactory(context, Color.parseColor("red"));
    ...
}

@Override
public View getView(int pos, View view, ViewGroup viewGroup) {
    ...
    someImageView.setImageBitmap(factory.build(someCount));
}
    

```

Currently, there are a couple configurable parameters; you can set the text size (which takes a SP value, not a regular pixel value), via setTextSize(int size):

```java

    factory.setTextSize(16); //16 sp.  Badge Factory will do the rest
    //sets the color of the badge for subsequent build() calls
    factory.setBadgeColor(Color.parseColor("blackerthanblack"));
```

Thats it.  Enjoy.

## TODOs

* Allow for different shapes as badges (Stars anyone)?
* More shader effects.
* Allow different font typefaces. (default is bold)
* allow for specified font color (default is white)
