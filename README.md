# Chrome Password Dumper [![Build Status](https://travis-ci.org/fletchto99/Chrome-Password-Dumper.svg?branch=master)](https://travis-ci.org/fletchto99/Chrome-Password-Dumper)

Chrome password dumper is a program used to recover lost saved passwords in google chrome, without the requirement of a master password.

##Limitations

* Windows: None. Will dump all passwords with ease.
* Mac: Requires access to keychain. In most cases this will just be a simple popup dialog saying Allow/Deny. In the case that the master password for keychain was changed, it will require that password. 
* Linux/Other OSes: Not yet supported. I don't plan on supporting linux any time soon.

##How to use

1. Download the most recent build from https://travis-ci.org/fletchto99/Chrome-Password-Dumper/builds
2. Execute JAR file (via commandline using `Java -jar [Path to build here]`)
3. IF you are on Mac allow the program access to keychain's passwords via the popup boxes.
4. All passwords will be dumped to a "Accouts" folder in the directory of the jar file.

## License
Don't use this for evil, please!
