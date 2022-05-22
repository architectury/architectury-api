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

Architectury Loom: https://github.com/architectury/architectury-loom (a fork of Fabric Loom adding multiplatform development capabilities)
Gradle Plugin: https://github.com/architectury/architectury-plugin (includes Architectury Injectables, for the `@ExpectPlatform` annotation)
Example Mod: https://github.com/architectury/architectury-example-mod (a discontinued example mod using Architectury, if possible, use the templates instead)
Mod Templates: https://github.com/architectury/architectury-templates (a set of templates to get started using the Architectury toolchain)

### Example: Usage of @ExpectPlatform
![Exmaple of the @ExpectPlatform annotation, part of Architectury Injectables](https://camo.githubusercontent.com/78c68766affb70fbd88f9806e0e95f78765ec339448d7102065f2942be2b3215/68747470733a2f2f6d656469612e646973636f72646170702e6e65742f6174746163686d656e74732f3538363138363230323738313138383130382f3737363432383831343330393738353632302f756e6b6e6f776e2e706e673f77696474683d31313931266865696768743d343339)

### Credits

In certain older versions, this library used to bundle typetools; you can find its license [here](https://github.com/jhalterman/typetools/blob/master/LICENSE.txt "")
