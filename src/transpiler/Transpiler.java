package transpiler;

import java.lang.Class;

import transpiler.Code;
import transpiler.FileClassLoader;

public class Transpiler {
    private Code builder;
    private final FileClassLoader fCl;

    public Transpiler(FileClassLoader fCl) {
        this.fCl = fCl;
    }


    public void setCode(Code builder) {
        this.builder = builder;
    }

    public void transpile() {
        Class<?> classToTranspile = this.fCl.loadIt();
        this.builder.setClassFile(classToTranspile);
        this.builder.setClassLoader(this.fCl);
        this.builder.assemble();
    }
}
