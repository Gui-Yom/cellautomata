# glife-lib
Conway's Game of Life : [https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life][wikipedia_gol]

### Tf is this ?
This is a java implementation of a Game Of Life computation engine (see link above).
It takes an image or a serialized (.gld) file as input. You can also draw the points yourself.

Then it can run multiple generations and record them to some popular formats :
 - GIF
 - APNG
 - BMP
 - PNG
 - GLD (A new format created on purpose) : it holds enough information to restore the computation state later.

### Example
Here's an example of what can be achieved :

![example gif][example_gif]

### Library usage
[![Release][jitpack-badge]][jitpack-url]

Add the JitPack repository to your buildfile :

Maven :
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.LimeiloN</groupId>
	    <artifactId>glife-lib</artifactId>
	    <version>tag</version>
    </dependency>
</dependencies>
```

Gradle :
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    implementation "com.github.LimeiloN:glife-lib:tag"
}
```

### Contributing
Easy ! See [CONTRIBUTING.md][contributing]

[wikipedia_gol]: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
[example_gif]: output.gif "Example"
[jitpack-badge]: https://jitpack.io/v/LimeiloN/glife-lib.svg "jitpack badge"
[jitpack-url]: https://jitpack.io/#LimeiloN/glife-lib
[contributing]: https://github.com/LimeiloN/glife-lib/blob/master/CONTRIBUTING.md
