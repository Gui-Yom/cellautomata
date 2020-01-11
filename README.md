# cellautomata
Conway's Game of Life : [https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life][wikipedia_gol] and other rules

### Tf is this ?
This is a java implementation of a Game Of Life computation engine (see link above).

It can run multiple generations and record them to some popular formats :
 - GIF
 - APNG
 - BMP
 - PNG

### Example
Here's an example of what can be achieved :

![example gif][example_gif]

### Installation
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
        <groupId>com.github.Gui-Yom</groupId>
	    <artifactId>cellautomata</artifactId>
	    <version>$tag$</version>
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
    implementation "com.github.Gui-Yom:cellautomata:$tag$"
}
```

### Contributing
Easy ! See [CONTRIBUTING.md][contributing]

[wikipedia_gol]: https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
[example_gif]: output.gif "Example"
[jitpack-badge]: https://jitpack.io/v/Gui-Yom/cellautomata.svg "jitpack badge"
[jitpack-url]: https://jitpack.io/#Gui-Yom/cellautomata
[contributing]: https://github.com/Gui-Yom/cellautomata/blob/master/CONTRIBUTING.md
