package mrk.utils;

import immibis.bon.*;
import immibis.bon.gui.Side;
import immibis.bon.io.ClassCollectionFactory;
import immibis.bon.io.JarWriter;
import immibis.bon.mcp.MappingFactory;
import immibis.bon.mcp.MappingLoader_MCP;
import immibis.bon.mcp.MappingLoader_MCP.CantLoadMCPMappingException;
import immibis.bon.mcp.MinecraftNameSet;
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Reverse extends Task {
    public static void startReverse(String mcp, String input) {
        Task.mcp = mcp;
        Task.input = input;
        Task.modFile = new File(input);
        Task.output = getDeobfPath() + modFile.getName();
        startDeobf();
    }

    static void startDeobf() {
        System.out.println("Deobfuscating mod...");
        createDir(getDeobfPath());

        final Side side = Side.Universal;
        final File mcpDir = new File(mcp);
        final String[] refPathList = side.referencePath.split(File.pathSeparator);
        final File inputFile = new File(input);
        final File outputFile = new File(output);

        try {
            IProgressListener progress = new IProgressListener() {
                @Override
                public void set(int arg0) {
                }

                @Override
                public void setMax(int arg0) {
                }

                @Override
                public void start(int arg0, String arg1) {
                }
            };

            String mcVer = MappingLoader_MCP.getMCVer(mcpDir);

            MinecraftNameSet refNS = new MinecraftNameSet(MinecraftNameSet.Type.MCP, side.nsside, mcVer);
            Map<String, ClassCollection> refCCList = new HashMap<String, ClassCollection>();

            for (String s : refPathList) {
                File refPathFile = new File(mcpDir, s);
                refCCList.put(s, ClassCollectionFactory.loadClassCollection(refNS, refPathFile, progress));
            }

            MinecraftNameSet.Type[] remapTo;
            MinecraftNameSet.Type inputType;

            inputType = MinecraftNameSet.Type.OBF;
            remapTo = new MinecraftNameSet.Type[]{MinecraftNameSet.Type.SRG, MinecraftNameSet.Type.MCP};

            MinecraftNameSet inputNS = new MinecraftNameSet(inputType, side.nsside, mcVer);
            ClassCollection inputCC = ClassCollectionFactory.loadClassCollection(inputNS, inputFile, progress);
            MappingFactory.registerMCPInstance(mcVer, side.nsside, mcpDir, progress);

            for (MinecraftNameSet.Type outputType : remapTo) {
                MinecraftNameSet outputNS = new MinecraftNameSet(outputType, side.nsside, mcVer);

                List<ClassCollection> remappedRefs = new ArrayList<>();
                for (Map.Entry<String, ClassCollection> e : refCCList.entrySet()) {
                    if (inputCC.getMinecraftNameSet().equals(e.getValue().getMinecraftNameSet())) {
                        remappedRefs.add(e.getValue());

                    } else {
                        remappedRefs.add(getRemapper(e.getValue(), inputCC.getMinecraftNameSet(), Collections.emptyList(), progress));
                    }
                }
                List<ReferenceDataCollection> referenceDataCollections = new ArrayList<>();

                for (ClassCollection classCollection : remappedRefs)
                    referenceDataCollections.add(ReferenceDataCollection.fromClassCollection(classCollection));

                inputCC = getRemapper(inputCC, outputNS, referenceDataCollections, progress);
            }

            JarWriter.write(outputFile, inputCC, progress);
            System.out.println("Deobfuscating completed.");
            startDecomp();
        } catch (IOException | ClassFormatException | CantLoadMCPMappingException | MappingFactory.MappingUnavailableException e) {
            e.printStackTrace();
        }
    }

    static void startDecomp() {
        System.out.println("Decompiling mod...");
        createDir(getSrcPath());
        ConsoleDecompiler.main(new String[]{getDeobfPath() + modFile.getName(), getSrcPath()});
        System.out.println("Decompiling completed");
    }

    static void createDir(String path) {
        File tempDir = new File(path);
        tempDir.mkdirs();
    }

    public static String getDeobfPath() {
        return modFile.getParent() + "/reverse/deobf/";
    }

    static String getSrcPath() {
        return modFile.getParent() + "/reverse/src/";
    }

    private static ClassCollection getRemapper(ClassCollection classCollection, MinecraftNameSet minecraftNameSet, Collection<ReferenceDataCollection> referenceDataCollections, IProgressListener progressListener) throws MappingFactory.MappingUnavailableException {
        return Remapper.remap(classCollection, MappingFactory.getMapping(classCollection.getMinecraftNameSet(), minecraftNameSet, null), referenceDataCollections, progressListener);
    }
}