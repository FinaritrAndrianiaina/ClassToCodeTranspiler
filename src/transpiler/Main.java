package transpiler;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        FileClassLoader fCL = new FileClassLoader("/home/fisa/TranspilerProject/out/production/TranspilerProject/transpiler","JavaCode","transpiler");
        var transpiler = new Transpiler(fCL);
        transpiler.setCode(new CsharpCode());
        transpiler.transpile();
    }
}   