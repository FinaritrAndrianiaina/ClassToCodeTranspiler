import transpiler.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            String fileprefix = args[0];    
            String classname = args[1];
            Code lang = null;
            switch (args[2]) {
                case "java":
                    lang = new JavaCode();
                    break;
                case "csharp":
                    lang = new CsharpCode();
                    break;
                case "ts":
                    lang = new TsCode();
                    break;
                default:
                    break;
            }
            String packagename = "";
            if(args.length>3){
                packagename =args[3];
            }
            FileClassLoader fCL = new FileClassLoader(fileprefix,classname,packagename);
            var transpiler = new Transpiler(fCL);
            transpiler.setCode(lang);
            transpiler.transpile();
        } catch (Exception e) {
            System.out.println("java Main [fileprefix] [classname]  [java/csharp] [packagename]");
            System.out.println("ex: java Main ../examples/transpiler/ JavaCode java transpiler");
            System.out.println(e.getMessage());
        }
    }
}   
