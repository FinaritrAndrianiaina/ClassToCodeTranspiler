# ClassToCodeTranspiler

This program can convert compiled java .class to another programming language

You need java 11 for this to work.

## How to use

```bash
    git clone git@github.com:FinaritrAndrianiaina/ClassToCodeTranspiler.git
    # First method: ./run-it.sh [fileprefix] [classname]  [java/csharp] [packagename(optionnel)] 
    ./run-it.sh examples/transpiler JavaCode java transpiler

    cd ClassToCodeTranspiler/src


    # java Main [fileprefix] [classname]  [java/csharp] [packagename(optionnel)]
    
    javac Main.java

    java Main ../examples/transpiler/ JavaCode java transpiler
    
    java Main /path/to/class/ ClassName java
```

