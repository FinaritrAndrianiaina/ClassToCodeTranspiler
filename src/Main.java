public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        FileClassLoader fCL = new FileClassLoader("/home/fisa/TranspilerProject/out/production/TranspilerProject/");
        var transpiler = new Transpiler(fCL);
        transpiler.setCode(new JavaCode());
        System.out.println(transpiler.transpile("Code"));
        System.out.println(transpiler.transpile("JavaCode"));
    }
}   