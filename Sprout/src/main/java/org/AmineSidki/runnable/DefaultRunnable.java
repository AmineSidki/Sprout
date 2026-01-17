package org.AmineSidki.runnable;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import picocli.CommandLine;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

@CommandLine.Command(name="default" , version = "0.1" , description = "Default Sprout workflow")
public class DefaultRunnable implements Runnable{

    @Override
    public void run() {

        File entityPackage = new File("./entity");
        File[] files = entityPackage.listFiles();

        if(files == null){
            throw new RuntimeException("Couldn't resolve entity package directory !");
        }

        JavaParser parser = new JavaParser();
        MustacheFactory mf = new DefaultMustacheFactory();

        for(File entity : files){
            if(!entity.isFile()){
                continue;
            }

            StringBuilder sb = new StringBuilder();
            try {
                Scanner fileScanner = new Scanner(entity);
                while(fileScanner.hasNextLine()){
                    sb.append(fileScanner.nextLine());
                    sb.append("\n");
                }
                fileScanner.close();

                //Parsing the code
                ParseResult<CompilationUnit> pr = parser.parse(sb.toString());
                CompilationUnit cu = pr.getResult().isPresent() ? pr.getResult().get() : null;

                if(cu == null){
                    throw new RuntimeException("An error occurred whilst parsing file : " + entity.getName());
                }

                // This stunt was pulled by intelliJ to make it possible to access these fields from the visitor (wtf ?!)
                final boolean[] isIdPresent = {false};
                final boolean[] isIdLong = {true};
                final String[] pack = new String[1];

                // Storing the fields this way : { Field:Type }
                // Leave this one for when you can implement Dto generation smh...
                HashMap<String , String> fields = new HashMap<>();

                //Get the full Package name: com.example.packageName.etc.entity
                new VoidVisitorAdapter<Void>(){
                    @Override
                    public void visit(PackageDeclaration pd, Void arg) {
                        super.visit(pd , arg);
                        pack[0] = pd.getNameAsString();
                    }
                }.visit(cu , null);

                new VoidVisitorAdapter<Void>(){
                    @Override
                    public void visit(FieldDeclaration fd, Void arg) {
                        super.visit(fd , arg);

                        if(fd.isAnnotationPresent("Id")){
                            isIdPresent[0] = true;
                            if(!fd.getElementType().toString().equals("Long")){
                                isIdLong[0] = false;
                            }
                        }

                        // For every multi-variable(1 or more at once) declaration this will execute :
                        for(VariableDeclarator vd : fd.getVariables()){
                            fields.put(vd.getNameAsString() , fd.getElementType().toString());
                        }
                    }
                }.visit(cu , null);

                //If there is no @Id annotated fields it throws an exception.
                if(!isIdPresent[0]){
                    throw new RuntimeException("No Id annotation present in supposed entity " + entity.getName() + ".");
                }

                //If it gets to this point then the parsing was successful.
                //TODO: Don't forget to implement other multiplicity annotations' support, but that will be important once you implement DTOs

                String className = entity.getName().split(Pattern.quote(".java"))[0];

                //Building the necessary components for repoMustache
                Mustache repoMustache = mf.compile("templates/RepositoryTemplate.mustache");

                //Create the Repository package if it doesn't exist yet
                File repoPackage = new File("./repository");
                if(!repoPackage.exists() && !repoPackage.mkdir()){
                    throw new RuntimeException("An error occurred whilst generating files.");
                }

                File repoFile = new File("./repository/" + className + "Repository.java");

                if(!repoFile.exists() && !repoFile.createNewFile()){
                    throw new RuntimeException("An error occurred whilst generating files.");
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter(repoFile));
                HashMap<String , Object> repoContext = new HashMap<>();

                //Replaces the .entity in the package --> .repository ; This same treatment will be done for the restant of the components, no need to document them as well.
                repoContext.put("PackageName" , pack[0].replaceFirst("entity" , "repository"));
                repoContext.put("ClassName" , className);
                repoContext.put("IdType" , isIdLong[0]);

                repoMustache.execute(writer,repoContext).flush();

            } catch (FileNotFoundException e) {
                throw new RuntimeException("An error occurred whilst reading files.");
            } catch (IOException e) {
                System.out.println(e);
                throw new RuntimeException("An error occurred whilst generating files.");
            }
        }

    }
}
