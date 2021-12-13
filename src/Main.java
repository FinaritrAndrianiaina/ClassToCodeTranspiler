import transpiler.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        FileClassLoader fCL = new FileClassLoader("/media/fisa/PARROT/TranspilerProject/src/transpiler","Code","transpiler");
        var transpiler = new Transpiler(fCL);
        transpiler.setCode(new CsharpCode());
        transpiler.transpile();
    }
}   