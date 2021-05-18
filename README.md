# Architectury API

Talk to us on [Discord](https://discord.gg/C2RdJDpRBP)!

An intermediary api aimed to ease developing multiplatform mods.

### What is Architectury API

Architectury API is an api to abstract calls to fabric api and forge api as both loader has different implementations of
what can be perceived as the same thing.

Architectury API updates regularly, with new hooks and features. Currently contains over **90** events hooks, networking
abstraction, loader calls abstraction, game registry abstraction and an easy to use @ExpectPlatform annotation (Only
works on static methods).

### Do I really need this API?

Architectury API is only one part of the architectury ecosystem, **Architectury Plugin** is the gradle plugin enabling
all this multiplatform actions.

Architectury API is optional for projects built on architectury, you may create your architectury project with just
Architectury Plugin.

### Advantages of Architectury

- Open sourced
- Less boilerplate for your multiplatform mod

### Getting started with making multiplatform mods

Gradle Plugin: https://github.com/architectury/architectury-plugin

Example Mod: https://github.com/architectury/architectury-example-mod

### Credits

This library bundles typetools, which you can find its
license [here](https://github.com/jhalterman/typetools/blob/master/LICENSE.txt "")
