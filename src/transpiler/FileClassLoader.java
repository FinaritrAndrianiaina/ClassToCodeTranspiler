package transpiler;

import java.lang.Class;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringJoiner;

public class FileClassLoader extends ClassLoader{

    private String prefix;
    private String fileName;
    private String filePackageName;
    public FileClassLoader(String prefix,String fileName,String packageName){
        this.prefix = prefix;this.fileName = fileName;this.filePackageName = packageName;
    }
    public String filePackageName() {
        return filePackageName;
    }

    public Class<?> loadIt(){
        byte[] data=loadClassBytesArray(fileName);
        StringJoiner sj = new StringJoiner(".");
        if(filePackageName!=""){
            sj.add(filePackageName);
        }
        sj.add(fileName);
        return defineClass(sj.toString(),data,0,data.length);
    }

    public byte[] loadClassBytesArray(String className){
        byte[] data=null;
        
        try{
            var file = new File(this.prefix+"/"+className+".class");
            try (FileInputStream fis = new FileInputStream(file)) {
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                int ch=0;
                while((ch=fis.read())!=-1){
                    baos.write(ch);
                }
                data=baos.toByteArray();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return data;
    }

    public String getPrefix() {
        return prefix;
    }

    public FileClassLoader copy() {
        return new FileClassLoader(this.prefix,this.fileName,this.filePackageName);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
